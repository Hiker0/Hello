package com.phicomm.demo.devices;

import com.phicomm.demo.data.Device;
import com.phicomm.demo.devices.DevicesContract.View;
import com.phicomm.demo.discovery.IIotAddress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hzn on 17-5-9.
 */

public class DevicesPresenter implements DevicesContract.Presenter {
    private View mDevicesView;
    private List<IIotAddress> mCachedAddresses = new ArrayList<>(0);
    private Map<String, Device> mCachedDevices = new HashMap<>();

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
//        List<Device> devices = new LinkedList<>();
//        devices.add(new Device("XX:XX:XX:XX"));
//        devices.add(new Device("YY:YY:XX:XX"));
        List<Device> devices = new ArrayList<>(mCachedDevices.values());

        if (!devices.isEmpty()) {
            mDevicesView.showDevices(devices);
        } else {
//            mDevicesView.withoutDevices();
            // TODO: 17-5-9 增加无设备显示
        }

    }

    @Override
    public void handleIotAddress(ArrayList<IIotAddress> iotAddresses) {
        mCachedAddresses = iotAddresses;

        for (IIotAddress address : iotAddresses) {
            if (mCachedDevices.containsKey(address.getBSSID())) {
                Device device = mCachedDevices.get(address.getBSSID());
                // TODO: 17-5-10 更新设备的局域网地址信息
            } else {
                Device device = Device.from(address);
                mCachedDevices.put(address.getBSSID(), device);
            }
        }

        loadDevices();
    }
}
