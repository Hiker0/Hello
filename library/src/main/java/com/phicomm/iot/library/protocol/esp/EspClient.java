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
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Author: allen.z
 * Date  : 2017-05-18
 * last modified: 2017-05-18
 */
public class EspClient implements IProtocol {
    final static String TAG = "Esp/EspClient";
    private String mHost;
    private int mPort;
    protected OkHttpClient mClient;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    protected EspClient(String host) {
        this(host, 80);
    }

    protected EspClient(String host, int port) {
        mHost = host;
        mPort = port;
        mClient = new OkHttpClient();
    }

    protected String getHost() {
        return mHost;
    }

    protected int getPort() {
        return mPort;
    }

    protected String getUrl(String url, String command) {
        StringBuilder sb = new StringBuilder();
        sb.append("http://")
                .append(getHost())
                .append(":")
                .append(getPort())
                .append(url)
                .append("?")
                .append("command=")
                .append(command);
        return sb.toString();
    }

    protected Response postCommand(final String url, final String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = mClient.newCall(request).execute();
        return response;
    }

    protected void postCommand(final String url, final String json, Action1<Response> action) {
        Subscription observable = Observable.create(new Observable.OnSubscribe<Response>() {
            @Override
            public void call(Subscriber<? super Response> subscriber) {
                Response response = null;
                try {
                    response = postCommand(url, json);
                    subscriber.onNext(response);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "post fail");
                    //subscriber.onError(e);
                }

            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe(action);
    }

    protected Response getCommand(final String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = mClient.newCall(request).execute();
        return response;
    }

    protected void getCommand(final String url, Action1<Response> action) {
        Subscription observable = Observable.create(new Observable.OnSubscribe<Response>() {
            @Override
            public void call(Subscriber<? super Response> subscriber) {
                Response response = null;
                try {
                    response = getCommand(url);
                    subscriber.onNext(response);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "get fail");
//                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe(action);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
