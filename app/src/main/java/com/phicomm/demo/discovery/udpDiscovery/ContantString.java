package com.phicomm.demo.discovery.udpDiscovery;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by chunya02.li on 2017/5/11.
 */

public class ContantString {
    public static final String data = "Are You Espressif IOT Smart Device?";
    public static final int IOT_DEVICE_PORT = 1025 ;
    public static final int IOT_APP_PORT = 4025;
    public static final int SO_TIMEOUT = 3000;
    public static final int RECEIVE_LEN = 64;
    public static InetAddress broadcastAddress;

    static {
        try {
            broadcastAddress = InetAddress.getByName("255.255.255.255");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * if the IOT_APP_PORT is occupied, other random port will be used
     */
}
