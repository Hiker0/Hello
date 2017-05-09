package com.phicomm.demo.devices;

import com.phicomm.demo.data.Device;
import com.phicomm.demo.devices.DevicesContract.View;

import java.util.LinkedList;
import java.util.List;

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
        loadDevices();
    }

    @Override
    public void loadDevices() {
        List<Device> devices = new LinkedList<>();
        devices.add(new Device("XX:XX:XX:XX"));
        devices.add(new Device("YY:YY:XX:XX"));

        if (!devices.isEmpty()) {
            mDevicesView.showDevices(devices);
        } else {
//            mDevicesView.withoutDevices();
            // TODO: 17-5-9 增加无设备显示
        }

    }
}
