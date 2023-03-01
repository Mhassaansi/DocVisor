package com.fictivestudios.docsvisor.model;


import com.google.gson.annotations.SerializedName;

public class ChatItem {

    String message;
    String sender_id;
    String reciever_id;
    String type;
    String created_at;
    String updated_at;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public ChatItem(String message, String sender_id, String reciever_id, String type) {
        this.message = message;
        this.sender_id = sender_id;
        this.reciever_id = reciever_id;
        this.type = type;
    }

    public ChatItem(String sender_id, String reciever_id) {
        this.sender_id = sender_id;
        this.reciever_id = reciever_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReciever_id() {
        return reciever_id;
    }

    public void setReciever_id(String reciever_id) {
        this.reciever_id = reciever_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
