package com.example.se_time.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

import okhttp3.Call;

public class AddAddressActivity extends AppCompatActivity {

    private EditText name;
    private EditText mobile;
    private TextView province;
    private TextView city;
    private TextView district;
    private EditText addr_detail;
    private EditText zip;


    private Toolbar toolbar;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address_form);

        initView();
        //loadAddrLists();
    }

    private void initView() {

        name=(EditText)findViewById(R.id.name);
        mobile=(EditText)findViewById(R.id.mobile);
        province=(TextView)findViewById(R.id.province);
       // province.ble
        city=(TextView)findViewById(R.id.city);
        district=(TextView)findViewById(R.id.district);
        addr_detail=(EditText)findViewById(R.id.addr_detail);
        zip=(EditText)findViewById(R.id.zip);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("新增地址");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btn_add_addr_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(VerifyForm())
                {
                    Intent intent=new Intent();
                    setResult(RESULT_CANCELED,intent);
                    finish();
                }
            }
        });
        findViewById(R.id.btn_add_addr_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAddress();
            }
        });
    }

    private void addAddress() {
        OkHttpUtils.post()
                .url(Constant.API.USER_ADDR_ADD_URL)
                .addParams("name",name.getText().toString())
                .addParams("mobile",mobile.getText().toString())
                .addParams("province",province.getText().toString())
                .addParams("city",city.getText().toString())
                .addParams("district",district.getText().toString())
                .addParams("addr",addr_detail.getText().toString())
                .addParams("zip",zip.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Type type=new TypeToken<SverResponse>(){}.getType();
                        SverResponse result=JSONUtils.fromJson(response,type);
                        if(result.getStatus()== ResponseCode.SUCCESS.getCode())
                        {
                            //回到选择界面
                            Intent intent=new Intent();
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                        else {
                            Toast.makeText(AddAddressActivity.this,result.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean VerifyForm() {
        if(name.getText().toString()!=null&&
                mobile.getText().toString()!=null&&
                province.getText().toString()!=null&&
                city.getText().toString()!=null&&
                district.getText().toString()!=null&&
                addr_detail.getText().toString()!=null)
        {
            return true;
        }
        return false;
    }


}
