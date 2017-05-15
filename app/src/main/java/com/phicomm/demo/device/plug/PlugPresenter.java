package com.phicomm.demo.device.plug;

import com.phicomm.demo.device.plug.PlugContract.View;
import com.phicomm.iot.library.device.BaseDevice;

/**
 * Created by hzn on 17-5-11.
 */

public class PlugPresenter implements PlugContract.Presenter {
    public View mPlugView;
    private BaseDevice mPlug;

    private boolean mIsOn = false;

    public PlugPresenter(View view) {
        mPlugView = view;
        mPlugView.setPresenter(this);
    }

    @Override
    public void start() {
        mPlugView.showState(mIsOn);
    }

    @Override
    public void stop() {

    }

    public void setPlug(BaseDevice plug) {
        mPlug = plug;
    }

    @Override
    public void switchState() {
        mIsOn = !mIsOn;
        mPlugView.showState(mIsOn);
    }
}
