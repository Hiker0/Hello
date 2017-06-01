package com.phicomm.iot.library.discover;

import android.util.Log;

import com.phicomm.iot.library.device.BaseDevice;
import com.phicomm.iot.library.device.IIotDevice;
import com.phicomm.iot.library.discover.JmdnsDiscover.JmdnsDiscoveryUtil;
import com.phicomm.iot.library.discover.udpDiscover.UdpDiscoveryUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chunya02.li on 2017/5/15.
 */

public class MeshDiscoveryUtil implements Runnable {
    private List<BaseDevice> mFinalDeviceList = new ArrayList<>();
    private Map<String, IIotDevice> mCachedIotAddress;
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
        mCachedIotAddress = new HashMap<>(0);

    }

    public void notifyDeviceResultAdd(List<BaseDevice> devList) {
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
        mCachedIotAddress.clear();
        if (mUdpDiscoveryThread != null) {
            mUdpDiscoveryThread = null;
        }
        if (mJmdnsDiscoveryThread != null) {
            mJmdnsDiscoveryThread = null;
        }
        startUdpDiscovery();
        if (PhiConstants.bIsUsingJMDNS) {
            startJmdnsDiscovery();
        }
        /*if(PhiConstants.bIsUserLogin) {
            startSynchronizeInternet();
        }*/
    }

    /* public void startSynchronizeInternet() {
        Callable<List<BaseDevice>> taskInternet = null;
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<List<BaseDevice>> futureInternet = null;
        addDiscoveryListener(mDiscoverResultListener);
        if (PhiConstants.bIsUserLogin) {
            taskInternet = new Callable<List<BaseDevice>>() {
                @Override
                public List<BaseDevice> call() throws Exception {
                    return doCommandSynchronizeInternet(PhiConstants.UserKey);
                }

            };
            futureInternet = executor.submit(taskInternet);
            try {
                notifyDeviceResultAdd(futureInternet.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            executor.shutdown();
        }
    }

      private List<BaseDevice> doCommandSynchronizeInternet(String userkey) {
           ICommandDeviceSynchronizeInternet action = new PhiCommandDeviceSynchronizeInternet();
           return action.doCommandSynchronizeInternet(userkey);
       }
   */
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
        public void onDeviceResultAdd(List<BaseDevice> resultList) {
            for (BaseDevice dev : resultList) {
                String mBssid = dev.getBssid();
                Log.d(TAG, "dev.mBssid=" + mBssid + "mFinalDeviceList.size()=" + mFinalDeviceList.size());
                if (mCachedIotAddress.containsKey(mBssid)) {
                    continue;
                } else {
                    mCachedIotAddress.put(mBssid, dev);
                    mFinalDeviceList.add(dev);
                }
            }
        }
    };

    @Override
    public void run() {
        try {
            discoverIOTDevices();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
