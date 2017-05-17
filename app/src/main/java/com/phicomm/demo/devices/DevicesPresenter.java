package com.phicomm.demo.devices;

import android.support.annotation.NonNull;

import com.phicomm.demo.data.Device;
import com.phicomm.demo.data.DevicesDataSource.LoadDevicesCallback;
import com.phicomm.demo.data.DevicesRepository;
import com.phicomm.demo.devices.DevicesContract.View;
import com.phicomm.discoverdevice.discoverlibrary.IIotDevice;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class DevicesPresenter implements DevicesContract.Presenter {
    private View mDevicesView;
    private DevicesRepository mDevicesRepository;

    public DevicesPresenter(@NonNull View view, @NonNull DevicesRepository repository) {
        mDevicesView = checkNotNull(view);
        mDevicesView.setPresenter(this);

        mDevicesRepository = checkNotNull(repository);
    }

    @Override
    public void start() {
        loadDevices();
    }

    @Override
    public void stop() {

    }

    @Override
    public void loadDevices() {
        mDevicesRepository.getDevices(new LoadDevicesCallback() {
            @Override
            public void onDeviceLoaded(List<Device> devices) {
                mDevicesView.showDevices(devices);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void handleIotAddress(ArrayList<IIotDevice> iotAddresses) {
        mDevicesRepository.syncIotAddresses(iotAddresses);
        loadDevices();
    }

    @Override
    public void openDeviceDetails(Device device) {
        mDevicesView.showDeviceDetailsUI(device);
    }
}
