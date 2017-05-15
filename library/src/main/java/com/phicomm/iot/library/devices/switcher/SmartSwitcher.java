package com.phicomm.iot.library.devices.switcher;

import com.phicomm.iot.library.device.BaseDevice;
import com.phicomm.iot.library.device.SmartDevice;

/**
 * Created by allen.z on 2017-05-04.
 */
public class SmartSwitcher extends SmartDevice {

    ISwitcherProtocol switcherProtocol;
    SwitcherStateListener mListener;

    public SmartSwitcher(BaseDevice device){
        super(device);
        if(device.getBrand().equals("phicomm")) {
            switcherProtocol = new PhiSwitcherProtocol(this);
        }
        setProtocol(switcherProtocol);
    }

    @Override
    public void onConnectSuccess() {

    }

    @Override
    public void open() {
        switcherProtocol.start();
    }

    @Override
    public void close() {
        switcherProtocol.stop();
    }


    public void setStateChangeListener(SwitcherStateListener listener) {
        mListener = listener;
    }

    public void turnOn() {
        switcherProtocol.turnOn();
    }

    public void turnOff() {
        switcherProtocol.turnOff();
    }

    public void qureyStatus(){
        switcherProtocol.qureyStatus();
    }

    public void onStateChange(boolean on){
        if(mListener != null){
            mListener.onStateChange(on);
        }
    }

    public interface  SwitcherStateListener{
        void onStateChange(boolean on);
    }
}
