package com.phicomm.discoverdevice.discoverlibrary;

import java.util.List;

/**
 * Created by chunya02.li on 2017/5/15.
 */

public interface IDiscoverResultListener {
    void onDeviceResultAdd(List<PhiIotDevice> devlist);
}
