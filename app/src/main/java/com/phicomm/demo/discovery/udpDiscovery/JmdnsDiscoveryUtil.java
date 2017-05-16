package com.phicomm.demo.discovery.udpDiscovery;

import android.util.Log;

import com.phicomm.demo.discovery.MeshDiscoveryUtil;
import com.phicomm.demo.discovery.SampleIotAddress;

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
    private static final String TAG = "lichunya MdnsDiscoverUtil";
    MeshDiscoveryUtil mMeshUti;
    List<SampleIotAddress> mSampleIotAddressList;

    public JmdnsDiscoveryUtil(MeshDiscoveryUtil meshUtil) {
        mMeshUti = meshUtil;
    }

    @Override
    public void run() {
        discoverJmdnsDevices();
    }

    private SampleIotAddress parseSeviceInfo2SampleIotAddres(javax.jmdns.ServiceInfo serviceInfo) {
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
        String bssid = keyValue.get(ContantString.KEY_BSSID);
        String type = keyValue.get(ContantString.KEY_TYPE);
        if (bssid == null || type == null) {
            Log.d(TAG, "parseSeviceInfo2SampleIotAddres(): bssid = null or type = null, return null");
            return null;
        }
        SampleIotAddress mSampleIotAddress = new SampleIotAddress(bssid, type, inetAddress);
        return mSampleIotAddress;
    }

    private List<SampleIotAddress> parseSeviceInfoArray2mSampleIotAddressList(javax.jmdns.ServiceInfo[] serviceInfoArray) {
        if (serviceInfoArray == null) {
            return Collections.emptyList();
        }
        List<SampleIotAddress> mSampleIotAddressList = new ArrayList<SampleIotAddress>();
        SampleIotAddress mSampleIotAddress = null;
        for (javax.jmdns.ServiceInfo serviceInfo : serviceInfoArray) {
            mSampleIotAddress = parseSeviceInfo2SampleIotAddres(serviceInfo);
            if (mSampleIotAddress != null && !mSampleIotAddressList.contains(mSampleIotAddress)) {
                mSampleIotAddressList.add(mSampleIotAddress);
            }
        }
        if (mSampleIotAddressList.isEmpty()) {
            return Collections.emptyList();
        } else {
            return mSampleIotAddressList;
        }
    }

    private List<SampleIotAddress> discoverJmdnsDevices() {
        try {
            final JmDNS mdnsService = JmDNS.create();
            Log.d(TAG, "discoverJmdnsDevices() JmDNS.create() finished");
            javax.jmdns.ServiceInfo[] serviceInfoArray = mdnsService.list(ContantString.IOT_SERVICE_TYPE_JMDNS, ContantString.LIST_TIMEOUT);
            Log.d(TAG, "serviceInfoArray.size=" + serviceInfoArray.length);
            mSampleIotAddressList = parseSeviceInfoArray2mSampleIotAddressList(serviceInfoArray);
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
        mMeshUti.notifyDeviceResultAdd(mSampleIotAddressList);
        return mSampleIotAddressList;
    }
}
