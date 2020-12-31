package com.momdelivery.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.momdelivery.R;
import com.momdelivery.network.ApiCallService;
import com.momdelivery.utils.CustomUploadProgressDialog;
import com.momdelivery.utils.Helper;
import com.momdelivery.utils.UploadEvent;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class BaseActivity extends AppCompatActivity {

    public boolean isKeyPadOpen;
    @Override
    protected void onResume() {
        super.onResume();

        try {
            EventBus.getDefault().register(this);

            KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
                @Override
                public void onVisibilityChanged(boolean isOpen) {
                    isKeyPadOpen = isOpen;
                }
            });
        }catch (Exception e){
        }

        locationEnabled();
    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            EventBus.getDefault().unregister(this);
        }catch (Exception e){
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            EventBus.getDefault().unregister(this);
        }catch (Exception e){
        }
    }

    @Subscribe
    public void timeOut(String msg){
        getDialog("Failed",msg);
    }

    public void getDialog(String tittle, String message) {
        new AlertDialog.Builder(this)
                .setTitle(tittle)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
//                .setNegativeButton("Cancel", null)
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }


    public void getDialog( String message) {
        new AlertDialog.Builder(this)
                .setTitle("Sorry")
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
//                .setNegativeButton("Cancel", null)
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }

    public void getDialogSuccess( String message) {
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
//                .setNegativeButton("Cancel", null)
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }


    public void getDialogSorry() {
        new AlertDialog.Builder(this)
                .setTitle("Sorry")
                .setMessage("Something went wrong")
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
//                .setNegativeButton("Cancel", null)
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }






    public void uploadFile(String imageName, Uri uri) {
        CustomUploadProgressDialog.getInstance(this).show();
        StorageReference storage = FirebaseStorage.getInstance().getReference().child(ApiCallService.Action.DOCUMENT).child(imageName);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(ApiCallService.Action.DOCUMENT);
        storage.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                EventBus.getDefault().post(new UploadEvent(uri.toString()));
                                uri.toString();
                                CustomUploadProgressDialog.setDismiss();
                            }
                        });
                    }


                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                        CustomUploadProgressDialog.setDismiss();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        CustomUploadProgressDialog.setProgress("Uploading... " + String.format("%.0f", progress) + " %");
                        if (progress == 100) {
                            CustomUploadProgressDialog.setProgress("Uploaded");
                        }
                    }
                });
    }


    public void fragmentSwitching(Fragment fragment) {
       /* if (isKeyPadOpen) {
            Helper.closeKeyPad(BaseActivity.this, isKeyPadOpen);
        }
        FragmentManager fm = BaseActivity.this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment).commit();*/
    }


    private void locationEnabled () {
        LocationManager lm = (LocationManager)
                getSystemService(Context. LOCATION_SERVICE ) ;
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager. GPS_PROVIDER ) ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager. NETWORK_PROVIDER ) ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(BaseActivity.this )
                    .setTitle("GPS Status")
                    .setMessage( "Please Enable GPS" )
                    .setPositiveButton( "TURN ON" , new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick (DialogInterface paramDialogInterface , int paramInt) {
                                    startActivity( new Intent(Settings. ACTION_LOCATION_SOURCE_SETTINGS )) ;
                                }
                            })
                    .setNegativeButton( "Cancel" , null )
                    .show() ;
        }
    }

}
