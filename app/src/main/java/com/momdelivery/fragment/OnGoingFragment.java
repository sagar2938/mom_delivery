package com.momdelivery.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.momdelivery.R;
import com.momdelivery.adapter.OnGoingAdapter;
import com.momdelivery.base.BaseFragment;
import com.momdelivery.network.ApiCallService;
import com.momdelivery.network.response.on_going.OrderDatum;
import com.momdelivery.network.response.success.SuccessResponse;
import com.momdelivery.network.response.on_going.GetOnGoingResponse;
import com.momdelivery.utils.CustomProgressDialog;
import com.momdelivery.utils.Helper;
import com.momdelivery.utils.Preferences;

import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class OnGoingFragment  extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    CustomProgressDialog progressDialog;


    LinearLayout dateLayout;
    LinearLayout fromLayout;
    LinearLayout toLayout;
    TextView start;
    TextView end,from_tv;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_targets, null);

        recyclerView=view.findViewById(R.id.recyclerView);
        swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);

        dateLayout = view.findViewById(R.id.dateLayout);
        fromLayout =view. findViewById(R.id.fromLayout);
        toLayout = view.findViewById(R.id.toLayout);
        from_tv=view.findViewById(R.id.from_tv);
        start = view.findViewById(R.id.from);
        end = view.findViewById(R.id.to);

        swipeRefreshLayout.setOnRefreshListener(this);
        progressDialog=CustomProgressDialog.getInstance(getActivity());

        start.setText(Helper.getCurrentDate());
        end.setText(Helper.getCurrentDate());

        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateDialog(start,end);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Map map=new HashMap();
        map.put("mobile", Preferences.getInstance(getContext()).getMobile());
        map.put("start_date", Helper.getCurrentDate());
        map.put("end_date", Helper.getCurrentDate());
        ApiCallService.action2(getActivity(),map,ApiCallService.Action.ACTION_GET_ON_GOING,progressDialog);
    }

    @Subscribe
    public void getNewLead(GetOnGoingResponse response){
        swipeRefreshLayout.setRefreshing(false);
        progressDialog.dismiss();
        if (response.getSuccess()){
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            List<OrderDatum> datumList=response.getOrderData();
//            Collections.reverse(datumList);
            recyclerView.setAdapter(new OnGoingAdapter(getActivity(),response.getOrderData()));
        }else {
            getDialogSorry();
        }

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        Map map=new HashMap();
        map.put("mobile", Preferences.getInstance(getContext()).getMobile());
        map.put("start_date", Helper.getCurrentDate());
        map.put("end_date", Helper.getCurrentDate());
        ApiCallService.action2(getActivity(),map,ApiCallService.Action.ACTION_GET_ON_GOING,progressDialog);
    }


    /*@Subscribe
    public void AcceptCancel(SuccessResponse response){
        if (response.getResponse().getConfirmation()==1) {
            getDialogSuccess(*//*response.getResponse().getOrderId()+*//*" "+response.getResponse().getMessage());
            onRefresh();
        }else {
            getDialogSorry();
        }

    }*/

    @Subscribe
    public void timeOut(String msg) {
        progressDialog.dismiss();
        getDialog("Failed", msg);
    }



    public void dateDialog(TextView start, TextView end) {

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().requestFeature(1);
        dialog.setContentView(R.layout.date_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        TextView from = dialog.findViewById(R.id.from);
        LinearLayout close = dialog.findViewById(R.id.close);
        TextView to = dialog.findViewById(R.id.to);
        Button submit = dialog.findViewById(R.id.submit);

        from.setText(Helper.getCurrentDate());
        to.setText(Helper.getCurrentDate());

        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateSelection(from, "start", to);
            }
        });
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateSelection(to, "end", from);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date fromDate = null;
                Date endDate = null;
                try {
                    fromDate = sdf.parse(from.getText().toString());
                    endDate = sdf.parse(to.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (fromDate.getTime() > endDate.getTime()) {
                    Toast.makeText(getActivity(), "Enter valid date", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
                start.setText(from.getText().toString());
                end.setText(to.getText().toString());

                Map map = new HashMap();
                map.put("start_date", from.getText().toString());
                map.put("end_date", to.getText().toString());
                map.put("mobile", Preferences.getInstance(getActivity()).getMobile());
                ApiCallService.action2(getActivity(),map,ApiCallService.Action.ACTION_GET_ON_GOING,progressDialog);
            }
        });

        dialog.show();
    }

    public void dateSelection(TextView v2, String str, TextView v3) {


        Calendar calendar = Calendar.getInstance();
//        String date = v3.getText().toString();
//        int year = Integer.parseInt(date.split("/")[2]);
//        int month = Integer.parseInt(date.split("/")[1]);
//        int day = Integer.parseInt(date.split("/")[0]);
//        calendar.set(year, month - 1, day);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                String d = new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(calendar.getTime());
                if (str.equals("start")) {
                    v2.setText(d);
                } else if (str.equals("end")) {
                    v2.setText(d);
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


}
