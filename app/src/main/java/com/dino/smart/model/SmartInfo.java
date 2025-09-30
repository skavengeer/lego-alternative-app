package com.dino.smart.model;

import java.io.Serializable;

public class SmartInfo implements Serializable {
    String msg;
    String sid;
    String smartName;

    public String getSid() {
        return this.sid;
    }

    public void setSid(String str) {
        this.sid = str;
    }

    public String getSmartName() {
        return this.smartName;
    }

    public void setSmartName(String str) {
        this.smartName = str;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String str) {
        this.msg = str;
    }
}
