package com.hxf.request.Monitor;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.hxf.request.Monitor.entity.RequestEntity;
import com.hxf.request.Monitor.service.RequestService;
import com.hxf.request.Monitor.utils.FloatingWindowHelper;

import org.json.JSONException;
import org.json.JSONObject;

import static com.hxf.request.Monitor.entity.RequestEntity.AUTHORIZATION;
import static com.hxf.request.Monitor.entity.RequestEntity.CAST_TIME;
import static com.hxf.request.Monitor.entity.RequestEntity.CONFIG;
import static com.hxf.request.Monitor.entity.RequestEntity.DATA;
import static com.hxf.request.Monitor.entity.RequestEntity.HEADERS;
import static com.hxf.request.Monitor.entity.RequestEntity.METHOD;
import static com.hxf.request.Monitor.entity.RequestEntity.PARAMS;
import static com.hxf.request.Monitor.entity.RequestEntity.REQUEST_TIME;
import static com.hxf.request.Monitor.entity.RequestEntity.STATUS;
import static com.hxf.request.Monitor.entity.RequestEntity.URL;
import static com.hxf.request.Monitor.entity.RequestEntity._200;
import static com.hxf.request.Monitor.entity.RequestEntity._404;

public class RNRequestMonitorModule extends ReactContextBaseJavaModule {
    private final ReactApplicationContext reactContext;

    private Intent sIntent;
    ServiceConnection serviceConnection;
    RequestService.RequestBinder binder;
    StringBuffer stringBuffer;
    RequestEntity requestEntity;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private boolean started;
    private static String STARTED = "started";

    public RNRequestMonitorModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        sIntent = new Intent(reactContext, RequestService.class);
        stringBuffer = new StringBuffer();
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (RequestService.RequestBinder)service;
                binder.showWindow();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.e("hxf","ServiceConnection onServiceDisconnected");
            }
        };
        settings = reactContext.getSharedPreferences("requestMonitor", 0);
        started = settings.getBoolean(STARTED,true);
        editor = settings.edit();
        if (started) {
            showWindow();
        }
        reactContext.addLifecycleEventListener(new LifecycleEventListener() {
            @Override
            public void onHostResume() {

            }

            @Override
            public void onHostPause() {

            }

            @Override
            public void onHostDestroy() {
                if (binder!=null) {
//                    reactContext.getApplicationContext().unbindService(serviceConnection);
//                    reactContext.stopService()
                }
            }
        });
    }

    @Override
    public String getName() {
        return "RNRequestMonitor";
    }


    @ReactMethod
    public void showWindow(){
        Log.e("hxf","ReactMethod bindService");
        if (binder == null) {
            if (FloatingWindowHelper.canDrawOverlays(reactContext, true)) {
                reactContext.getApplicationContext().bindService(sIntent,serviceConnection, Context.BIND_AUTO_CREATE);
            }
        } else {
            binder.showWindow();
        }
    }

    @ReactMethod
    public void hideWindow(){
        if (binder!=null) {
            binder.hideWindow();
        }
    }

    @ReactMethod
    public void addRecord(String strData){
        stringBuffer.delete(0,stringBuffer.length());
        Log.e("hxf","addRecord : "+strData);
        try {
            requestEntity = new RequestEntity();
            requestEntity.setMetadata(strData);
            JSONObject record = new JSONObject(strData);
            String status = record.getString(STATUS);
            String castTime = record.getString(CAST_TIME);
            String requestTime = "";
            if (record.has(REQUEST_TIME)){
                requestTime = record.getString(REQUEST_TIME);
            }
            JSONObject config = record.getJSONObject(CONFIG);
            String method = "-";
            if (config.has(METHOD)) {
                method = config.getString(METHOD);
            }
            JSONObject headersObj = config.getJSONObject(HEADERS);
            String headerStr = config.getString(HEADERS);
            String authorization = headersObj.getString(AUTHORIZATION);
            String params = config.getString(PARAMS);
            String url = config.getString(URL);

            requestEntity.setHeader(headerStr);
            requestEntity.setMethod(method);

            stringBuffer.append(REQUEST_TIME);
            stringBuffer.append(":");
            stringBuffer.append(requestTime);
            stringBuffer.append(",");
            requestEntity.setRequestTime(requestTime);

            stringBuffer.append(CAST_TIME);
            stringBuffer.append(":");
            stringBuffer.append(castTime);
            stringBuffer.append(",");
            requestEntity.setCastTime(castTime);

            stringBuffer.append(STATUS);
            stringBuffer.append(":");
            stringBuffer.append(status);
            stringBuffer.append(",");
            requestEntity.setStatus(status);

            stringBuffer.append(URL);
            stringBuffer.append(":");
            stringBuffer.append(url);
            stringBuffer.append(",");
            requestEntity.setUrl(url);

            stringBuffer.append(AUTHORIZATION);
            stringBuffer.append(":");
            stringBuffer.append(authorization);
            stringBuffer.append(",");
            requestEntity.setAuthorization(authorization);

            stringBuffer.append("params");
            stringBuffer.append(":");
            stringBuffer.append(params);
            stringBuffer.append(",");
            requestEntity.setParams(params);

            if (_200.equals(status)) {
                String data = record.getString(DATA);

                stringBuffer.append(DATA);
                stringBuffer.append(":");
                stringBuffer.append(data);
                stringBuffer.append(",");
                requestEntity.setData(data);
            } else if (_404.equals(status)) {

            }

            if (binder!=null) {
                binder.addEntity(requestEntity);
            }
            Log.e("hxf",stringBuffer.toString());
        } catch (JSONException e) {
            Log.e("hxf","parse Exception");
            e.printStackTrace();
        }
    }


    @ReactMethod
    public boolean isStarted(){
        return settings.getBoolean(STARTED,true);
    }

    @ReactMethod
    public boolean setStarted(boolean status){
        editor.putBoolean(STARTED,status);
        return editor.commit();
    }
}
