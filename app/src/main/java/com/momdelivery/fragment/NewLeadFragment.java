package com.momdelivery.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.momdelivery.R;
import com.momdelivery.adapter.NewLeadAdapter;
import com.momdelivery.base.BaseFragment;
import com.momdelivery.network.ApiCallService;
import com.momdelivery.network.response.success.SuccessResponse;
import com.momdelivery.network.response.on_going.GetNewOrderResponse;
import com.momdelivery.utils.CustomProgressDialog;
import com.momdelivery.utils.Preferences;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class NewLeadFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    CustomProgressDialog progressDialog;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_lead_new_total, null);
        recyclerView=view.findViewById(R.id.recyclerView);
        swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        progressDialog=CustomProgressDialog.getInstance(getActivity());
       return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Map map=new HashMap();
        map.put("mobile", Preferences.getInstance(getContext()).getMobile());
        ApiCallService.action2(getActivity(),map,ApiCallService.Action.ACTION_GET_NEW_LEAD,progressDialog);

    }

    @Subscribe
    public void getNewLead(GetNewOrderResponse response){
        progressDialog.dismiss();
        swipeRefreshLayout.setRefreshing(false);
        if (response.getSuccess()){
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new NewLeadAdapter(getActivity(),response.getOrderData()));
        }else {
            getDialog("Something went wrong");
        }

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        progressDialog=CustomProgressDialog.getInstance(getActivity());
        Map map=new HashMap();
        map.put("mobile", Preferences.getInstance(getContext()).getMobile());
        ApiCallService.action2(getActivity(),map,ApiCallService.Action.ACTION_GET_NEW_LEAD,progressDialog);
    }


    @Subscribe
    public void AcceptCancel(SuccessResponse response){
        if (response.getResponse().getConfirmation()==1) {
            getDialogSuccess(/*response.getResponse().getOrderId()+*/" "+response.getResponse().getMessage());
            onRefresh();
        }else {
            getDialogSorry();
        }
    }


    @Subscribe
    public void timeOut(String msg) {
        progressDialog.dismiss();
        getDialog("Failed", msg);
    }

}
