package com.momdelivery.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.momdelivery.R;
import com.momdelivery.utils.AppUser;
import com.momdelivery.utils.LocalRepositories;
import com.momdelivery.utils.Preferences;

public class ProfileActivity extends AppCompatActivity {

    RelativeLayout logout;
    TextView aadharNo;
    TextView name;
    TextView companyName;
    TextView mobile;
    TextView companyId;
    TextView driveringLicense;
    TextView address;
    TextView vehicleNo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");
        logout=findViewById(R.id.logout);
        aadharNo=findViewById(R.id.aadharNo);
        name=findViewById(R.id.name);
        companyName=findViewById(R.id.companyName);
        mobile=findViewById(R.id.mobile);
        driveringLicense=findViewById(R.id.driveringLicense);
        address=findViewById(R.id.address);
        vehicleNo=findViewById(R.id.vehicleNo);

        AppUser appUser= LocalRepositories.getAppUser(this);

        aadharNo.setText(appUser.getUserDatum().getAadharNo());
        name.setText(appUser.getUserDatum().getName());
        companyName.setText(appUser.getUserDatum().getCompanyName());
        mobile.setText(appUser.getUserDatum().getMobile());
        driveringLicense.setText(appUser.getUserDatum().getDriveringLicense());
        address.setText(appUser.getUserDatum().getAddress());
        vehicleNo.setText(appUser.getUserDatum().getVehicleNo());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Preferences.getInstance(getApplicationContext()).setLogin(false);
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
