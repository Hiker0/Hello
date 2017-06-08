package com.phicomm.iot.library.discover.internetDiscover;

import com.phicomm.iot.library.device.IIotDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by chunya02.li on 2017/6/8.
 */

public class InternetDevicesDiscovery {

    private static InternetDevicesDiscovery instance;

    private IInternetDiscoverResultListener mInternetDiscoverResultListener;
    private PhiCommandDeviceSynchronizeInternet mSynchronizeAction;
    private ArrayList<IIotDevice> mInternetList;

    public static synchronized InternetDevicesDiscovery getInstance() {
        if (instance == null) {
            instance = new InternetDevicesDiscovery();
        }
        return instance;
    }

    private InternetDevicesDiscovery() {
    }

    public void startInternetDiscovery(final String userkey){
        Callable<ArrayList<IIotDevice>> taskInternet = null;
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<ArrayList<IIotDevice>> futureInternet = null;
        taskInternet = new Callable<ArrayList<IIotDevice>>() {
            @Override
            public ArrayList<IIotDevice> call() throws Exception {
                return doCommandSynchronizeInternet(userkey);
            }
        };
        futureInternet = executor.submit(taskInternet);
        try {
            mInternetList = futureInternet.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (mInternetList != null && mInternetDiscoverResultListener != null) {
            mInternetDiscoverResultListener.onDeviceResultAdd(mInternetList);
        }
        executor.shutdown();
    }

    private ArrayList<IIotDevice> doCommandSynchronizeInternet(String userKey) {
        mSynchronizeAction = new PhiCommandDeviceSynchronizeInternet();
        return mSynchronizeAction.doCommandSynchronizeInternet(userKey);
    }

    public void setInternetDiscoverResultListener(IInternetDiscoverResultListener internetDiscoverResultListener) {
        mInternetDiscoverResultListener = internetDiscoverResultListener;
    }


    public interface IInternetDiscoverResultListener {
        void onDeviceResultAdd(List<IIotDevice> devlist);
    }


}
