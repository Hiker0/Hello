package com.phicomm.iot.library.discover.broadcast;

import com.phicomm.iot.library.device.BaseDevice;
import com.phicomm.iot.library.device.TYPE;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Johnson on 2017-04-13.
 */

public class EspressMessage {
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

    static String getQureyData(){
        return data;
    }

    static String  getAnserData(TYPE type, String bssid, String addr){

        return  "I'm "+type.toString()+"."+bssid+" "+addr;
    }
    static InetAddress getAddress(){
        return broadcastAddress;
    }
}
