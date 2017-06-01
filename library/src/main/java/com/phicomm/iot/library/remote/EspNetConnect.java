package com.phicomm.iot.library.remote;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.phicomm.iot.library.connect.tcp.SocketClient;

/**
 * Author: allen.z
 * Date  : 2017-05-27
 * last modified: 2017-05-27
 */

public class EspNetConnect implements SocketClient.Listener{
    public final static  String TAG ="EspNetConnect";
    final static  String HOST ="iot.espressif.cn";
    final static  int PORT = 8000;
    final static  long PING_MS = 8000;

    SocketClient mClient;
    String mToken;
    String pingJson ;
    Handler mHandler;
    protected Gson mGson;

    protected EspNetConnect(String token){
        mToken = token;
        mClient = new SocketClient(HOST,PORT);
        mClient.setListener(this);
        mGson = new Gson();
        mHandler = new Handler();
    }

    Runnable pingRunnable = new Runnable() {
        @Override
        public void run() {
            ping();
            mHandler.postDelayed(pingRunnable, PING_MS);
        }
    };

    public void start(){
        mClient.connect();
    }

    void identify(){
        Log.d(TAG, "identify");
        EspBeans.IdentifyBean identifyBean = new EspBeans.IdentifyBean(mToken);
        Gson gson = new Gson();
        String identifyJson = gson.toJson(identifyBean);
        mClient.send(identifyJson);
        mHandler.postDelayed(pingRunnable, PING_MS);
    }

    protected void send(String json){
        mClient.send(json);
    }

    void ping(){
        Log.d(TAG, "ping");
        if(pingJson == null){
            EspBeans.PingBean pingBean = new EspBeans.PingBean(mToken);
            pingJson = mGson.toJson(pingBean);
            mHandler = new Handler();
        }
        mClient.send(pingJson);
    }

    @Override
    public void onConnect() {
        Log.d(TAG, "onConnect");
        identify();
    }

    @Override
    public void onReceive(String json) {
        Log.d(TAG, "onReceive:"+json);
    }

    @Override
    public void onClose() {
        Log.d(TAG, "onClose");
        close();
    }

    public void close(){
        Log.d(TAG, "close");
        mHandler.removeCallbacks(pingRunnable);
        mClient.disconnect();
    }


}
