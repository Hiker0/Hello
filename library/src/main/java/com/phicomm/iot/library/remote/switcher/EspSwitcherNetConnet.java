package com.phicomm.iot.library.remote.switcher;

import android.util.Log;

import com.google.gson.Gson;
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
        Log.d(TAG, "handleGet");
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
        DataType dataType = new DataType();
        int status = on? 1: 0;
        dataType.addDatapoints(new DataType.DatapointsBean(status));
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


        public void pushDatapoint(String stream, DataType dataJson){
            Log.d(TAG, "pushDatapoint>>dataJson");
            EspBeans.DataPoint dp = new EspBeans.DataPoint<DataType>(getToken(), stream, dataJson);
            String json = mGson.toJson(dp);
            send(json);
        }
    }

    static class DataType {
        private List<DatapointsBean> datapoints;

        DataType(){
            datapoints = new ArrayList<DatapointsBean>();
        }

        public void addDatapoints(DatapointsBean datapoint) {
            datapoints.add(datapoint);
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

}
