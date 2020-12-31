package com.momdelivery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.momdelivery.R;
import com.momdelivery.network.ApiCallService;
import com.momdelivery.network.response.login.LoginResponse;
import com.momdelivery.utils.AppUser;
import com.momdelivery.utils.Helper;
import com.momdelivery.utils.LocalRepositories;
import com.momdelivery.utils.Preferences;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {

    EditText mobile;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");
        mobile=findViewById(R.id.mobile);
        submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobile.getText().toString().isEmpty()){
                    getDialog("Enter mobile number");
                    return;
                }
                if (mobile.getText().toString().trim().length()!=10){
                    getDialog("Enter valid mobile number");
                    return;
                }
                Map map=new HashMap();
                map.put("mobile",mobile.getText().toString());
                map.put("otp", Helper.getOtp());
                ApiCallService.action(LoginActivity.this,map,ApiCallService.Action.ACTION_LOG_IN);
            }
        });
    }

    @Subscribe
    public void login(LoginResponse response){
        if (response.getResponse().getConfirmation()==1){
            Preferences.getInstance(getApplicationContext()).setMobile(response.getResponse().getUserData().get(0).getMobile());
            Preferences.getInstance(getApplicationContext()).setOtp(response.getResponse().getOtp());
            AppUser appUser=LocalRepositories.getAppUser(getApplicationContext());
            appUser.setUserDatum(response.getResponse().getUserData().get(0));
            LocalRepositories.saveAppUser(getApplication(),appUser);
            startActivity(new Intent(getApplicationContext(),OtpActivity.class));
        }else {
            getDialog(response.getResponse().getMessage());
        }
    }
}
