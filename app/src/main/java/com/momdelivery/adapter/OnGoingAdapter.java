package com.momdelivery.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.momdelivery.R;
import com.momdelivery.network.ApiCallService;
import com.momdelivery.network.response.on_going.OrderDatum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OnGoingAdapter extends RecyclerView.Adapter<OnGoingAdapter.ViewHolder> {

    Activity context;
    List<OrderDatum> response;
    public OnGoingAdapter(Activity mContext, List<OrderDatum> orderDataList) {
        this.context=mContext;
        response=orderDataList;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public OnGoingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_on_going, parent, false);
        return new OnGoingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OnGoingAdapter.ViewHolder viewHolder, int position) {
        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.recyclerView.setAdapter(new OrderItemDetailAdapter(context,response.get(position).getProductList()));

     //   Glide.with(context).load(response.get(position).getImage_name()).into(viewHolder.image);

        viewHolder.name.setText(response.get(position).getName());
        viewHolder.address.setText(response.get(position).getLocation());
        viewHolder.totalAmount.setText("₹"+response.get(position).getTotalPrice());
        viewHolder.time.setText(response.get(position).getCreatedAt());
        viewHolder.orderId.setText(response.get(position).getOrderId());


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


        viewHolder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map=new HashMap();
                map.put("orderId",response.get(position).getOrderId());
//                ApiCallService.action(context, map, ApiCallService.Action.ACTION_ACCEPT_ORDER);
            }
        });


        viewHolder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog("Cancel","Are you sure you want to cancel this order?",position);
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
        LinearLayout cancel;
        TextView note;
        ImageView image;

        public ViewHolder(View view) {
            super(view);
            recyclerView=view.findViewById(R.id.recyclerView);
            name=view.findViewById(R.id.name);
            address=view.findViewById(R.id.address);
            totalAmount=view.findViewById(R.id.totalAmount);
            time=view.findViewById(R.id.time);
            orderId=view.findViewById(R.id.orderId);
            accept=view.findViewById(R.id.accept);
            cancel=view.findViewById(R.id.cancel);
            note=view.findViewById(R.id.note);
            image=view.findViewById(R.id.circular_iv);

        }

    }



    public void getDialog(String tittle, String message,Integer position) {
        new AlertDialog.Builder(context)
                .setTitle(tittle)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Map map=new HashMap();
                        map.put("orderId",response.get(position).getOrderId());
//                        ApiCallService.action(context, map, ApiCallService.Action.ACTION_CANCEL_ORDER);

                    }
                })
                .setNegativeButton("No", null)
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }

}
