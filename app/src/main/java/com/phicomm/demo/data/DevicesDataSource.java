package com.phicomm.demo.data;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by hzn on 17-5-10.
 */

public interface DevicesDataSource {
    void getDevices(@NonNull LoadDevicesCallback callback);

    interface LoadDevicesCallback {
        void onDeviceLoaded(List<Device> devices);

        void onDataNotAvailable();
    }
}
