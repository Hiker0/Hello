package com.phicomm.demo.discovery.udpDiscovery;

import android.util.Log;

import com.phicomm.demo.discovery.SampleIotAddress;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by chunya02.li on 2017/5/11.
 */

public class UdpDataParser {
    // "I'm Plug.18:fe:34:9a:b1:4c 192.168.100.126"
    // "I'm Plug with mesh.18:fe:34:9a:b1:4c 192.168.100.126"

    // '^' beginning sign
    // '\\w' word symbol containing '_'
    // '\\.' .
    // '\\d' 0-9
    // '$' ending sign

    private static final String DEVICE_PATTERN_TYPE = "^I'm ((\\w)+( )*)+\\.";
    private static final String DEVICE_PATTERN_BSSID = "([0-9a-fA-F]{2}:){5}([0-9a-fA-F]{2} )";
    private static final String DEVICE_PATTERN_IP = "(\\d+\\.){3}(\\d+)";

    private static final String DEVICE_PATTERN = DEVICE_PATTERN_TYPE + DEVICE_PATTERN_BSSID + DEVICE_PATTERN_IP;

    private static String TAG = "lichunya UdpDataParser";

    /**
     * check whether the data is valid
     *
     * @param data the content String get from UDP Broadcast
     * @return whether the data is valid
     */
    public static boolean isValid(String data) {
        return (data.matches(DEVICE_PATTERN));
    }

    /**
     * filter the device's type String from data
     *
     * @param data the content String get from UDP Broadcast
     * @return the device's type String
     */
    public static String filterType(String data) {
        String[] dataSplitArray = data.split("\\.");
        return dataSplitArray[0].split(" ")[1];
    }

    /**
     * filter the device's bssid from data
     *
     * @param data the content String get from UDP Broadcast
     * @return the device's bssid String
     */
    public static String filterBssid(String data) {
        String[] dataSplitArray = data.split("\\.");
        return dataSplitArray[1].split(" ")[0];
    }

    /**
     * filter the device's ip address from data
     *
     * @param data the content String get from UDP Broadcast
     * @return the device's ip address String
     */
    public static String filterIpAddress(String data) {
        String[] dataSplitArray = data.split(" ");
        return dataSplitArray[dataSplitArray.length - 1];
    }

    public static SampleIotAddress parsePackage(DatagramPacket packet) throws UnknownHostException {
        String receiveContent = new String(packet.getData(), packet.getOffset(), packet.getLength()).trim();
        String hostname = null;
        InetAddress responseAddr = null;
        String responseBSSID = null;
        SampleIotAddress mSampleIotAddress = null;
        Log.d(TAG,"receiveContent="+receiveContent);
        if (UdpDataParser.isValid(receiveContent)) {
            String deviceTypeStr = UdpDataParser.filterType(receiveContent);
            if (deviceTypeStr == null) {
                Log.d(TAG, " type is null or the type of the device don't support local mode.");
            } else {
                hostname = UdpDataParser.filterIpAddress(receiveContent);
                if (hostname.equals("0.0.0.0")) {
                    return mSampleIotAddress;
                }
                responseAddr = InetAddress.getByName(hostname);
                Log.d(TAG, receiveContent);
                responseBSSID = UdpDataParser.filterBssid(receiveContent);
                mSampleIotAddress = new SampleIotAddress(responseBSSID, deviceTypeStr, responseAddr);
            }
        }
        return mSampleIotAddress;
    }
}
