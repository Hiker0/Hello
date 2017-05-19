package com.phicomm.iot.library.devices.switcher;

import com.google.gson.Gson;
import com.phicomm.iot.library.protocol.esp.EspClient;
import com.phicomm.iot.library.protocol.esp.StatusResponse;
import com.phicomm.iot.library.devices.switcher.SwitchInterface.OperateFinish;
import java.io.IOException;

import okhttp3.Response;
import rx.functions.Action1;

/**
 * Author: allen.z
 * Date  : 2017-05-18
 * last modified: 2017-05-18
 */
public class EspSwitcherService extends EspClient implements SwitchInterface.ISwitcher {
    String ipAddress;
    SwitchInterface.IListener mListener;


    EspSwitcherService(String host, SwitchInterface.IListener listener) {
        super(host);
        ipAddress = host;
        mListener = listener;
    }

    @Override
    public void start() {
        super.start();
        qureyStatus();
    }

    @Override
    public void turnOn(OperateFinish finish) {
        changeStatus(1, finish);
    }

    @Override
    public void turnOff(SwitchInterface.OperateFinish finish) {
        changeStatus(0, finish);
    }

    void changeStatus(int status, final OperateFinish finish) {
        StatusResponse statusResponse = new StatusResponse();
        statusResponse.setStatus(status);
        Gson gson = new Gson();

        String url = getUrl("/config", "switch");
        postCommand(url, gson.toJson(statusResponse), new Action1<Response>() {
            @Override
            public void call(Response response) {
//                handleStatusResponse(response);
                if (response.isSuccessful()) {
                    finish.OnFinish();
                }
            }
        });
    }

    void handleStatusResponse(Response response) {
        if (response.isSuccessful()) {
            try {
                Gson gson = new Gson();
                String json = response.body().string();
                StatusResponse statusResponse = gson.fromJson(json, StatusResponse.class);
                if (mListener != null && statusResponse != null) {
                    boolean on = statusResponse.getStatus() == 1;
                    mListener.onStatusChange(on);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void qureyStatus() {
        String url = getUrl("/config", "switch");
        getCommand(url, new Action1<Response>() {
            @Override
            public void call(Response response) {
                handleStatusResponse(response);
            }
        });
    }

}
