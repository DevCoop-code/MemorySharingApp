package com.androidgamedev.com.reminiscence;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidgamedev.com.reminiscence.contact.UserContactData;
import com.androidgamedev.com.reminiscence.contact.UserDBManager;
import com.androidgamedev.com.reminiscence.contact.UserDBSqlData;
import com.androidgamedev.com.reminiscence.server.ResponseLoginInfoDTO;
import com.androidgamedev.com.reminiscence.server.ServerInfo;
import com.androidgamedev.com.reminiscence.server.SignUpService;
import com.androidgamedev.com.reminiscence.server.UserInfoStatusDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A login screen that offers login via email/password.
 */
public class LoginFragment extends Fragment
{
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    public LoginFragment()
    {

    }

    public static LoginFragment newInstance()
    {
        LoginFragment loginFragment = new LoginFragment();
        return loginFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View loginView = inflater.inflate(R.layout.activity_login, container, false);

        mEmailView = (EditText) loginView.findViewById(R.id.email);
        mPasswordView = (EditText)loginView.findViewById(R.id.password);
        Button mEmailSignInButton = (Button)loginView.findViewById(R.id.email_sign_in_button);
        Button mEmailSignUpButton = (Button)loginView.findViewById(R.id.email_sign_up_button);

        mEmailSignInButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                attemptLogin();
            }
        });

        mEmailSignUpButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(getContext(),"회원가입",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), SignUpActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });
        mLoginFormView = loginView.findViewById(R.id.login_form);
        mProgressView = loginView.findViewById(R.id.login_progress);

        return loginView;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin()
    {
        if (mAuthTask != null)
        {
            return;
        }
        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) && !isPasswordValid(password))
        {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email))
        {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email))
        {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel)
        {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }else
        {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email)
    {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password)
    {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }else
        {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean>
    {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password)
        {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            HashMap<String, Object> signinInfo = new HashMap<>();
            signinInfo.put("id", mEmail);
            signinInfo.put("password", mPassword);

            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(ServerInfo.server_address)
                    .build();

            SignUpService signinService = retrofit.create(SignUpService.class);
            Call<ResponseLoginInfoDTO> signin = signinService.signinEvent(signinInfo);
            signin.enqueue(new Callback<ResponseLoginInfoDTO>()
            {
                @Override
                public void onResponse(Call<ResponseLoginInfoDTO> call, Response<ResponseLoginInfoDTO> response)
                {
                    if(response.isSuccessful())
                    {
                        String status = response.body().getStatus();
                        String name = response.body().getUsername();

                        //로그인에 성공한 경우
                        if(status.equals("loginsuccess"))
                        {
                            Toast.makeText(getContext(),"로그인에 성공하셨습니다.",Toast.LENGTH_SHORT).show();
                            String[] emails = {mEmail};
                            //android mysqllite에 저장되어 있는 계정인 여부를 확인
                            UserDBManager dbMgr = new UserDBManager(getContext());
                            dbMgr.dbOpen();
                            String userEmail = dbMgr.checkNewOrNot(UserDBSqlData.CHECK_ACCOUNT, emails);

                            //저장되어 있지 않은 계정이면 저장
                            if(userEmail == null)
                            {
                                Log.v("LoginFragment", "저장되어 있지 않은 계정입니다.");
                                UserContactData user_account = new UserContactData(mEmail, name, "0");
                                dbMgr.insertData(UserDBSqlData.SQL_DB_INSERT_DATA, user_account);
                                dbMgr.dbClose();
                            }
                            else    //uauth값 갱신
                            {
                                dbMgr.updateUauth(UserDBSqlData.SQL_DB_UPDATE_AUTH2, emails);
                                dbMgr.dbClose();
                            }

                            Intent i = new Intent(getContext(), HomeActivity.class);
                            startActivity(i);
                        }else if(status.equals("loginfailure"))
                        {
                            Toast.makeText(getContext(),"로그인에 실패하셨습니다.",Toast.LENGTH_SHORT).show();
                        }
                    }else
                    {
                        Toast.makeText(getContext(), "서버로부터 응답을 제대로 받지 못했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseLoginInfoDTO> call, Throwable t)
                {
                    Toast.makeText(getContext(), "현재 서버에 문제가 발생하여 서비스에 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            mAuthTask = null;
            showProgress(false);
        }

        @Override
        protected void onCancelled()
        {
            mAuthTask = null;
            showProgress(false);
        }
    }
}