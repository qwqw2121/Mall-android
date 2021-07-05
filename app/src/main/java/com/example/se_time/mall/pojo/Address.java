package com.example.se_time.mall.pojo;

import android.view.View;

import com.example.se_time.mall.listener.OnItemClickListener;

import java.io.Serializable;

public class Address implements Serializable {

    private Integer id;
    private Integer uid;
    private String name;
    private String phone;
    private String mobile;
    private String province="";
    private String city="";
    private String district="";
    private String addr="";
    private String zip;

    public int getDefaultAddr() {
        return defaultAddr;
    }

    public void setDefaultAddr(int defaultAddr) {
        this.defaultAddr = defaultAddr;
    }

    private int defaultAddr;


    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public String getAddrDetail()
    {
        System.out.println(getProvince()+" "+getCity()+" "+getDistrict()+" "+getAddr());
        return getProvince()+" "+getCity()+" "+getDistrict()+" "+getAddr();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

}
