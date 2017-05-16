package com.phicomm.demo.discovery.udpDiscovery;

import com.phicomm.demo.discovery.SampleIotAddress;

import java.util.List;

/**
 * Created by chunya02.li on 2017/5/15.
 */

public interface IDiscoverResultListener {
    void onDeviceResultAdd(List<SampleIotAddress> devlist);
}
