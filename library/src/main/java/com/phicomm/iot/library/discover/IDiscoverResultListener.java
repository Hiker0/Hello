package com.phicomm.iot.library.discover;

import com.phicomm.iot.library.device.BaseDevice;

import java.util.List;

/**
 * Created by chunya02.li on 2017/5/15.
 */

public interface IDiscoverResultListener {
    void onDeviceResultAdd(List<BaseDevice> devlist);
}
