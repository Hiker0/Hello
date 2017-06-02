package com.phicomm.iot.library.protocol.esp;

import android.util.Log;

import com.phicomm.iot.library.protocol.IProtocol;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Author: allen.z
 * Date  : 2017-05-18
 * last modified: 2017-05-18
 */
public class EspNetClient implements IProtocol {
    final static String TAG = "Esp/EspNetClient";
    private String HOST ="https://iot.espressif.cn";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private String mToken ;
    protected OkHttpClient mClient;

    public EspNetClient(String token) {
        mToken = token;
        mClient = new OkHttpClient();
    }

    public Response getDataPoint(String streamName) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(HOST);
        sb.append("/v1/datastreams/");
        sb.append(streamName);
        sb.append("/datapoint/");
        String url = sb.toString();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization","token "+mToken)
                .build();
        Response response = mClient.newCall(request).execute();
        return response;
    }

    public Response createDataPoint(String streamName, String  json) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(HOST);
        sb.append("/v1/datastreams/");
        sb.append(streamName);
        sb.append("/datapoint/");
        sb.append("?deliver_to_device=true");
        String url = sb.toString();

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization","token "+mToken)
                .post(body)
                .build();
        Response response = mClient.newCall(request).execute();
        return response;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
