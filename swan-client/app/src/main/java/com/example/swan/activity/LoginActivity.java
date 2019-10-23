package com.example.swan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.swan.MainActivity;
import com.example.swan.R;
import com.example.swan.domain.Message;
import com.example.swan.util.AccountHelper;
import com.example.swan.util.CommonUtil;
import com.example.swan.util.HttpUtil;
import com.example.swan.util.QMUITipDialogUtil;
import com.example.swan.util.ToastUtil;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 登录activity
 *
 * @author feng
 */
public class LoginActivity extends AppCompatActivity {


    private EditText usernameText;
    private EditText passwordText;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();
        initWidget();

    }

    private void initWidget() {
        TextView registerTextView = findViewById(R.id.login_tv_reg);
        registerTextView.setOnClickListener(registerListener);
        usernameText = findViewById(R.id.login_username);
        passwordText = findViewById(R.id.login_password);
        ImageView back = findViewById(R.id.login_back);
        back.setOnClickListener(v -> finish());
        Button submitButton = findViewById(R.id.login_submit);
        submitButton.setOnClickListener(submitListener);
    }


    private View.OnClickListener registerListener = v -> {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    };

    private View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String username = usernameText.getText().toString();
            String password = passwordText.getText().toString();
            Log.d("username", username);
            Log.d("password", password);
            if (CommonUtil.isNullOrEmpty(username)) {
                QMUITipDialog tipDialog = QMUITipDialogUtil.getFailTipDialog(LoginActivity.this, "用户名不能为空");
                handler.post(() -> {
                    tipDialog.show();
                    handler.postDelayed(tipDialog::dismiss, 1000);
                });
                return;
            }
            if (CommonUtil.isNullOrEmpty(password)) {
                QMUITipDialog tipDialog = QMUITipDialogUtil.getFailTipDialog(LoginActivity.this, "密码不能为空");
                handler.post(() -> {
                    tipDialog.show();
                    handler.postDelayed(tipDialog::dismiss, 1000);
                });
                return;
            }
            Map<String, String> data = new HashMap<>();
            data.put("username", username);
            data.put("password", password);
            //Http请求
            HttpUtil.asynPost("/account/login", data, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    QMUITipDialogUtil.getFailTipDialog(LoginActivity.this, "登录失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String errInfo = null;
                    if (response.isSuccessful() && response.body() != null) {
                        try {

                            Message message = JSON.parseObject(response.body().string(), Message.class);
                            if (message.isSuccess()) {
                                AccountHelper.writeAuthToken(LoginActivity.this, message.getParams().get("auth_token"));
                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this, MenuActivity.class);
                                startActivity(intent);
                                finish();
                                return;
                            } else {
                                errInfo = message.getMsg();
                            }
                        } catch (Exception e) {
                            Log.d("request login", e.getMessage());
                        }
                    }
                    if (errInfo == null) {
                        errInfo = "登录失败";
                    }
                    Looper.prepare();
                    ToastUtil.showError(LoginActivity.this, errInfo);
                    Looper.loop();


                }
            });


        }
    };
}
