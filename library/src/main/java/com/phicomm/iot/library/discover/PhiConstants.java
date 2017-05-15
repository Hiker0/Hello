package com.phicomm.iot.library.discover;

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
}
