package com.example.swan.activity;

import android.content.Intent;
import android.graphics.Bitmap;
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

import com.alibaba.fastjson.JSON;
import com.example.swan.R;
import com.example.swan.domain.Message;
import com.example.swan.util.CommonUtil;
import com.example.swan.util.HttpUtil;
import com.example.swan.util.QMUITipDialogUtil;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 注册activity
 *
 * @author xu
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText usernameText;
    private EditText passwordText;
    private EditText confirmPasswordText;
    private Button submitButton;
    private Handler handler = new Handler();
    private String captchaToken;
    private ImageView captchaView;
    private EditText captchaText;
    private QMUITipDialog tipDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initWidget();

    }

    private void initWidget() {
        usernameText = findViewById(R.id.register_username);
        passwordText = findViewById(R.id.register_password);
        confirmPasswordText = findViewById(R.id.register_confirm_password);
        captchaText = findViewById(R.id.register_captcha_edit);
        submitButton = findViewById(R.id.register_submit);
        captchaView = findViewById(R.id.register_captcha);
        captchaView.setOnClickListener(captchaListener);
        submitButton.setOnClickListener(submitListener);
    }

    private View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String username = usernameText.getText().toString();
            String password = passwordText.getText().toString();
            String confirmPassword = confirmPasswordText.getText().toString();
            String captcha = captchaText.getText().toString();
      /*      Log.d("username", username);
            Log.d("password", password);
            Log.d("confirmPassword", confirmPassword);*/
            if (CommonUtil.isNullOrEmpty(captchaToken)) {
               tipDialog = QMUITipDialogUtil.getFailTipDialog(RegisterActivity.this, "请先获取验证码");
                handler.post(() -> {
                    tipDialog.show();
                    handler.postDelayed(tipDialog::dismiss, 1000);
                });
                return;
            }

            if (CommonUtil.isNullOrEmpty(captcha)) {
                tipDialog = QMUITipDialogUtil.getFailTipDialog(RegisterActivity.this, "验证码不能为空");
                handler.post(() -> {
                    tipDialog.show();
                    handler.postDelayed(tipDialog::dismiss, 1000);
                });
                return;
            }
            if (CommonUtil.isNullOrEmpty(username)) {
                tipDialog = QMUITipDialogUtil.getFailTipDialog(RegisterActivity.this, "用户名不能为空");
                handler.post(() -> {
                    tipDialog.show();
                    handler.postDelayed(tipDialog::dismiss, 1000);
                });
                return;
            }
            if (CommonUtil.isNullOrEmpty(password)) {
                tipDialog = QMUITipDialogUtil.getFailTipDialog(RegisterActivity.this, "密码不能为空");
                handler.post(() -> {
                    tipDialog.show();
                    handler.postDelayed(tipDialog::dismiss, 1000);
                });
                return;
            }

            if (!confirmPassword.equals(password)) {
                tipDialog = QMUITipDialogUtil.getFailTipDialog(RegisterActivity.this, "两次密码不同");
                handler.post(() -> {
                    tipDialog.show();
                    handler.postDelayed(tipDialog::dismiss, 1000);
                });
                return;
            }
            //Http 请求注册
            QMUITipDialog loadingDialog = QMUITipDialogUtil.getLoadingTipDialog(RegisterActivity.this, "正在注册，请稍后");
            loadingDialog.show();
            Map<String, String> data = new HashMap<>();
            data.put("username", username);
            data.put("password", password);
            data.put("kaptcha_token", captchaToken);
            data.put("kaptcha", captcha);


            HttpUtil.asynPost("/account/register", data, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    loadingDialog.dismiss();
                    tipDialog = QMUITipDialogUtil.getFailTipDialog(RegisterActivity.this, "未知错误，请重试");
                    tipDialog.show();
                    handler.postDelayed(tipDialog::dismiss, 1000);

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String res = null;
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            try {
                                Message message = JSON.parseObject(response.body().string(), Message.class);
                                if (!message.isSuccess()) {
                                    res = message.getMsg();
                                    Log.d("res",res);
                                }
                            } catch (Exception e) {
                                Log.d("onResponse", e.getMessage());
                                res = "注册失败";
                            }
                        }else {
                            res="注册失败";
                        }
                    }else {
                        res="注册失败";
                    }
                    loadingDialog.dismiss();
                    if(res==null){
                        Looper.prepare();
                        tipDialog=QMUITipDialogUtil.getSuccessTipDialog(RegisterActivity.this,"注册成功");
                        tipDialog.show();
                        handler.postDelayed(tipDialog::dismiss, 1000);
                        Looper.loop();
                        Intent intent=new Intent();
                        intent.setClass(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }else {
                        Looper.prepare();
                        tipDialog=QMUITipDialogUtil.getFailTipDialog(RegisterActivity.this,res);
                        tipDialog.show();
                        handler.postDelayed(tipDialog::dismiss, 1000);
                        Looper.loop();

                    }
                }
            });
        }
    };


    private View.OnClickListener captchaListener = v -> {
        //由于HttpUtil.synGetKaptcha(); 为同步 ，为了不阻塞主线程，新建线程
        new Thread(() -> {
            Map<String, String> res = HttpUtil.synGetKaptcha();
            if (res != null) {
                String img = res.get("img");
                String kaptchaToken = res.get("kaptcha_token");
                if (!CommonUtil.isNullOrEmpty(kaptchaToken) && !CommonUtil.isNullOrEmpty(img)) {
                    captchaToken = kaptchaToken;
                    //更新验证码

                 handler.post(() -> captchaView.setImageBitmap(CommonUtil.parseImage(img)));
                }
            }
        }).start();

    };
}
