package com.androidgamedev.com.reminiscence;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidgamedev.com.reminiscence.contact.UserDBManager;
import com.androidgamedev.com.reminiscence.contact.UserDBSqlData;
import com.androidgamedev.com.reminiscence.localprocess.DataInfo;
import com.androidgamedev.com.reminiscence.server.ServerInfo;
import com.androidgamedev.com.reminiscence.server.SettingService;
import com.androidgamedev.com.reminiscence.server.UserInfoStatusDTO;
import com.androidgamedev.com.reminiscence.util.CheckAccount;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePasswordActivity extends Activity
{
    private EditText yourPassword, newPassword, newPasswordCheck;
    private Button savePassword_btn;

    private View mProgressView;
    private View mFormView;

    SettingPasswordTask task = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        yourPassword = (EditText)findViewById(R.id.check_pw);
        newPassword = (EditText)findViewById(R.id.new_password);
        newPasswordCheck = (EditText)findViewById(R.id.new_password_check);
        savePassword_btn = (Button)findViewById(R.id.savePasswordBtn);

        mProgressView = findViewById(R.id.changename_progress);
        mFormView = findViewById(R.id.nameChangeFrame);

        savePassword_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showProgress(true);

                String current_password = yourPassword.getText().toString();
                String new_password = newPassword.getText().toString();
                String new_password_check = newPasswordCheck.getText().toString();

                boolean cancel = false;
                View focusView = null;

                //현재 비밀번호 작성 여부 확인
                if(TextUtils.isEmpty(current_password))
                {
                    yourPassword.setError(getString(R.string.error_field_required));
                    focusView = yourPassword;
                    cancel = true;
                }

                if(TextUtils.isEmpty(new_password) && !isPasswordValid(new_password))
                {
                    newPassword.setError(getString(R.string.error_invalid_password));
                    focusView = newPassword;
                    cancel = true;
                }

                if(!new_password.equals(new_password_check))
                {
                    newPassword.setError(getString(R.string.error_incorrect_password));
                    focusView = newPassword;
                    cancel = true;
                }

                if(cancel)
                {
                    focusView.requestFocus();
                }else
                {
                    //서버에 연락
                    task = new SettingPasswordTask(DataInfo.user_id, current_password, new_password, v);
                    task.execute();
                }
            }
        });
    }

    private boolean isPasswordValid(String password)
    {
        return password.length() > 4;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }else
        {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class SettingPasswordTask extends AsyncTask<Void, Void, String>
    {
        private final String email;
        private final String password;
        private final String newpassword;
        private final View view;

        SettingPasswordTask(String email, String password, String newpassword, View view)
        {
            this.email = email;
            this.password = password;
            this.newpassword = newpassword;
            this.view = view;
        }

        @Override
        protected String doInBackground(Void... params)
        {
            HashMap<String, Object> changePasswordMap = new HashMap<>();
            changePasswordMap.put("email", email);
            changePasswordMap.put("password", password);
            changePasswordMap.put("newpassword", newpassword);

            UserInfoStatusDTO dto = null;

            boolean check_account = CheckAccount.checkAccount(getApplicationContext());
            if(check_account)
            {
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(ServerInfo.server_address)
                        .build();
                SettingService service = retrofit.create(SettingService.class);
                Call<UserInfoStatusDTO> infostatus = service.changePassword(changePasswordMap);
                try
                {
                    dto = infostatus.execute().body();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                Log.v("ChangePasswordActivity", dto.getStatus());
            }

            return dto.getStatus();
        }

        @Override
        protected void onPostExecute(String status)
        {
            if(status.equals("success"))
            {
                Log.v("ChangePasswordActivity", "성공적으로 비밀번호가 바뀌었습니다.");
                Toast.makeText(ChangePasswordActivity.this,"성공적으로 비밀번호가 바뀌었습니다.", Toast.LENGTH_SHORT);
            }else
            {
                Log.v("ChangePasswordActivity", "비밀번호가 틀렸습니다.");
                Toast.makeText(ChangePasswordActivity.this,"잘못된 비밀번호 입니다.", Toast.LENGTH_SHORT);
            }
            task = null;
            showProgress(false);
        }
    }
}
