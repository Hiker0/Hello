package com.phicomm.demo.devices;

import com.phicomm.demo.devices.DevicesContract.View;

/**
 * Created by hzn on 17-5-9.
 */

public class DevicesPresenter implements DevicesContract.Presenter {
    private View mDevicesView;

    public DevicesPresenter(View view) {
        mDevicesView = view;
        mDevicesView.setPresenter(this);
    }

    @Override
    public void start() {
        mDevicesView.hello();
    }
}
