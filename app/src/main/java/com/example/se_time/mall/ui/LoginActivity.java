package com.example.se_time.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.se_time.mall.R;
import com.example.se_time.mall.config.Constant;
import com.example.se_time.mall.pojo.SverResponse;
import com.example.se_time.mall.pojo.User;
import com.example.se_time.mall.utils.JSONUtils;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.example.se_time.mall.pojo.ResponseCode;

import java.lang.reflect.Type;

import okhttp3.Call;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText accountEdit;
    private EditText passwordEdit;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        accountEdit=(EditText)findViewById(R.id.account);
        passwordEdit=(EditText)findViewById(R.id.password);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                //调用注册注册
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
              //  finish();
                break;
        }
    }
    private void login(){
        String account = accountEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        if(TextUtils.isEmpty(account)){
            Toast.makeText(this,"请输入登录账号！",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"请输入登录密码！",Toast.LENGTH_LONG).show();
            return;
        }
        OkHttpUtils.post()
                .url(Constant.API.USER_LOGIN_URL)
                .addParams("account",account)
                .addParams("password",password)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(LoginActivity.this,"网络出现问题！",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Type type = new TypeToken<SverResponse<User>>(){}.getType();
                        SverResponse<User> result = JSONUtils.fromJson(response,type);
                        if(result.getStatus()== ResponseCode.SUCCESS.getCode()){
                            //发送本地广播
                            Intent intent = new Intent(Constant.ACTION.LOAD_CART_ACTION);
                            LocalBroadcastManager.getInstance(LoginActivity.this).sendBroadcast(intent);
                            LoginActivity.this.finish();
                        }else{
                            Toast.makeText(LoginActivity.this,result.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
