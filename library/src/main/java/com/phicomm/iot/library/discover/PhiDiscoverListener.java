package com.phicomm.iot.library.discover;

/**
 * Created by Johnson on 2017-04-13.
 */

public interface PhiDiscoverListener {
    void onDeviceAdd(DiscoveredDevice dev);
    void onDeviceRemove(DiscoveredDevice dev);
}
