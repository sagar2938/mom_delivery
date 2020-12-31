package com.momdelivery.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.momdelivery.R;
import com.momdelivery.fragment.NewLeadFragment;
import com.momdelivery.fragment.OnGoingFragment;
import com.momdelivery.network.ApiCallService;
import com.momdelivery.network.ThisApp;
import com.momdelivery.network.response.GetStatusResponse;
import com.momdelivery.network.response.TokenResponse;
import com.momdelivery.network.response.success.SuccessResponse;
import com.momdelivery.utils.Preferences;
import com.suke.widget.SwitchButton;

import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    LinearLayoutManager mLayoutManager;
    int onlineStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setSaveFromParentEnabled(false);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(4);
        FragmentManager fragmentManager = getSupportFragmentManager();
        System.out.println("check back stack " + fragmentManager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        if (getIntent().getStringExtra("firebase")!=null){
            loadIncomingOrderUi();
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        Intent intent = new Intent(MainActivity.this, GPSService.class);
        startService(intent);

        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Map map=new HashMap();
                map.put("mobile",Preferences.getInstance(getApplicationContext()).getMobile());
                map.put("fcmToken",newToken);
                map.put("userType","Delivery");
                ThisApp.getApi(getApplicationContext()).sendToken(map).enqueue(new Callback<TokenResponse>() {
                    @Override
                    public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
//                        Toast.makeText(MainActivity.this, "token saved", Toast.LENGTH_SHORT).show();
                        System.out.println("https url: "+"https://mom-apicalls.appspot.com/delivery/partner/addtoken/");
                        System.out.println("https req: "+map);
//                        System.out.println("https res: "+response.body().getResponse().getMessage());
                    }

                    @Override
                    public void onFailure(Call<TokenResponse> call, Throwable t) {

                    }
                });
//                ApiCallService.action(MainActivity.this,map,ApiCallService.Action.ACTION_SAVE_TOKEN);
            }
        });

    }


    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {

            switch (position) {

                case 0:
                    return new NewLeadFragment();
                case 1:
                    return new OnGoingFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

    }


    MenuItem item;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
        item = menu.findItem(R.id.button_item);
        MenuItem profile_item = menu.findItem(R.id.profile_item);

        final SwitchButton switchButton = item.getActionView().findViewById(R.id.switch_button);
        final LinearLayout profile = profile_item.getActionView().findViewById(R.id.profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });
        switchButton.setChecked(true);
        switchButton.isChecked();
        switchButton.toggle();     //switch state
        switchButton.toggle(false);//switch without animation
        switchButton.setShadowEffect(true);//disable shadow effect
        switchButton.setEnabled(true);//disable button
        switchButton.setEnableEffect(false);//disable the switch animation
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Calendar c = Calendar.getInstance();
                String formattedDate = df.format(c.getTime());
                Log.d("Chechecked", String.valueOf(isChecked));
                Map map = new Hashtable();
                map.put("mobile", Preferences.getInstance(getApplicationContext()).getMobile());
                map.put("timeStamp", formattedDate);
                map.put("status", isChecked);
                ApiCallService.action(MainActivity.this, map, ApiCallService.Action.ACTION_UPDATE_STATUS);

            }
        });
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.button_item) {
            ApiCallService.action(MainActivity.this, null, ApiCallService.Action.ACTION_UPDATE_STATUS);
            return false;
        }
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Map map = new HashMap();
        map.put("mobile", Preferences.getInstance(getApplicationContext()).getMobile());
        ApiCallService.action2(MainActivity.this, map, ApiCallService.Action.ACTION_GET_STATUS);
    }


    @Subscribe
    public void getStatus(GetStatusResponse response) {
        onlineStatus = response.getResponse().getConfirmation();
        final SwitchButton switchButton = item.getActionView().findViewById(R.id.switch_button);
        if (onlineStatus == 1) {
            switchButton.setChecked(true);
        } else {
            switchButton.setChecked(false);
        }

    }


    /*@Subscribe
    public void AcceptCancel(SuccessResponse response){
        if (response.getResponse().getConfirmation()==1) {
            getDialogSuccess(*//*response.getResponse().getOrderId()+*//*" "+response.getResponse().getMessage());

        }else {
            getDialogSorry();
        }
    }*/


    private void loadIncomingOrderUi() {
        final MediaPlayer mediaPlayer;
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.carnatic);
        mediaPlayer.start();
        new AlertDialog.Builder(this)
                .setTitle("New Order")
                .setMessage("You received a new order")
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mediaPlayer.stop();
                    }
                })
//                .setNegativeButton("Cancel", null)
                .setIcon(R.mipmap.ic_launcher)
                .show();


    }

}
