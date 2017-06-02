package com.phicomm.iot.library.devices.switcher;

import android.os.Handler;

import com.phicomm.iot.library.device.BaseDevice;
import com.phicomm.iot.library.device.SmartDevice;

/**
 * Created by allen.z on 2017-05-04.
 */
public class SmartSwitcher extends SmartDevice implements SwitchInterface.IListener {

    SwitchInterface.ISwitcher switcherProtocol;
    SwitcherStateListener mListener;
    Handler mHandler;

    public SmartSwitcher(BaseDevice device) {
        super(device);

        if(device.getIsLocalAddress()) {
            switcherProtocol = new EspSwitcherService(getAddress(), this);
        }else{
            switcherProtocol  = new EspSwitcherNetService(getToken(), this);
        }
        mHandler = new Handler();
    }

    void runOnMainThread(Runnable runnable) {
        mHandler.post(runnable);
    }

    public void open() {
        switcherProtocol.start();
        qureyStatus();
    }

    public void close() {
        switcherProtocol.stop();
    }

    private void refresh(){
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                qureyStatus();
            }
        });
    }

    public void setStateChangeListener(SwitcherStateListener listener) {
        mListener = listener;
    }

    public void turnOn() {
        switcherProtocol.turnOn(new SwitchInterface.OperateFinish() {
            public void OnFinish() {
                refresh();
            }
        });
    }

    public void turnOff() {
        switcherProtocol.turnOff(new SwitchInterface.OperateFinish() {
            public void OnFinish() {
                refresh();
            }
        });
    }

    public void qureyStatus() {
        switcherProtocol.qureyStatus();
    }

    @Override
    public void onStatusChange(final boolean on) {
        if (mListener != null) {
            runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    mListener.onStateChange(on);
                }
            });
        }
    }

    public interface SwitcherStateListener {
        void onStateChange(boolean on);
    }
}
