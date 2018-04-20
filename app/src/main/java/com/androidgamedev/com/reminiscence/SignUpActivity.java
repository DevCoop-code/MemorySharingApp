package com.androidgamedev.com.reminiscence;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidgamedev.com.reminiscence.server.ServerInfo;
import com.androidgamedev.com.reminiscence.server.SignUpService;
import com.androidgamedev.com.reminiscence.server.UserInfoStatusDTO;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.security.AccessController.getContext;

public class SignUpActivity extends Activity
{
    private SignUpTask mSignupTask = null;

    private EditText email_edit;
    private EditText nickname_edit;
    private EditText password_edit;
    private View mProgressView;
    private View mSignUpFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email_edit = (EditText)findViewById(R.id.email);
        nickname_edit = (EditText)findViewById(R.id.nickname);
        password_edit = (EditText)findViewById(R.id.password);

        Button signup_btn = (Button)findViewById(R.id.email_sign_up_button);
        signup_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                attemptSignUp();
            }
        });

        mProgressView = findViewById(R.id.signup_progress);
        mSignUpFormView = findViewById(R.id.signup_form);
    }

    private void attemptSignUp()
    {
        String email = email_edit.getText().toString();
        String password = password_edit.getText().toString();
        String nickname = nickname_edit.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //유저가 입력한 비밀번호의 유효성 검사
        if(TextUtils.isEmpty(password) && !isPasswordValid(password))
        {
            password_edit.setError(getString(R.string.error_invalid_password));
            focusView = password_edit;
            cancel = true;
        }

        //유저가 입력한 이메일 주소 유효성 검사 - 입력 여부
        if(TextUtils.isEmpty(email))
        {
            email_edit.setError(getString(R.string.error_field_required));
            focusView = email_edit;
            cancel = true;
        }else if(!isEmailValid(email))  //유효한 이메일 형식인지 여부
        {
            email_edit.setError(getString(R.string.error_invalid_email));
            focusView = email_edit;
            cancel = true;
        }

        //유저가 입력한 닉네임의 유효성 검사
        if(TextUtils.isEmpty(nickname))
        {
            nickname_edit.setError(getString(R.string.error_exists_email));
            focusView = nickname_edit;
            cancel = true;
        }
        if(!isNicknameValid(nickname))
        {
            nickname_edit.setError(getString(R.string.error_invalid_nickname));
            focusView = nickname_edit;
            cancel = true;
        }

        if(cancel)
        {
            focusView.requestFocus();
        }else
        {
            showProgress(true);
            mSignupTask = new SignUpTask(email, password, nickname);
            mSignupTask.execute();
        }
    }

    private boolean isPasswordValid(String password)
    {
        return password.length() > 4;
    }
    private boolean isEmailValid(String email)
    {
        return email.contains("@");
    }
    private boolean isNicknameValid(String nickname)
    {
        return nickname.length() < 10;
    }

    /*
    ProgressBar UI 보여주기
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSignUpFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }else
        {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class SignUpTask extends AsyncTask<Void, Void, Boolean>
    {
        private final String mEmail;
        private final String mPassword;
        private final String mNickname;
        private String status = null;

        SignUpTask(String email, String password, String nickname)
        {
            Log.v("SignUpActivity", "AsyncTask Execute!!");
            mEmail = email;
            mPassword = password;
            mNickname = nickname;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            HashMap<String, Object> signupInfo = new HashMap<>();
            signupInfo.put("uemail",mEmail);
            signupInfo.put("upw",mPassword);
            signupInfo.put("uname",mNickname);

            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(ServerInfo.server_address)
                    .build();
            SignUpService signupService = retrofit.create(SignUpService.class);
            Call<UserInfoStatusDTO> signup = signupService.signupEvent(signupInfo);
            signup.enqueue(new Callback<UserInfoStatusDTO>(){
                @Override
                public void onResponse(Call<UserInfoStatusDTO> call, Response<UserInfoStatusDTO> response)
                {
                    if(response.isSuccessful())
                    {
                        Log.v("SignUpActivity", "Network Successful");
                        status = response.body().getStatus();

                        //회원가입에 성공한 경우
                        if(status.equals("success"))
                        {
                            Toast.makeText(getApplicationContext(),"회원가입을 축하드립니다.",Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(i);
                        }else if(status.equals("alreadyexists")) //회원가입에 성공하지 못함 - 이미 존재하는 경우
                        {
                            Toast.makeText(getApplicationContext(),"이미 존재하는 계정입니다.",Toast.LENGTH_SHORT).show();
                        }
                    }else
                    {
                        Log.v("SignUpActivity", response.body().getStatus());
                        Log.v("SignUpActivity", "Network Successful Calling");
                        status = "responseproblem";
                    }
                }
                @Override
                public void onFailure(Call<UserInfoStatusDTO> call, Throwable t)
                {
                    Toast.makeText(getApplicationContext(),"죄송합니다 현재 서버에 문제가 발생하였습니다.",Toast.LENGTH_SHORT).show();
                    Log.v("SignUpActivity", "Network Failure");
                }
            });
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success)
        {
            mSignupTask = null;
            showProgress(false);
        }

        @Override
        protected void onCancelled()
        {
            mSignupTask = null;
            showProgress(false);
        }
    }
}
