package com.phicomm.demo.device.switcher;

import com.phicomm.demo.data.Device;
import com.phicomm.demo.device.switcher.SwitcherContract.View;
import com.phicomm.iot.library.device.BaseDevice;
import com.phicomm.iot.library.device.switcher.SmartSwitcher;

/**
 * Created by hzn on 17-5-11.
 */

public class SwitcherPresenter implements SwitcherContract.Presenter {
    public View mSwitcherView;
    private Device mDevice;
    private SmartSwitcher mSwitcher;

    private boolean mIsOn = false;

    public SwitcherPresenter(View view, Device device) {
        mSwitcherView = view;
        mSwitcherView.setPresenter(this);

        mDevice = device;
        BaseDevice dev = new BaseDevice();
        dev.setAddress(device.getLocalAddress().getHostAddress());
        dev.setBssid(device.getBssid());
        dev.setTypeName(device.getType());
        dev.setBrand("phicomm");
        mSwitcher = new SmartSwitcher(dev);
        mSwitcher.setStateChangeListener(new SmartSwitcher.SwitcherStateListener() {
            @Override
            public void onStateChange(boolean on) {
                mSwitcherView.notifyState(on);
            }
        });
    }

    @Override
    public void start() {
        mSwitcher.open();
        if(mIsOn) {
            mSwitcherView.notifyState(mIsOn);
        }
    }


    public void stop(){
        mSwitcher.close();
    }

    @Override
    public void turnOn() {
        mSwitcher.turnOn();
    }

    @Override
    public void turnOff() {
        mSwitcher.turnOff();
    }

    @Override
    public boolean isOn() {
        return false;
    }
}
