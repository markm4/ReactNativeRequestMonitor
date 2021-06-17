package com.hxf.request.Monitor.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.hxf.request.Monitor.R;
import com.hxf.request.Monitor.activity.RequestListActivity;
import com.hxf.request.Monitor.entity.GetDataEvent;
import com.hxf.request.Monitor.entity.RefreshListEvent;
import com.hxf.request.Monitor.entity.RequestEntity;
import com.hxf.request.Monitor.utils.FloatingWindowHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedList;


public class RequestService extends Service {

    private LayoutInflater layoutInflater;
    private View windowView;
    private FloatingWindowHelper floatingWindowHelper;
    private Intent startActivityIntent;
    private LinkedList<RequestEntity> entities;
    private RefreshListEvent refreshListEvent;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("hxf","RequestService onCreate");
        layoutInflater = LayoutInflater.from(this);
        windowView = layoutInflater.inflate(R.layout.window_view,null,false);
        startActivityIntent = new Intent(this, RequestListActivity.class);
        startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        windowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(startActivityIntent);
            }
        });
        floatingWindowHelper = new FloatingWindowHelper(this);
        entities = new LinkedList<>();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        Log.e("hxf","RequestService onDestroy");
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("hxf","RequestService onUnbind");
        return super.onUnbind(intent);
    }

    public void showWindow(){
        floatingWindowHelper.addView(windowView, 100, 100, true);
    }

    public void hideWindow(){
        floatingWindowHelper.clear();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new RequestBinder();
    }

    public class RequestBinder extends Binder{
        RequestService getService() {
            return RequestService.this;
        }

        public void hideWindow(){
            RequestService.this.hideWindow();
        }
        public void showWindow(){
            RequestService.this.showWindow();
        }

        public boolean addEntity(RequestEntity entity){
            entities.addFirst(entity);
            Log.e("hxf","addEntity = "+entities.size());
            refreshListEvent = new RefreshListEvent();
            refreshListEvent.ACTION_ID = RefreshListEvent.REFRESH;
            refreshListEvent.data = entities;
            EventBus.getDefault().post(refreshListEvent);
            return true;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void getListData(GetDataEvent messageEvent) {
        refreshListEvent = new RefreshListEvent();
        refreshListEvent.ACTION_ID = RefreshListEvent.REFRESH;
        refreshListEvent.data = entities;
        EventBus.getDefault().post(refreshListEvent);
    }
}