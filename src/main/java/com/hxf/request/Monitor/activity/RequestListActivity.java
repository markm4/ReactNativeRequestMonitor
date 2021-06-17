package com.hxf.request.Monitor.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hxf.request.Monitor.R;
import com.hxf.request.Monitor.adapter.RequestListAdapter;
import com.hxf.request.Monitor.entity.GetDataEvent;
import com.hxf.request.Monitor.entity.RefreshListEvent;
import com.hxf.request.Monitor.entity.RequestEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;

public class RequestListActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    LinkedList<RequestEntity> data = new LinkedList<>();
    RequestListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RequestListAdapter(data,this);
        recyclerView.setAdapter(adapter);
    }

    public static void launchActivity(Activity activity){
        Intent intent = new Intent(activity,RequestListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        RequestDetailActivity.launchActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        GetDataEvent getDataEvent = new GetDataEvent();
        EventBus.getDefault().post(getDataEvent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshList(RefreshListEvent messageEvent) {
        if(messageEvent.ACTION_ID == RefreshListEvent.REFRESH) {
            Log.e("hxf","action = "+messageEvent.ACTION_ID);
            Log.e("hxf","data = "+messageEvent.data);
            data.clear();
            data.addAll(messageEvent.data);
            Log.e("hxf","data size = "+data.size());
            if (adapter!=null) {
                Log.e("hxf","notifyDataSetChanged");
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
}