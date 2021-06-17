package com.hxf.request.Monitor.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxf.request.Monitor.R;
import com.hxf.request.Monitor.activity.RequestDetailActivity;
import com.hxf.request.Monitor.entity.RequestEntity;

import java.util.LinkedList;

public class RequestListAdapter extends RecyclerView.Adapter {

    LinkedList<RequestEntity> data;
    Context context;
    LayoutInflater layoutInflater;
    View ItemView;
    RecyclerView.ViewHolder viewHolder;
    RequestEntity entity;

    public RequestListAdapter(LinkedList<RequestEntity> data, Context context) {
        this.data = data;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ItemView = layoutInflater.inflate(R.layout.request_item_view,null);
        viewHolder = new RequestListHolder(ItemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        entity = data.get(i);
        ((RequestListHolder)viewHolder).urlTV.setText(entity.getUrl());
        ((RequestListHolder)viewHolder).timeTV.setText(entity.getRequestTime());
        ((RequestListHolder)viewHolder).paramsTV.setText(entity.getParams());
        ((RequestListHolder) viewHolder).view.setOnClickListener((e)->{
            entity = data.get(i);
            RequestDetailActivity.launchActivity((Activity) context,entity);
        });
//        ((RequestListHolder)viewHolder).urlTV.setText("url");
//        ((RequestListHolder)viewHolder).timeTV.setText("time");
//        ((RequestListHolder)viewHolder).paramsTV.setText("params");
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
//        return 10;
    }

    public class RequestListHolder extends RecyclerView.ViewHolder{
        public TextView urlTV,timeTV,paramsTV;
        public View view;
        public RequestListHolder(View itemView) {
            super(itemView);
            urlTV  =  itemView.findViewById(R.id.url_tv);
            timeTV  =  itemView.findViewById(R.id.request_time_tv);
            paramsTV  =  itemView.findViewById(R.id.request_params_tv);
            view = itemView;
        }
    }
}
