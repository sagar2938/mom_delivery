package com.momdelivery.network;

import com.momdelivery.network.request.EventPushRequest;
import com.momdelivery.network.response.GetStatusResponse;
import com.momdelivery.network.response.PushNotificationResponse;
import com.momdelivery.network.response.TokenResponse;
import com.momdelivery.network.response.login.LoginResponse;
import com.momdelivery.network.response.success.SuccessResponse;
import com.momdelivery.network.response.on_going.GetNewOrderResponse;
import com.momdelivery.network.response.on_going.GetOnGoingResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {

    @POST("delivery/login/")
    Call<LoginResponse> login(@Body Map signUp);


    @POST("delivery/get/new/order/list/")
    Call<GetNewOrderResponse> getNewLeadOrder(@Body Map signUp);

    @POST("api/fcm/confirm/order/ ")
    Call<SuccessResponse> acceptOrder(@Body Map signUp);


    @POST("api/fcm/cancel/order/")
    Call<SuccessResponse> cancelOrder(@Body Map signUp);

    @POST("/delivery/get/complete/order/list/")
    Call<GetOnGoingResponse> onGoingOrder(@Body Map signUp);


    @POST("delivery/partner/addtoken/")
    Call<TokenResponse> sendToken(@Body Map request);

    @POST("api/fcm/complete/order/")
    Call<SuccessResponse> completeOrder(@Body Map request);

    @POST("delivery/live/location/")
    Call<SuccessResponse> addLiveLocation(@Body Map request);

    @POST("/delivery/partner/online/status/")
    Call<SuccessResponse> status(@Body Map signUp);

    @POST("/delivery/live/location/")
    Call<SuccessResponse> liveLocation(@Body Map address);

    @POST("api/mom/add/token/")
    Call<SuccessResponse> saveLocation(@Body Map request);

    @POST("api/user/fcm/updated/")
    Call<PushNotificationResponse> pushNotification(@Body EventPushRequest request);

    @POST("/delivery/get/deliver/status/")
    Call<GetStatusResponse> getStatus(@Body Map request);

}
