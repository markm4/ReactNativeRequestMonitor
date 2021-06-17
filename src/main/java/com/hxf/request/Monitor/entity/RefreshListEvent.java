package com.hxf.request.Monitor.entity;

import java.util.LinkedList;

public class RefreshListEvent {
    public static int REFRESH = 1;
    public static int GETDATA = 2;

    public int ACTION_ID;
    public LinkedList<RequestEntity> data;
}
