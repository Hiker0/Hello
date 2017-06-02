package com.phicomm.iot.library.devices.switcher;

import com.google.gson.Gson;
import com.phicomm.iot.library.devices.switcher.SwitchInterface.OperateFinish;
import com.phicomm.iot.library.protocol.IProtocol;
import com.phicomm.iot.library.protocol.esp.EspNetClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Response;

/**
 * Author: allen.z
 * Date  : 2017-05-18
 * last modified: 2017-05-18
 */
public class EspSwitcherNetService implements SwitchInterface.ISwitcher, IProtocol {
    static String STREAM_NAME = "plug-status";
    EspNetClient mClient;
    String mToken;
    ExecutorService mSingleThreadExecutor;
    SwitchInterface.IListener mListener;

    EspSwitcherNetService(String token, SwitchInterface.IListener listener) {
        mToken = token;
        mListener = listener;
        mClient = new EspNetClient(mToken);
    }

    @Override
    public void start() {
        mSingleThreadExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void stop() {
        mSingleThreadExecutor.shutdown();
    }

    @Override
    public void turnOn(OperateFinish finish) {
        createPoint(true);
    }

    private void createPoint(boolean on){
        StatusData data = new StatusData(on);
        Gson gson = new Gson();
        final String json = gson.toJson(data);

        mSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    Response response = mClient.createDataPoint(STREAM_NAME, json);
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String datapoint = jsonObject.getString("datapoint");
                    JSONObject jsonObjectPoint = new JSONObject(datapoint);
                    int x = jsonObjectPoint.getInt("x");
                    mListener.onStatusChange(x == 1);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void turnOff(OperateFinish finish) {
        createPoint(false);
    }

    @Override
    public void qureyStatus() {
        mSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = mClient.getDataPoint(STREAM_NAME);
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String datapoint = jsonObject.getString("datapoint");
                    JSONObject jsonObjectPoint = new JSONObject(datapoint);
                    int x = jsonObjectPoint.getInt("x");
                    mListener.onStatusChange(x == 1);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    class StatusData {

        private DatapointBean datapoint;

        StatusData(boolean on) {
            datapoint = new DatapointBean(on? 1: 0);
        }

        public class DatapointBean {
            private int x;

            DatapointBean(int x) {
                this.x = x;
            }
        }
    }

}
