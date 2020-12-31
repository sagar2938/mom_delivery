package com.momdelivery.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.momdelivery.R;
import com.momdelivery.activity.TrackMomActivity;
import com.momdelivery.activity.TrackUserActivity;
import com.momdelivery.network.ApiCallService;
import com.momdelivery.network.ThisApp;
import com.momdelivery.network.request.EventPushRequest;
import com.momdelivery.network.response.PushNotificationResponse;
import com.momdelivery.network.response.on_going.OrderDatum;
import com.momdelivery.utils.AppUser;
import com.momdelivery.utils.LocalRepositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewLeadAdapter extends RecyclerView.Adapter<NewLeadAdapter.ViewHolder> {

    Activity context;
    List<OrderDatum> response;

    public NewLeadAdapter(Activity mContext, List<OrderDatum> orderDataList) {
        this.context = mContext;
        response = orderDataList;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public NewLeadAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_new_lead, parent, false);
        return new NewLeadAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewLeadAdapter.ViewHolder viewHolder, int position) {
        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.recyclerView.setAdapter(new OrderItemDetailAdapter(context, response.get(position).getProductList()));

        viewHolder.name.setText(response.get(position).getName());
        viewHolder.address.setText(response.get(position).getLocation());
        viewHolder.totalAmount.setText("₹" + response.get(position).getTotalPrice());
        viewHolder.time.setText(response.get(position).getCreatedAt());
        viewHolder.orderId.setText(response.get(position).getOrderId());
        viewHolder.momCode.setText(response.get(position).getMom_code());
        viewHolder.momAddress.setText(response.get(position).getMom_address());

        viewHolder.note.setText(response.get(position).getNote());
        if (response.get(position).getNote()==null){
            viewHolder.note.setVisibility(View.GONE);
        }else {
            if (response.get(position).getNote().trim().equals("")){
                viewHolder.note.setVisibility(View.GONE);
            }else {
//                viewHolder.note.setVisibility(View.VISIBLE);
                viewHolder.note.setVisibility(View.GONE);
            }
        }

        Glide.with(context)
                .load(response.get(position).getImage_name())
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(new RequestOptions().placeholder(R.drawable.user)).into(viewHolder.image);

        viewHolder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDatum orderDatum = response.get(position);
                AppUser appUser = LocalRepositories.getAppUser(context);
                appUser.setOrderDatum(orderDatum);
                LocalRepositories.saveAppUser(context, appUser);
                Intent intent = new Intent(context, TrackUserActivity.class);
                context.startActivity(intent);
//                ApiCallService.action(context, map, ApiCallService.Action.ACTION_ACCEPT_ORDER);
            }
        });
        viewHolder.trackMom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDatum orderDatum = response.get(position);
                AppUser appUser = LocalRepositories.getAppUser(context);
                appUser.setOrderDatum(orderDatum);
                LocalRepositories.saveAppUser(context, appUser);
                Intent intent = new Intent(context, TrackMomActivity.class);
                context.startActivity(intent);
//                ApiCallService.action(context, map, ApiCallService.Action.ACTION_ACCEPT_ORDER);
            }
        });


        viewHolder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog("Delivered", "Are you sure you have delivered?", position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return response.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        TextView name;
        TextView address;
        TextView totalAmount;
        TextView time;
        TextView orderId;
        LinearLayout accept;
        LinearLayout trackMom;
        LinearLayout cancel;
        ImageView image;
        TextView note;
        TextView momAddress;
        TextView momCode;

        public ViewHolder(View view) {
            super(view);
            recyclerView = view.findViewById(R.id.recyclerView);
            name = view.findViewById(R.id.name);
            address = view.findViewById(R.id.address);
            totalAmount = view.findViewById(R.id.totalAmount);
            time = view.findViewById(R.id.time);
            orderId = view.findViewById(R.id.orderId);
            accept = view.findViewById(R.id.accept);
            cancel = view.findViewById(R.id.cancel);
            image = view.findViewById(R.id.image);
            note = view.findViewById(R.id.note);
            momAddress = view.findViewById(R.id.momAddress);
            momCode = view.findViewById(R.id.momCode);
            trackMom = view.findViewById(R.id.trackMom);

        }

    }


    public void getDialog(String tittle, String message, Integer position) {
        new AlertDialog.Builder(context)
                .setTitle(tittle)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Map map = new HashMap();
                        map.put("orderId", response.get(position).getOrderId());
                        ApiCallService.action(context, map, ApiCallService.Action.ACTION_COMPLETE_ORDER);

                        ThisApp.getApi(context).pushNotification(new EventPushRequest("Customer",response.get(position).getMobile(),"Your food has been delivered")).enqueue(new Callback<PushNotificationResponse>() {
                            @Override
                            public void onResponse(Call<PushNotificationResponse> call, Response<PushNotificationResponse> response) {

                            }

                            @Override
                            public void onFailure(Call<PushNotificationResponse> call, Throwable t) {

                            }
                        });


                        ThisApp.getApi(context).pushNotification(new EventPushRequest("Vendor",response.get(position).getMomMobile(),"Your food has been delivered to user")).enqueue(new Callback<PushNotificationResponse>() {
                            @Override
                            public void onResponse(Call<PushNotificationResponse> call, Response<PushNotificationResponse> response) {

                            }

                            @Override
                            public void onFailure(Call<PushNotificationResponse> call, Throwable t) {

                            }
                        });

                    }
                })
                .setNegativeButton("No", null)
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }

}
