package com.phicomm.demo.device.light;

import com.phicomm.iot.library.device.BaseDevice;
import com.phicomm.iot.library.devices.light.SmartLight;

/**
 * Created by hzn on 17-5-11.
 */

public class LightPresenter implements LightContract.Presenter {
    public LightContract.View mLightView;
    private BaseDevice mDevice;
    private SmartLight mLight;

    private int mBrightness = 0;

    public LightPresenter(LightContract.View view, BaseDevice device) {
        mLightView = view;
        mLightView.setPresenter(this);

        mDevice = device;
//        BaseDevice dev = new BaseDevice();
//        dev.setAddress(device.getLocalAddress().getHostAddress());
//        dev.setBssid(device.getBssid());
//        dev.setType(device.getType());
//        dev.setBrand(BRAND.PHICOMM);
        mLight = new SmartLight(device);
        mLight.setStateChangeListener(new SmartLight.LightStateListener() {
            @Override
            public void onBrightnessChange(int brightness) {
                mLightView.notifyState(brightness);
            }
        });
    }

    @Override
    public void start() {
        mLight.open();
        mLightView.notifyState(mBrightness);

    }


    public void stop(){
        mLight.close();
    }


    @Override
    public void setBrightness(int brightness) {
        mLight.setBrightness(brightness);
    }

    @Override
    public void refresh() {
        mLight.qureyStatus();
    }

}
