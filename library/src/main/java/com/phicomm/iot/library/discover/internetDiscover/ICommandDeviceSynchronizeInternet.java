package com.phicomm.iot.library.discover.internetDiscover;

import com.phicomm.iot.library.device.IIotDevice;

import java.util.ArrayList;

/**
 * Created by chunya02.li on 2017/5/27.
 */

public interface ICommandDeviceSynchronizeInternet {
    public  static final String URL = "https://iot.espressif.cn/v1/user/devices/";
    public static final String Devices = "devices";


    /**
     * synchronize the user's device from the Server
     *
     * @return the group(which contains device list) list of the user
     */
    ArrayList<IIotDevice> doCommandSynchronizeInternet(String userKey);

    ArrayList<IIotDevice> getInternetDeviceList();
}

