package com.momdelivery.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by pc on 10/7/2016.
 */
public class Preferences {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    private static Preferences instance;

    private static final String BASE_URL = "base_url";
    private static final String login = "login";
    private static final String token = "token";
    private static final String OTP = "otp";
    private final String name = "name";
    private final String mobile = "mobile";
    private final String api_key = "api_key";
    private final String email = "email";
    private final String address = "address";
    private final String latitude = "latitude";
    private final String longitude = "longitude";
    private final String addressStatus = "addressStatus";
    private final String mode = "mode";
    private final String profileImage = "profileImage";

    private Preferences(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences("My_pref", 0);
        editor = pref.edit();
    }


    public static Preferences getInstance(Context context) {
        if (instance == null) {
            instance = new Preferences(context);
        }
        return instance;
    }


    public void setToken(String cname) {
        editor.putString(token, cname);
        editor.commit();
    }

    public String getToken() {
        return pref.getString(token, "");
    }


    public void setLogin(Boolean cname) {
        editor.putBoolean(login, cname);
        editor.commit();
    }

    public boolean isLogin() {
        return pref.getBoolean(login, false);
    }


    public void setOtp(String cname) {
        editor.putString(OTP, cname);
        editor.commit();
    }

    public String getOtp() {
        return pref.getString(OTP, "");
    }


    public void setMobile(String cname) {
        editor.putString(mobile, cname);
        editor.commit();
    }

    public String getMobile() {
        return pref.getString(mobile, "");
    }


 public void setApiKey(String cname) {
        editor.putString(api_key, cname);
        editor.commit();
    }

    public String getApiKey() {
        return pref.getString(api_key, "");
    }




 public void setEmail(String cname) {
        editor.putString(email, cname);
        editor.commit();
    }

    public String getEmail() {
        return pref.getString(email, "");
    }





 public void setName(String cname) {
        editor.putString(name, cname);
        editor.commit();
    }

    public String getName() {
        return pref.getString(name, "");
    }




 public void setAddress(String cname) {
        editor.putString(address, cname);
        editor.commit();
    }

    public String getAddress() {
        return pref.getString(address, "");
    }




 public void setLatitude(String cname) {
        editor.putString(latitude, cname);
        editor.commit();
    }

    public String getLatitude() {
        return pref.getString(latitude, "");
    }




 public void setLongitude(String cname) {
        editor.putString(longitude, cname);
        editor.commit();
    }

    public String getLongitude() {
        return pref.getString(longitude, "");
    }


 public void setAddressStatus(Integer cname) {
        editor.putInt(addressStatus, cname);
        editor.commit();
    }

    public Integer getAddressStatus() {
        return pref.getInt(addressStatus, 0);
    }


 public void setMode(String cname) {
        editor.putString(mode, cname);
        editor.commit();
    }

    public String getMode() {
        return pref.getString(mode, "Cash On Delivery");
    }




 public void setProfileImage(String cname) {
        editor.putString(profileImage, cname);
        editor.commit();
    }

    public String getProfileImage() {
        return pref.getString(profileImage, "");
    }




}