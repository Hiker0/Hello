package com.phicomm.iot.library.devices.light;

import com.phicomm.iot.library.device.BaseDevice;
import com.phicomm.iot.library.device.SmartDevice;

/**
 * Created by allen.z on 2017-05-04.
 */
public class SmartLight extends SmartDevice {

    ILightProtocol lightProtocol;
    LightStateListener mListener;

    public SmartLight(BaseDevice device){
        super(device);
        lightProtocol = new PhiLightProtocol(this);
        setProtocol(lightProtocol);
    }

    @Override
    public void onConnectSuccess() {

    }

    public void setBrightness(int brightness){
        lightProtocol.setBrightness((byte)brightness);
    }

    @Override
    public void open() {
        lightProtocol.start();
    }

    @Override
    public void close() {
        lightProtocol.stop();
    }


    public void setStateChangeListener(LightStateListener listener) {
        mListener = listener;
    }

    public void qureyStatus(){
        lightProtocol.qureyStatus();
    }

    public void onBrightChange(int brightness){
        if(mListener != null){
            mListener.onBrightnessChange(brightness);
        }
    }

    public interface LightStateListener {
        void onBrightnessChange(int brightness);
    }
}
