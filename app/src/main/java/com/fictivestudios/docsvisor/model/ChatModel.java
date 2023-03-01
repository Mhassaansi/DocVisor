package com.fictivestudios.docsvisor.model;

public class ChatModel {
    String msg;
    String date_received;
    int type;

    public ChatModel(String msg, String date_received, int type) {

        this.msg = msg;
        this.date_received = date_received;
        this.type = type;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate_received() {
        return date_received;
    }

    public void setDate_received(String date_received) {
        this.date_received = date_received;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
