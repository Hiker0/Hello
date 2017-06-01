package com.phicomm.iot.library.remote.switcher;

import com.phicomm.iot.library.device.BaseDevice;
import com.phicomm.iot.library.device.SmartDevice;
import com.phicomm.iot.library.remote.RemoteDevice;

/**
 * Created by allen.z on 2017-05-05.
 */
public class RomateSwitcher extends RemoteDevice implements RemoteSwitcherInterface.ISwitcher {
    RemoteSwitcherInterface.IReporter switcherProtocol;
    RemoteSwitchListener mListener;
    EspSwitcherNetConnet mNetConnect;
    boolean isOn;
    public RomateSwitcher(BaseDevice dev, String token) {
        super(dev, token);
        switcherProtocol = new EspRemoteSwitcher(this,this);
        mNetConnect = new EspSwitcherNetConnet(this,this);
    }


    public void open() {
        switcherProtocol.start();
        mNetConnect.start();
    }

    public void close() {
        switcherProtocol.stop();
        mNetConnect.stop();
    }

    @Override
    public void turnOn() {
        isOn = true;
        if (mListener != null) {
            mListener.turnOn();
        }
        reportStatus(true);
    }

    public void reportStatus(boolean on){
        isOn = on;
        switcherProtocol.reportStatus(on);
        mNetConnect.reportStatus(on);
    }

    @Override
    public void turnOff() {
        isOn = false;
        if (mListener != null) {
            mListener.turnOff();
        }

        reportStatus(false);
    }

    @Override
    public boolean isOn() {
        return isOn;
    }

    public void setListener(RemoteSwitchListener listener){
        mListener = listener;
    }
    public interface  RemoteSwitchListener{
        void turnOn();
        void turnOff();
    }

}
