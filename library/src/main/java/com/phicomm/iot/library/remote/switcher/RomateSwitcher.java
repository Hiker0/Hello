package com.phicomm.iot.library.remote.switcher;

import com.phicomm.iot.library.device.BaseDevice;
import com.phicomm.iot.library.devices.switcher.ISwitcher;
import com.phicomm.iot.library.device.SmartDevice;

/**
 * Created by allen.z on 2017-05-05.
 */
public class RomateSwitcher extends SmartDevice implements ISwitcher {
    IRemoteSwitcher switcherProtocol;
    RemoteSwitchListener mListener;
    boolean isOn;
    public RomateSwitcher(BaseDevice dev) {
        super(dev);
        switcherProtocol = new EspRemoteSwitcher(this,this);
    }


    public void open() {
        switcherProtocol.start();
    }

    public void close() {
        switcherProtocol.stop();
    }

    @Override
    public void turnOn() {
        isOn = true;
        if (mListener != null) {
            mListener.turnOn();
        }
        switcherProtocol.reportStatus(true);
    }

    public void reportStatus(boolean on){
        isOn = on;
        switcherProtocol.reportStatus(on);
    }

    @Override
    public void turnOff() {
        isOn = false;
        if (mListener != null) {
            mListener.turnOff();
        }

        switcherProtocol.reportStatus(false);
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
