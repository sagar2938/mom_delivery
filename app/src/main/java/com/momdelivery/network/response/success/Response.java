
package com.momdelivery.network.response.success;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.momdelivery.network.response.login.UserDatum;

import java.util.List;

public class Response {

    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("confirmation")
    @Expose
    private Integer confirmation;
    @SerializedName("user_data")
    @Expose
    private List<UserDatum> userData = null;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(Integer confirmation) {
        this.confirmation = confirmation;
    }

    public List<UserDatum> getUserData() {
        return userData;
    }

    public void setUserData(List<UserDatum> userData) {
        this.userData = userData;
    }

}
