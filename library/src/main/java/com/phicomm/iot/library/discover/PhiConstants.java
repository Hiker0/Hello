package com.phicomm.iot.library.discover;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Johnson on 2017-04-13.
 */

public class PhiConstants {

    public final static int MAX_MSG_ABSOLUTE = 512;

    public final static int MAX_ANOUNCE_NUM= 3;

    public final static int STATE_CANCELED = 0;
    public final static int STATE_ANOUNCE = 1;
    public final static int STATE_ANOUNCED = 2;

    public final static int DEVICE_GROUP_PORT = 2332;
    public final static String DEVICE_GROUP_ADDRESS = "224.0.0.251";

    public final static long RENEWAL_TTL_INTERVAL=1000*6;
    public final static long ANOUNCER_INTERVAL=500;
	
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
    public static final String UserKey = "ca52829f37466da790e2a5b11f4e79cfac395646";
    public static InetAddress broadcastAddress;

    static {
        try {
            broadcastAddress = InetAddress.getByName("255.255.255.255");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
