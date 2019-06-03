package com.example.swan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.swan.R;
import com.example.swan.util.CommonUtil;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

/**
 * 登录activity
 * @author feng
 */
public class LoginActivity extends AppCompatActivity {


    private TextView registerTextView;
    private EditText usernameText;
    private EditText passwordText;
    private Button submitButton;
    private Handler handler=new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initWidget();

    }
    private void initWidget(){
        registerTextView= findViewById(R.id.login_tv_reg);
        registerTextView.setOnClickListener(registerListener);
        usernameText=findViewById(R.id.login_username);
        passwordText=findViewById(R.id.login_password);
        submitButton=findViewById(R.id.login_submit);
        submitButton.setOnClickListener(submitListener);
    }


    private View.OnClickListener registerListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            intent.setClass(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener submitListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String username=usernameText.getText().toString();
            String password=passwordText.getText().toString();
            if(CommonUtil.isNullOrEmpty(username)){
                QMUITipDialog tipDialog=   CommonUtil.getFailTipDialog(LoginActivity.this,"用户名不能为空");
                handler.post(() -> {
                    tipDialog.show();
                    handler.postDelayed(tipDialog::dismiss, 1000);
                });
                return;
            }
            if (CommonUtil.isNullOrEmpty(password)){
                QMUITipDialog tipDialog=   CommonUtil.getFailTipDialog(LoginActivity.this,"密码不能为空");
                handler.post(() -> {
                    tipDialog.show();
                    handler.postDelayed(tipDialog::dismiss, 1000);
                });
                return;
            }

            
        }
    };
}
