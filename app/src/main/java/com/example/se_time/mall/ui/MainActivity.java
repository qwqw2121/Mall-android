package com.example.se_time.mall.ui;

import android.icu.util.ULocale;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.se_time.mall.R;
import com.example.se_time.mall.fragment.CartFragment;
import com.example.se_time.mall.fragment.CategoryFragment;
import com.example.se_time.mall.fragment.HomeFragment;
import com.example.se_time.mall.fragment.UserFragment;

public class MainActivity extends AppCompatActivity {

    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonHome;

    private Fragment homeFragment;
    private Fragment categoryFragment;
    private Fragment cartFragment;
    private Fragment userFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();
        bindEvent();
    }

    private void bindEvent() {
        mRadioGroup=(RadioGroup)findViewById(R.id.radio_group_button);
        mRadioButtonHome=(RadioButton)findViewById(R.id.radio_button_home);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                switch (checkedId)
                {
                    case R.id.radio_button_home:
                        ft.show(homeFragment).hide(cartFragment).hide(categoryFragment).hide(userFragment).commit();
                        break;
                    case R.id.radio_button_category:
                        ft.show(categoryFragment).hide(cartFragment).hide(homeFragment).hide(userFragment).commit();
                        break;
                    case R.id.radio_button_cart:
                        ft.show(cartFragment).hide(homeFragment).hide(categoryFragment).hide(userFragment).commit();
                        break;
                    case R.id.radio_button_user:
                        ft.show(userFragment).hide(cartFragment).hide(categoryFragment).hide(homeFragment).commit();
                        break;
                }
            }
        });

        mRadioButtonHome.setChecked(true);
    }

    private void initFragment(){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        homeFragment=new HomeFragment();
        cartFragment=new CartFragment();
        categoryFragment=new CategoryFragment();
        userFragment=new UserFragment();

        ft.add(R.id.container,homeFragment,"home");
        ft.add(R.id.container,cartFragment,"cart");
        ft.add(R.id.container,categoryFragment,"category");
        ft.add(R.id.container,userFragment,"user");

        ft.show(homeFragment).hide(cartFragment).hide(categoryFragment).hide(userFragment).commit();
    }

    //防止碎片同时出现
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        for(int i=0;i<mRadioGroup.getChildCount();i++)
        {
            RadioButton mTab=(RadioButton) mRadioGroup.getChildAt(i);
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            Fragment fragment=getSupportFragmentManager().findFragmentByTag((String)mTab.getTag());
            if(fragment!=null)
            {
                if(!mTab.isChecked())//判断当前选择的是哪个
                {
                    ft.hide(fragment);
                }
            }
            ft.commit();
        }
    }
}
