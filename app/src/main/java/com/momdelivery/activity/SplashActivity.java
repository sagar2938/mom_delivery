package com.momdelivery.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.momdelivery.R;
import com.momdelivery.utils.AppUser;
import com.momdelivery.utils.FusedLocation;
import com.momdelivery.utils.LocalRepositories;
import com.momdelivery.utils.Preferences;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

public class SplashActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    public static final int MULTIPLE_PERMISSIONS = 4;
    String[] permissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CAPTURE_VIDEO_OUTPUT,
            Manifest.permission.CAPTURE_SECURE_VIDEO_OUTPUT,
            Manifest.permission.CAMERA};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.splash_activity);

        new FusedLocation(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Getting Location...");


        if (LocalRepositories.getAppUser(this)==null){
            LocalRepositories.saveAppUser(getApplicationContext(),new AppUser());
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final int ACCESS_COARSE_LOCATION = PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            final int ACCESS_FINE_LOCATION = PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            final int WRITE_EXTERNAL_STORAGE = PermissionChecker.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            final int READ_EXTERNAL_STORAGE = PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            final int CALL_PHONE = PermissionChecker.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
            final int CAMERA = PermissionChecker.checkSelfPermission(this, Manifest.permission.CAMERA);
            final int CAPTURE_VIDEO_OUTPUT = PermissionChecker.checkSelfPermission(this, Manifest.permission.CAPTURE_VIDEO_OUTPUT);
            final int CAPTURE_SECURE_VIDEO_OUTPUT = PermissionChecker.checkSelfPermission(this, Manifest.permission.CAPTURE_SECURE_VIDEO_OUTPUT);
            if (ACCESS_COARSE_LOCATION == PermissionChecker.PERMISSION_GRANTED
                    && ACCESS_FINE_LOCATION == PermissionChecker.PERMISSION_GRANTED
                    && WRITE_EXTERNAL_STORAGE == PermissionChecker.PERMISSION_GRANTED
                    && READ_EXTERNAL_STORAGE == PermissionChecker.PERMISSION_GRANTED
                    && CALL_PHONE == PermissionChecker.PERMISSION_GRANTED
                    && CAPTURE_VIDEO_OUTPUT == PermissionChecker.PERMISSION_GRANTED
                    && CAPTURE_SECURE_VIDEO_OUTPUT == PermissionChecker.PERMISSION_GRANTED
                    && CAMERA == PermissionChecker.PERMISSION_GRANTED) {
                launchActivity();
            } else {
                checkPermissions();
            }
        } else {
            launchActivity();
        }


    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(SplashActivity.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);

            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchActivity();
                } else {
                    launchActivity();
                }
                return;
            }
        }
    }



    private void launchActivity() {
        new FusedLocation(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (FusedLocation.location!=null){
                        progressDialog.dismiss();
                        launch();
                    }else {
                        if (!progressDialog.isShowing()){
                            progressDialog.show();
                        }
                        launchActivity();
                    }
                }catch (Exception e){

                }
            }
        }, 100);

    }


    void launch() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

               /* startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();*/

                if(Preferences.getInstance(SplashActivity.this).isLogin()){
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }

            }
        }, 1000);
    }


}
