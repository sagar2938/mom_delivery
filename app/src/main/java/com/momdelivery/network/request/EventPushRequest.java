package com.momdelivery.network.request;

import android.content.Context;

import com.momdelivery.utils.Preferences;


public class EventPushRequest {
    String to;
    String senderType;
    String message;
    String title;



    public EventPushRequest(String to, String message){
        this.to=to;
        this.message=message;
        senderType="Vendor";
        title="MOM";
    }


    public EventPushRequest(String senderType, String to, String message){
        this.to=to;
        this.message=message;
        this.senderType=senderType;
        title="MOM";
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSenderType() {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
