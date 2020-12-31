package com.momdelivery.network;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.IntentService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.momdelivery.network.response.GetStatusResponse;
import com.momdelivery.network.response.TokenResponse;
import com.momdelivery.network.response.login.LoginResponse;
import com.momdelivery.network.response.success.SuccessResponse;
import com.momdelivery.network.response.on_going.GetNewOrderResponse;
import com.momdelivery.network.response.on_going.GetOnGoingResponse;
import com.momdelivery.utils.Helper;
import com.momdelivery.R;
import com.momdelivery.utils.CustomProgressDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCallService extends IntentService {

    static Object request;
    static Activity context;


    public ApiCallService() {
        super("ApiCallService");
    }


    public static void action(Activity ctx, String action) {
        Intent intent = new Intent(ctx, ApiCallService.class);
        intent.setAction(action);
        ctx.startService(intent);
        context = ctx;
    }

    public static void action(Context ctx, Object request, String action) {

        ApiCallService.request = request;
        Intent intent = new Intent(ctx, ApiCallService.class);
        intent.setAction(action);
        ctx.startService(intent);
    }


    public static void action(Activity ctx, Object request, String action) {
        context = ctx;
        if (!Helper.isNetworkAvailable(context)){
            getDialog(context,"No Internet","Please check your internet connection!!!",request,action);
            return;
        }
        CustomProgressDialog.getInstance(ctx).show();
        ApiCallService.request = request;
        Intent intent = new Intent(ctx, ApiCallService.class);
        intent.setAction(action);
        ctx.startService(intent);
    }


    public static void action2(Activity ctx, Object request, String action) {
        context = ctx;
        if (!Helper.isNetworkAvailable(context)){
            getDialog(context,"No Internet","Please check your internet connection!!!",request,action);
            return;
        }
        ApiCallService.request = request;
        Intent intent = new Intent(ctx, ApiCallService.class);
        intent.setAction(action);
        ctx.startService(intent);
    }

    public static void action2(Activity ctx, Object request, String action,CustomProgressDialog dialog) {
        context = ctx;
        if (!Helper.isNetworkAvailable(context)){
            getDialog(context,"No Internet","Please check your internet connection!!!",request,action);
            return;
        }
        dialog.show();
        ApiCallService.request = request;
        Intent intent = new Intent(ctx, ApiCallService.class);
        intent.setAction(action);
        ctx.startService(intent);
    }


    class Local<T> implements Callback<T> {

        public void onResponse(Call<T> call, Response<T> response) {
            CustomProgressDialog.setDismiss();
            if (response.code() == 200) {
                T body = response.body();
                EventBus.getDefault().post(body);
            } else {
//                getDialog("Some thing went wrong!!! " + response.code());
                EventBus.getDefault().post("Some thing went wrong!!! " + response.code());
            }

        }

        public void onFailure(Call<T> call, Throwable t) {
            CustomProgressDialog.setDismiss();
            EventBus.getDefault().post(t.getMessage());
        }
    }






    public  class Local2<T> implements Callback<T> {

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if (response.code() == 200) {
                T body = response.body();
                EventBus.getDefault().post(body);
            } else {
                EventBus.getDefault().post(ApiCallService.Action.ERROR + " " + response.code());
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            EventBus.getDefault().post(t.getMessage());
        }
    }


    public interface Action{
        String ACTION_LOG_IN="ACTION_LOG_IN";
        String ACTION_RESEND_OTP ="ACTION_RESEND_OTP";
        String ERROR = "Some thing went wrong";
        String DOCUMENT = "documents";

        String ACTION_GET_NEW_LEAD = "ACTION_GET_NEW_LEAD";
        String ACTION_GET_ON_GOING= "ACTION_GET_ON_GOING";
        String ACTION_ACCEPT_ORDER = "ACTION_ACCEPT_ORDER";
        String ACTION_CANCEL_ORDER = "ACTION_CANCEL_ORDER";
        String ACTION_SEND_TOKEN = "ACTION_SEND_TOKEN";
        String ACTION_COMPLETE_ORDER = "ACTION_COMPLETE_ORDER";
        String ACTION_ADD_LIVE_LOCATION = "ACTION_ADD_LIVE_LOCATION";
        String ACTION_UPDATE_STATUS = "ACTION_UPDATE_STATUS";
        String ACTION_LIVE_LOCATION = "ACTION_LIVE_LOCATION";
        String ACTION_SAVE_TOKEN = "ACTION_SAVE_TOKEN";
        String ACTION_GET_STATUS = "ACTION_GET_STATUS";
    }

    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        Api api = ThisApp.getApi(this.getApplicationContext());
        if (action.equals(Action.ACTION_LOG_IN)) {
            api.login((Map) request).enqueue(new Local<LoginResponse>());
        }if (action.equals(Action.ACTION_GET_NEW_LEAD)) {
            api.getNewLeadOrder((Map) request).enqueue(new Local<GetNewOrderResponse>());
        }if (action.equals(Action.ACTION_ACCEPT_ORDER)) {
            api.acceptOrder((Map) request).enqueue(new Local<SuccessResponse>());
        }if (action.equals(Action.ACTION_CANCEL_ORDER)) {
            api.cancelOrder((Map) request).enqueue(new Local<SuccessResponse>());
        }if (action.equals(Action.ACTION_GET_ON_GOING)) {
            api.onGoingOrder((Map) request).enqueue(new Local<GetOnGoingResponse>());
        }if (action.equals(Action.ACTION_SEND_TOKEN)) {
            api.sendToken((Map) request).enqueue(new Local<TokenResponse>());
        }if (action.equals(Action.ACTION_COMPLETE_ORDER)) {
            api.completeOrder((Map) request).enqueue(new Local<SuccessResponse>());
        }if (action.equals(Action.ACTION_ADD_LIVE_LOCATION)) {
            api.addLiveLocation((Map) request).enqueue(new Local<SuccessResponse>());
        }if (action.equals(Action.ACTION_UPDATE_STATUS)) {
            api.status((Map) request).enqueue(new Local<SuccessResponse>());
        } if (action.equals(Action.ACTION_LIVE_LOCATION)) {
//            api=ThisApp.getApi(this,"https://mom-apicalls.appspot.com/");
//            api=ThisApp.getApi(this);
            api.liveLocation((Map) request).enqueue(new Local2<SuccessResponse>());
        }if (action.equals(Action.ACTION_SAVE_TOKEN)) {
            api.saveLocation((Map) request).enqueue(new Local2<SuccessResponse>());
        } if (action.equals(Action.ACTION_GET_STATUS)) {
            api.getStatus((Map) request).enqueue(new Local2<GetStatusResponse>());
        }
    }





    static void getDialog(final Activity context, String tittle, String message, final Object request, final String action) {
        new AlertDialog.Builder(context)
                .setTitle(tittle)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ApiCallService.action(context,request,action);
                    }
                })
//                .setNegativeButton("Exit", null)
                .setIcon(R.drawable.ic_launcher_background)
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

}

