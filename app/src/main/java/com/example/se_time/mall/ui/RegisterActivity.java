package com.example.se_time.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.se_time.mall.R;
import com.example.se_time.mall.config.Constant;
import com.example.se_time.mall.pojo.ResponseCode;
import com.example.se_time.mall.pojo.SverResponse;
import com.example.se_time.mall.utils.JSONUtils;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;

public class RegisterActivity extends AppCompatActivity {

    private EditText account;
    private EditText psw1;
    private EditText psw2;
    private EditText email;
    private EditText phone;
    private EditText question;
    private EditText asw;
    private Button register;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_from);

        initView();

    }

    private void submitForm() {

        if(verify())
        {
            OkHttpUtils.post()
                    .url(Constant.API.USER_REGISTER_URL)
                    .addParams("account",account.getText().toString())
                    .addParams("password",psw1.getText().toString())
                    .addParams("email",email.getText().toString())
                    .addParams("phone",phone.getText().toString())
                    .addParams("question",question.getText().toString())
                    .addParams("asw",asw.getText().toString())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Type type=new TypeToken<SverResponse>(){}.getType();
                            SverResponse result=JSONUtils.fromJson(response,type);
                            if(result.getStatus()==ResponseCode.SUCCESS.getCode())
                            {
                                Toast.makeText(RegisterActivity.this,result.getMsg(),Toast.LENGTH_SHORT).show();
//                                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
//                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(RegisterActivity.this,result.getMsg(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private boolean verify() {

        if(account.getText().toString().isEmpty()||psw1.getText().toString().isEmpty()||psw2.getText().toString().isEmpty()||email.getText().toString().isEmpty())
        {
            // System.out.println("info2:"+user+psw1+psw2+email+code);
            Toast.makeText(RegisterActivity.this,"请先填写完成注册信息",Toast.LENGTH_SHORT).show();
            return false;
        }

       if(!verifyName(account.getText().toString()))
       {
           Toast.makeText(RegisterActivity.this,"用户名格式，以字母开头，长度在5~18之间，只能包含字符、数字和下划线",Toast.LENGTH_SHORT).show();
           return false;
       }
        if(!verifyPsw(psw1.getText().toString()))
        {
            Toast.makeText(RegisterActivity.this,"密码不能含有非法字符，长度在4-10之间",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!psw1.getText().toString().equals(psw2.getText().toString()))
        {
            Toast.makeText(RegisterActivity.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!verifyEmail(email.getText().toString()))
        {
            Toast.makeText(RegisterActivity.this,"请检查邮箱格式",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void initView() {

        account=(EditText)findViewById(R.id.account);
        psw1=(EditText)findViewById(R.id.psw_1);
        psw2=(EditText)findViewById(R.id.psw_2);
        email=(EditText)findViewById(R.id.email);
        phone=(EditText)findViewById(R.id.phone);
        question=(EditText)findViewById(R.id.question);
        asw=(EditText)findViewById(R.id.asw);
        register=(Button)findViewById(R.id.register);

        if(register==null){
            System.out.println("null");
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

    }

    private boolean verifyEmail(String email) {
        Pattern pattern = Pattern.compile("^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}$");
        Matcher matcher= pattern.matcher(email);
        System.out.println("email:"+matcher.matches());
        return matcher.matches();
    }
    private boolean verifyPsw(String psw1) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{4,10}$");
        Matcher matcher= pattern.matcher(psw1);
        System.out.println("psw:"+matcher.matches());
        return matcher.matches();
    }

    private boolean verifyName(String user) {

        Pattern pattern = Pattern.compile("^[a-zA-Z]\\w{4,17}$");
        Matcher matcher= pattern.matcher(user);
        System.out.println("user:"+matcher.matches());
        return matcher.matches();
    }
}
