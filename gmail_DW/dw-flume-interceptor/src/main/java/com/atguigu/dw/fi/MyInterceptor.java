package com.atguigu.dw.fi;

import com.google.gson.Gson;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.HashMap;
import java.util.List;

public class MyInterceptor implements Interceptor {

    private static final String SELECTOER_HEADER ="logType";
    private Gson gson = null;

    @Override
    public void initialize() {
       gson = new Gson();
    }

    @Override
    public Event intercept(Event event) {
        String body = new String(event.getBody());
        HashMap hashMap = gson.fromJson(body, HashMap.class);
        String type = (String)hashMap.get("type");
        event.getHeaders().put(SELECTOER_HEADER,type);
        return event;
    }

    @Override
    public List<Event> intercept(List<Event> list) {
        for (Event event : list) {
            intercept(event);
        }
        return list;
    }

    @Override
    public void close() {

    }
    public static class Builder implements Interceptor.Builder{

        @Override
        public Interceptor build() {
            return new MyInterceptor();
        }

        @Override
        public void configure(Context context) {

        }
    }



}
