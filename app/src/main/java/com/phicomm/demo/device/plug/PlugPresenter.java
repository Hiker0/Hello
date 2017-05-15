package com.phicomm.demo.device.plug;

import com.phicomm.demo.data.Device;
import com.phicomm.demo.device.plug.PlugContract.View;

/**
 * Created by hzn on 17-5-11.
 */

public class PlugPresenter implements PlugContract.Presenter {
    public View mPlugView;
    private Device mPlug;

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

    public void setPlug(Device plug) {
        mPlug = plug;
    }

    @Override
    public void switchState() {
        mIsOn = !mIsOn;
        mPlugView.showState(mIsOn);
    }
}
