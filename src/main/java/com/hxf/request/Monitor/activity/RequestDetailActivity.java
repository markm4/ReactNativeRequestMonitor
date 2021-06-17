package com.hxf.request.Monitor.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.hxf.request.Monitor.R;
import com.hxf.request.Monitor.entity.RequestEntity;

import java.io.Serializable;

public class RequestDetailActivity extends AppCompatActivity {

    public static String REQUEST_ENTITY = "RequestEntity";
    private Serializable serializable;
    private RequestEntity requestEntity;
    private EditText urlEdt,methodEdt,headerEdt,paramsEdt,resultEdt,allEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
        urlEdt = findViewById(R.id.url_edt);
        methodEdt = findViewById(R.id.method_edt);
        headerEdt = findViewById(R.id.header_edt);
        paramsEdt = findViewById(R.id.params_edt);
        resultEdt = findViewById(R.id.result_edt);
        allEdt = findViewById(R.id.all_edt);
    }

    @Override
    protected void onResume() {
        super.onResume();
        serializable = getIntent().getSerializableExtra(REQUEST_ENTITY);
        if (serializable!=null) {
            requestEntity = (RequestEntity)serializable;
            Log.e("hxf",requestEntity.toString());
            urlEdt.setText(requestEntity.getUrl());
            methodEdt.setText(requestEntity.getMethod());
            headerEdt.setText(requestEntity.getHeader());
            paramsEdt.setText(requestEntity.getParams());
            resultEdt.setText(requestEntity.getData());
            allEdt.setText(requestEntity.getMetadata());
        }
    }

    public static void launchActivity(Activity activity){
        Intent intent = new Intent(activity,RequestDetailActivity.class);
        activity.startActivity(intent);
    }

    public static void launchActivity(Activity activity, RequestEntity entity){
        Intent intent = new Intent(activity,RequestDetailActivity.class);
        intent.putExtra(REQUEST_ENTITY,entity);
        activity.startActivity(intent);
    }
}