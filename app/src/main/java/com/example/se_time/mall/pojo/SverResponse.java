package com.example.se_time.mall.pojo;

import java.io.Serializable;

public class SverResponse<T>  implements Serializable {
   // private static final long serialVersionUID = 1L;
    private int status;
    private String msg;
    private T data;

    public SverResponse() {

    }

    public SverResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
