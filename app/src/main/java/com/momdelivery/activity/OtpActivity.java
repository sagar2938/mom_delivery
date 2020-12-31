package com.momdelivery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.momdelivery.R;
import com.momdelivery.network.ApiCallService;
import com.momdelivery.network.response.TokenResponse;
import com.momdelivery.utils.Preferences;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

public class OtpActivity extends BaseActivity {

    Button submit;
    Pinview pinview;
    TextView mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_otp);
        submit = findViewById(R.id.submit);
        pinview = findViewById(R.id.pinview);
        mobile = findViewById(R.id.mobile);
        mobile.setText(Preferences.getInstance(getApplicationContext()).getMobile());
        getSupportActionBar().setTitle("OTP");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
//        pinview.setValue(Preferences.getInstance(getApplicationContext()).getOtp());



        Map map=new HashMap();
        try {
            map.put("mobile",Preferences.getInstance(getApplicationContext()).getMobile());
            map.put("fcmToken", FirebaseInstanceId.getInstance().getToken());
            ApiCallService.action(OtpActivity.this,map, ApiCallService.Action.ACTION_SEND_TOKEN);
        }catch (IllegalStateException e){
            e.printStackTrace();
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinview.getValue().isEmpty()) {
                    getDialog("Enter Otp");
                    return;
                }
                if (pinview.getValue().length() != 4) {
                    getDialog("Enter Complete Otp");
                    return;
                }
                if (!pinview.getValue().equals(Preferences.getInstance(getApplicationContext()).getOtp())) {
                    getDialog("Enter Valid Otp");
                    return;
                }
                Preferences.getInstance(getApplicationContext()).setLogin(true);
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }



    @Subscribe
    public void tokenResponse(TokenResponse response){
        if (response.getResponse().getConfirmation()==1) {
//            Toast.makeText(this, ""+response.getResponse().getMessage(), Toast.LENGTH_SHORT).show();
        }else {
            getDialog(response.getResponse().getMessage());
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
