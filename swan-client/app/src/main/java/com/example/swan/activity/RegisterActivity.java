package com.example.swan.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.swan.R;
import com.example.swan.util.CommonUtil;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

/**
 * 注册activity
 * @author xu
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText usernameText;
    private EditText passwordText;
    private EditText confirmPasswordText;
    private Button submitButton;
    private Handler handler=new Handler();
    private String captchaToken;
    private ImageView captchaView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initWidget();

    }

    private void initWidget(){
        usernameText=findViewById(R.id.register_username);
        passwordText=findViewById(R.id.register_password);
        confirmPasswordText=findViewById(R.id.register_confirm_password);
        submitButton=findViewById(R.id.register_submit);
        captchaView=findViewById(R.id.register_captcha);
        submitButton.setOnClickListener(submitListener);
    }
    private View.OnClickListener submitListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String username=usernameText.getText().toString();
            String password=passwordText.getText().toString();
            String confirmPassword=confirmPasswordText.getText().toString();
            Log.d("username",username);
            Log.d("password",password);
            Log.d("confirmPassword",confirmPassword);
            if(CommonUtil.isNullOrEmpty(username)){
                QMUITipDialog tipDialog=   CommonUtil.getFailTipDialog(RegisterActivity.this,"用户名不能为空");
                handler.post(() -> {
                    tipDialog.show();
                    handler.postDelayed(tipDialog::dismiss, 1000);
                });
                return;
            }
            if (CommonUtil.isNullOrEmpty(password)){
                QMUITipDialog tipDialog=   CommonUtil.getFailTipDialog(RegisterActivity.this,"密码不能为空");
                handler.post(() -> {
                    tipDialog.show();
                    handler.postDelayed(tipDialog::dismiss, 1000);
                });
                return;
            }

            if (!confirmPassword.equals(password)){
                QMUITipDialog tipDialog=   CommonUtil.getFailTipDialog(RegisterActivity.this,"两次密码不同");
                handler.post(() -> {
                    tipDialog.show();
                    handler.postDelayed(tipDialog::dismiss, 1000);
                });
                return;
            }

            //Http 请求注册

        }
    };

}
