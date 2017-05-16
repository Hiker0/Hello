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
    public static final long LIST_TIMEOUT = 2000;
    public static final int RECEIVE_LEN = 64;
    public static final String KEY_BSSID = "bssid";
    public static final String KEY_TYPE = "type";
    public static final String IOT_SERVICE_TYPE_JMDNS = "_http._tcp.local.";
    public static final boolean bIsUsingJMDNS = true;
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
