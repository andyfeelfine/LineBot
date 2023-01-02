package com.example.linebot.model.respone;

public class CommonRes<T> {

    String resCode;
    String resMessage;
    T data;

    public CommonRes() {
    }

    public CommonRes SucceesCommonRes(T data) {
        resCode = "000";
        resMessage = "Success";
        this.data = data;
        return this;
    }

    public CommonRes ErrorCommonRes() {
        resCode = "999";
        resMessage = "Failed";
        return this;
    }

    public CommonRes(String resCode) {
        this.resCode = resCode;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMessage() {
        return resMessage;
    }

    public void setResMessage(String resMessage) {
        this.resMessage = resMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
