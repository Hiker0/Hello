package com.phicomm.discoverdevice.discoverlibrary;

import android.util.Log;

import com.phicomm.discoverdevice.discoverlibrary.JmdnsDiscover.JmdnsDiscoveryUtil;
import com.phicomm.discoverdevice.discoverlibrary.udpDiscover.UdpDiscoveryUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by chunya02.li on 2017/5/15.
 */

public class MeshDiscoveryUtil {
    private List<PhiIotDevice> mFinalDeviceList = new ArrayList<>();
    private UdpDiscoveryUtil mUdpDiscover;
    private JmdnsDiscoveryUtil mJmdnsDiscover;

    private List<IDiscoverResultListener> mDiscoverListeners;
    private IDiscoverResultListener mMeshDiscoverListeners;

    private Thread mUdpDiscoveryThread;
    private Thread mJmdnsDiscoveryThread;
    private static String TAG = "MeshDiscoveryUtil";


    public MeshDiscoveryUtil() {
        mDiscoverListeners = Collections.synchronizedList(new ArrayList());
        mUdpDiscover = new UdpDiscoveryUtil(this);
        mJmdnsDiscover = new JmdnsDiscoveryUtil(this);

    }

    public void notifyDeviceResultAdd(List<PhiIotDevice> devList) {
        Log.d(TAG, "mDiscoverListeners.size()=" + mDiscoverListeners.size() + "devList.size=" + devList.size());
        for (IDiscoverResultListener listener : mDiscoverListeners) {
            listener.onDeviceResultAdd(devList);
        }
        //return total result to discoverService
        if (mMeshDiscoverListeners != null) {
            mMeshDiscoverListeners.onDeviceResultAdd(mFinalDeviceList);
        }
    }

    public void setMeshDiscoverResultListener(IDiscoverResultListener meshDiscoverResultListener) {
        mMeshDiscoverListeners = meshDiscoverResultListener;
    }

    public void discoverIOTDevices() throws Exception {
        Log.d(TAG, "discoverIOTDevices");
        mFinalDeviceList.clear();
        mDiscoverListeners.clear();
        if (mUdpDiscoveryThread != null) {
            mUdpDiscoveryThread = null;
        }
        if (mJmdnsDiscoveryThread != null) {
            mJmdnsDiscoveryThread = null;
        }
        startUdpDiscovery();
        if (ContantString.bIsUsingJMDNS) {
            startJmdnsDiscovery();
        }
    }

    private void startUdpDiscovery() throws Exception {
        addDiscoveryListener(mDiscoverResultListener);
        mUdpDiscoveryThread = new Thread(mUdpDiscover, "UdpDiscover");
        mUdpDiscoveryThread.start();
    }

    private void startJmdnsDiscovery() throws Exception {
        addDiscoveryListener(mDiscoverResultListener);
        mJmdnsDiscoveryThread = new Thread(mJmdnsDiscover, "JmdnsDiscover");
        mJmdnsDiscoveryThread.start();
    }

    public void addDiscoveryListener(IDiscoverResultListener listener) {
        mDiscoverListeners.add(listener);
    }

    public void removeServiceListener(IDiscoverResultListener listener) {
        mDiscoverListeners.remove(listener);
    }

    private IDiscoverResultListener mDiscoverResultListener = new IDiscoverResultListener() {
        @Override
        public void onDeviceResultAdd(List<PhiIotDevice> resultList) {
            Log.d(TAG,"resultList.size="+resultList);
            for (PhiIotDevice dev : resultList) {
                Log.d(TAG,"dev="+dev);
                if (mFinalDeviceList.contains(dev)) {
                    continue;
                } else {
                    mFinalDeviceList.add(dev);
                }
            }
        }
    };

}
