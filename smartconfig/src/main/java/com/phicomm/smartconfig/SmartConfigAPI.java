package com.phicomm.smartconfig;

import android.content.Context;
import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchListener;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.IEsptouchTask;
import com.espressif.iot.esptouchB.EsptouchTaskB;
import com.espressif.iot.esptouchB.IEsptouchListenerB;
import com.espressif.iot.esptouchB.IEsptouchResultB;
import com.espressif.iot.esptouchB.IEsptouchTaskB;

import java.util.List;

/**
 * Created by hanyuan.chen on 2017/4/13.
 */

public class SmartConfigAPI implements ISmartConfigAPI {

    private final Object mLock = new Object();
    private ISmartConfigTask mSmartConfigTask;
    private IEsptouchTask mEsptouchTaskA;
    private IEsptouchTaskB mEsptouchTaskB;
    private String apSsid;
    private String apBssid;
    private String apPassword;
    private boolean isSsidHidden;
    private int taskResultCount;
    private Context context;
    public static int EXTRA_LEN = 40;
    public static int mTargetPort = 7001;

    public SmartConfigAPI(String apSsid, String apBssid, String apPassword,
                          boolean isSsidHidden, int taskResultCount, Context context) {
        this.apSsid = apSsid;
        this.apBssid = apBssid;
        this.apPassword = apPassword;
        this.isSsidHidden = isSsidHidden;
        this.taskResultCount = taskResultCount;
        this.context = context;
    }

    public Boolean addDevicesSyn(ISmartConfigListener mSmartConfigListener) {
        synchronized (mLock) {
            mSmartConfigTask = new SmartConfigTask(apSsid, apBssid, apPassword,
                    isSsidHidden, context);
            mSmartConfigTask.setEsptouchListener(mSmartConfigListener);
        }
        List<ISmartConfigResult> resultList = mSmartConfigTask.executeForResults(taskResultCount);
        ISmartConfigResult firstResult = resultList.get(0);
        if (!firstResult.isCancelled()) {
            if (firstResult.isSuc()) {
                return true;
            }
        }
        return false;
    }

    public Boolean addDevicesSyn(ISmartConfigListener mSmartConfigListener, int waitUdpTotalMillisecond) {
        synchronized (mLock) {
            mSmartConfigTask = new SmartConfigTask(apSsid, apBssid, apPassword,
                    isSsidHidden, waitUdpTotalMillisecond, context);
            mSmartConfigTask.setEsptouchListener(mSmartConfigListener);
        }
        List<ISmartConfigResult> resultList = mSmartConfigTask.executeForResults(taskResultCount);
        ISmartConfigResult firstResult = resultList.get(0);
        if (!firstResult.isCancelled()) {
            if (firstResult.isSuc()) {
                return true;
            }
        }
        return false;
    }

    public Boolean addDevicesSynA(IEsptouchListener mEsptouchListener) {
        synchronized (mLock) {
            mEsptouchTaskA = new EsptouchTask(apSsid, apBssid, apPassword,
                    isSsidHidden, context);
            mEsptouchTaskA.setEsptouchListener(mEsptouchListener);
        }
        List<IEsptouchResult> resultList = mEsptouchTaskA.executeForResults(taskResultCount);
        IEsptouchResult firstResult = resultList.get(0);
        if (!firstResult.isCancelled()) {
            if (firstResult.isSuc()) {
                return true;
            }
        }
        return false;
    }

    public Boolean addDevicesSynA(IEsptouchListener mEsptouchListener, int waitUdpTotalMillisecond) {
        synchronized (mLock) {
            mEsptouchTaskA = new EsptouchTask(apSsid, apBssid, apPassword,
                    isSsidHidden, waitUdpTotalMillisecond, context);
            mEsptouchTaskA.setEsptouchListener(mEsptouchListener);
        }
        List<IEsptouchResult> resultList = mEsptouchTaskA.executeForResults(taskResultCount);
        IEsptouchResult firstResult = resultList.get(0);
        if (!firstResult.isCancelled()) {
            if (firstResult.isSuc()) {
                return true;
            }
        }
        return false;
    }

    public Boolean addDevicesSynB(IEsptouchListenerB mEsptouchListener) {
        synchronized (mLock) {
            mEsptouchTaskB = new EsptouchTaskB(apSsid, apBssid, apPassword,
                    isSsidHidden, context);
            mEsptouchTaskB.setEsptouchListener(mEsptouchListener);
        }
        List<IEsptouchResultB> resultList = mEsptouchTaskB.executeForResults(taskResultCount);
        IEsptouchResultB firstResult = resultList.get(0);
        if (!firstResult.isCancelled()) {
            if (firstResult.isSuc()) {
                return true;
            }
        }
        return false;
    }

    public Boolean addDevicesSynB(IEsptouchListenerB mEsptouchListener, int waitUdpTotalMillisecond) {
        synchronized (mLock) {
            mEsptouchTaskB = new EsptouchTaskB(apSsid, apBssid, apPassword,
                    isSsidHidden, waitUdpTotalMillisecond, context);
            mEsptouchTaskB.setEsptouchListener(mEsptouchListener);
        }
        List<IEsptouchResultB> resultList = mEsptouchTaskB.executeForResults(taskResultCount);
        IEsptouchResultB firstResult = resultList.get(0);
        if (!firstResult.isCancelled()) {
            if (firstResult.isSuc()) {
                return true;
            }
        }
        return false;
    }

    public void interrupt() {
        if (mSmartConfigTask != null) {
            mSmartConfigTask.interrupt();
        }
    }

    public void interruptA() {
        if (mEsptouchTaskA != null) {
            mEsptouchTaskA.interrupt();
        }
    }

    public void interruptB() {
        if (mEsptouchTaskB != null) {
            mEsptouchTaskB.interrupt();
        }
    }

    public void setEXTRA_LEN(int len) {
        this.EXTRA_LEN = len;
    }

    public void setTargetPort(int port) {
        this.mTargetPort = port;
    }

}
