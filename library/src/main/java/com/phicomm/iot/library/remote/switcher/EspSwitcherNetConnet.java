package com.phicomm.iot.library.remote.switcher;

import android.renderscript.Element;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.phicomm.iot.library.device.SmartDevice;
import com.phicomm.iot.library.remote.EspBeans;
import com.phicomm.iot.library.remote.EspNetConnect;
import com.phicomm.iot.library.remote.RemoteDevice;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: allen.z
 * Date  : 2017-05-31
 * last modified: 2017-05-31
 */

public class EspSwitcherNetConnet extends RemoteDevice implements RemoteSwitcherInterface.IReporter{
    public final static  String TAG ="EspNetConnet";
    RemoteSwitcherInterface.ISwitcher mSwitcher;
    NetConnet mNetConnet;
    EspSwitcherNetConnet(RemoteDevice device, RemoteSwitcherInterface.ISwitcher switcher) {
        super(device);
        mSwitcher = switcher;
        mNetConnet = new NetConnet(device.getToken());
    }

    void handleGet(JSONObject jsonObject){
        Log.d(TAG, "handleGet");

    }

    void handlePost(JSONObject jsonObject){
        Log.d(TAG, "handlePost");
        boolean deliver = false;
        int nonce = 0;
        try {
            deliver = jsonObject.getBoolean("deliver_to_device");
            if(deliver) {
                nonce = jsonObject.getInt("nonce");
                mNetConnet.replyPost(nonce);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void start() {
        Log.d(TAG, "start");
        mNetConnet.start();
    }

    @Override
    public void stop() {
        Log.d(TAG, "stop");
        mNetConnet.close();
    }

    @Override
    public void reportStatus(boolean on) {
        Log.d(TAG,"reportStatus");
        EspBeans.DataPoints<DatapointsBean> dataType = new EspBeans.DataPoints<DatapointsBean>();
        int status = on? 1: 0;
        dataType.addDatapoints(new DatapointsBean(status));
        mNetConnet.pushDatapoint("plug-status",dataType);
    }

    class NetConnet extends EspNetConnect{

        protected NetConnet(String token) {
            super(token);
        }

        @Override
        public void onReceive(String json) {
            Log.d(TAG, "onReceive:"+json);
            try {
                JSONObject jsonObject = new JSONObject(json);
                String method = jsonObject.getString("method");
                if("GET".equals(method)){
                    handleGet(jsonObject);
                }else if("POST".equals(method)){
                    handlePost(jsonObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        public void pushDatapoint(String stream, EspBeans.DataPoints<DatapointsBean> dataJson){
            Log.d(TAG, "pushDatapoint>>dataJson");
            String path = "/v1/datastreams/"+stream+"/datapoints/";
            EspBeans.PostMethod dp = new EspBeans.PostMethod<EspBeans.DataPoints<DatapointsBean>>(getToken(), path, dataJson);
            String json = mGson.toJson(dp);
            send(json);
        }

        public void replyPost(int nonce){

            EspBeans.DataPoints<DatapointsBean> dataType = new EspBeans.DataPoints<DatapointsBean>();
            int status = mSwitcher.isOn()? 1: 0;
            dataType.addDatapoints(new DatapointsBean(status));
            EspBeans.PostReply reply = new EspBeans.PostReply<DatapointsBean>(200,nonce,true,dataType);
            String json = mGson.toJson(reply);
            send(json);
        }
    }
    public static class DatapointsBean {
        /**
         * x : 1
         */

        private int x;
        DatapointsBean(int status){
            x = status;
        }
    }

}
