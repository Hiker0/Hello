package com.phicomm.demo.devices;

import com.phicomm.demo.BasePresenter;
import com.phicomm.demo.BaseView;
import com.phicomm.demo.data.Device;
import com.phicomm.iot.library.device.IIotDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzn on 17-5-9.
 */

public interface DevicesContract {
    interface View extends BaseView<Presenter> {
        void showDevices(List<Device> devices);

        void showDeviceDetailsUI(Device device);
    }

    interface Presenter extends BasePresenter {
        void loadDevices();

        void handleIotAddress(ArrayList<IIotDevice> iotAddresses);

        void openDeviceDetails(Device device);
    }
}
