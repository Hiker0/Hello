package com.phicomm.iot.library.discover.JmdnsDiscover;

import android.util.Log;

import com.phicomm.iot.library.device.BaseDevice;
import com.phicomm.iot.library.device.TYPE;
import com.phicomm.iot.library.discover.MeshDiscoveryUtil;
import com.phicomm.iot.library.discover.PhiConstants;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jmdns.JmDNS;

import static java.lang.Thread.sleep;

/**
 * Created by chunya02.li on 2017/5/15.
 */

public class JmdnsDiscoveryUtil implements Runnable {
    private static final String TAG = "MdnsDiscoverUtil";
    MeshDiscoveryUtil mMeshUti;
    List<BaseDevice> mBaseDeviceList;

    public JmdnsDiscoveryUtil(MeshDiscoveryUtil meshUtil) {
        mMeshUti = meshUtil;
    }

    @Override
    public void run() {
        discoverJmdnsDevices();
    }

    private BaseDevice parseSeviceInfo2SampleIotAddres(javax.jmdns.ServiceInfo serviceInfo) {
        byte[] textBytes = serviceInfo.getTextBytes();
        Log.d(TAG, "parseSeviceInfo2SampleIotAddres(): textBytes toString: " + new String(textBytes));
        // check whether the serviceInfo is valid
        int index = 0;
        while (index < textBytes.length) {
            index += (1 + textBytes[index]);
        }
        if (index != textBytes.length) {
            Log.d(TAG, "parseSeviceInfo2SampleIotAddres(): bad serviceInfo format, return null");
            return null;
        }
        // get InetAddress
        InetAddress inetAddress = null;
        inetAddress = serviceInfo.getInet4Addresses()[0];
        // get bssid, device type, device version
        Map<String, String> keyValue = new HashMap<String, String>();
        index = 0;
        String part = null;
        String[] subParts = null;
        while (index < textBytes.length) {
            part = new String(textBytes, index + 1, textBytes[index]);
            subParts = part.split("=");
            // if the content is invalid, just ignore it
            if (subParts.length == 2) {
                keyValue.put(subParts[0], subParts[1]);
            }
            index += (1 + textBytes[index]);
        }
        String bssid = keyValue.get(PhiConstants.KEY_BSSID);
        String type = keyValue.get(PhiConstants.KEY_TYPE);
        if (bssid == null || type == null) {
            Log.d(TAG, "parseSeviceInfo2SampleIotAddres(): bssid = null or type = null, return null");
            return null;
        }
        TYPE devicetype= TYPE.getTypeEnumByString(type);
        BaseDevice mBaseDevice = new BaseDevice(devicetype,String.valueOf(inetAddress),bssid);
        return mBaseDevice;
    }

    private List<BaseDevice> parseSeviceInfoArray2mBaseDeviceList(javax.jmdns.ServiceInfo[] serviceInfoArray) {
        if (serviceInfoArray == null) {
            return Collections.emptyList();
        }
        List<BaseDevice> mBaseDeviceList = new ArrayList<BaseDevice>();
        BaseDevice mBaseDevice = null;
        for (javax.jmdns.ServiceInfo serviceInfo : serviceInfoArray) {
            mBaseDevice = parseSeviceInfo2SampleIotAddres(serviceInfo);
            if (mBaseDevice != null && !mBaseDeviceList.contains(mBaseDevice)) {
                mBaseDeviceList.add(mBaseDevice);
            }
        }
        if (mBaseDeviceList.isEmpty()) {
            return Collections.emptyList();
        } else {
            return mBaseDeviceList;
        }
    }

    private List<BaseDevice> discoverJmdnsDevices() {
        try {
            final JmDNS mdnsService = JmDNS.create();
            Log.d(TAG, "discoverJmdnsDevices() JmDNS.create() finished");
            javax.jmdns.ServiceInfo[] serviceInfoArray = mdnsService.list(PhiConstants.IOT_SERVICE_TYPE_JMDNS, PhiConstants.LIST_TIMEOUT);
            Log.d(TAG, "serviceInfoArray.size=" + serviceInfoArray.length);
            mBaseDeviceList = parseSeviceInfoArray2mBaseDeviceList(serviceInfoArray);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        sleep(3000);
                        mdnsService.close();
                    } catch (IOException igrnoeException) {
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMeshUti.notifyDeviceResultAdd(mBaseDeviceList);
        return mBaseDeviceList;
    }
}