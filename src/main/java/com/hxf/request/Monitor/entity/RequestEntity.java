package com.hxf.request.Monitor.entity;

import java.io.Serializable;

public class RequestEntity implements Serializable {

    public static String CAST_TIME = "castTime";
    public static String METHOD = "method";
    public static String REQUEST_TIME = "requestTime";
    public static String STATUS = "status";
    public static String CONFIG = "config";
    public static String HEADERS = "headers";
    public static String PARAMS = "data";
    public static String URL = "url";
    public static String DATA = "data";
    public static String AUTHORIZATION = "Authorization";
    public static String _200 = "200";
    public static String _404 = "404";

    String url;
    String status;
    String Authorization;
    String params = "null";
    String castTime;
    String data;
    String requestTime;
    String method;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    String header;

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    String metadata;

    public String getUrl() {
        return url;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthorization() {
        return Authorization;
    }

    public void setAuthorization(String authorization) {
        Authorization = authorization;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCastTime() {
        return castTime;
    }

    public void setCastTime(String castTime) {
        this.castTime = castTime;
    }

    @Override
    public String toString() {
        return "RequestEntity{" +
                "url='" + url + '\'' +
                ", status='" + status + '\'' +
                ", Authorization='" + Authorization + '\'' +
                ", params='" + params + '\'' +
                ", castTime='" + castTime + '\'' +
                ", data='" + data + '\'' +
                ", requestTime='" + requestTime + '\'' +
                ", metadata='" + metadata + '\'' +
                '}';
    }
}
