package com.phicomm.iot.library.discover.broadcast;

import android.os.Handler;
import android.util.Log;

import com.phicomm.iot.library.device.BaseDevice;
import com.phicomm.iot.library.discover.PhiConstants;
import com.phicomm.iot.library.discover.DiscoveredDevice;
import com.phicomm.iot.library.discover.PhiDiscover;
import com.phicomm.iot.library.discover.PhiDiscoverListener;
import com.phicomm.iot.library.discover.PhiDiscoverPackage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Johnson on 2017-04-13.
 */

public class PhiDiscoverBroadcastImpl extends PhiDiscover {

    private final static String TAG = "PhiDiscover";
    private final static boolean DEBUG = true;
    /**
     * This is the multicast group, we are listening to for multicast DNS messages.
     */
    private InetAddress mGroupAddr;

    private MulticastSocket mSocket;
    /**
     * Used to fix live lock problem on unregester.
     */
    private boolean mClosed = false;

    private int mState = PhiConstants.STATE_CANCELED;
    /**
     * Holds instances of ServiceListener's.
     * Keys are Strings holding a fully qualified service type.
     * Values are LinkedList's of ServiceListener's.
     */
    private List<PhiDiscoverListener> mDiscoverListeners;
    private List<DiscoveredDevice> mDeviceList;
    private Thread mListenerThread = null;
    private Timer mTimer;
    private TimerTask mTask;
    private Handler mHandler;


    public PhiDiscoverBroadcastImpl(String addr, int port) throws IOException {
        super(addr, port);
        mDiscoverListeners = Collections.synchronizedList(new ArrayList());
        mDeviceList = Collections.synchronizedList(new ArrayList());
        mTimer = new Timer();
        mHandler = new Handler();
    }

    @Override
    public void startResearchDevice() throws Exception {
        if (mState != PhiConstants.STATE_CANCELED) {
            Log.w(TAG, "PhiDiscover is already running");
            throw new Exception();
        }
        mDeviceList.clear();
        openSocket(mIpAddress, mPort);
        mState = PhiConstants.STATE_ANOUNCE;
        startAnouncher();
        mListenerThread = new Thread(new PhiSocketListener(this), "PhiDiscover.SocketListener");
        mListenerThread.start();
    }

    @Override
    public void cancel() {
        if(mState != PhiConstants.STATE_CANCELED) {
            mState = PhiConstants.STATE_CANCELED;
            closeMulticastSocket();
            mHandler.removeCallbacksAndMessages(null);
            mDiscoverListeners.clear();
            mDeviceList.clear();
            mListenerThread = null;
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
        }
    }

    @Override
    public void reStart() {
        if (PhiConstants.STATE_CANCELED != getState()) {
            synchronized (this) {
                setState(PhiConstants.STATE_CANCELED);
                closeMulticastSocket();
                mDiscoverListeners = null;
                mDeviceList.clear();
                mDeviceList = null;
                mListenerThread = null;
                if (mTask != null) {
                    mTask.cancel();
                    mTask = null;
                }
                try {
                    openSocket(mGroupAddr.getHostAddress(), mPort);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mState = PhiConstants.STATE_ANOUNCE;
                startAnouncher();
                mListenerThread = new Thread(new PhiSocketListener(this), "PhiDiscover.SocketListener");
                mListenerThread.start();
            }
        }
    }
    @Override
    public void addServiceListener(PhiDiscoverListener listener) {
        for (PhiDiscoverListener lis : mDiscoverListeners) {
            if (lis == listener) {
                return;
            }
        }
        mDiscoverListeners.add(listener);
    }

    @Override
    public void removeServiceListener(PhiDiscoverListener listener) {
        mDiscoverListeners.remove(listener);
    }

    @Override
    public List<DiscoveredDevice> getDevices() {
        return mDeviceList;
    }


    public void startAnouncher() {
        if (mTask != null) {
            mTask.cancel();
        }
        Anouncer task = new Anouncer(this);
        task.start(mTimer);
        mTask = task;
    }

    public void startRenewer() {
        if (mTask != null) {
            mTask.cancel();
        }
        Renewer task = new Renewer(this);
        task.start(mTimer);
        mTask = task;
    }

    public void handleQurey(PhiDiscoverPackage phipackage) {
        if(mHostInfo != null) {
            BaseDevice.TYPE type = mHostInfo.getType();
            PhiDiscoverPackage packet = new PhiDiscoverPackage(mHostInfo.getBrand(), type.name(), mHostInfo.getName(), mHostInfo.getBssid());
            send(packet);
        }
    }

    public void handleResponse(PhiDiscoverPackage phipackage) {
        DiscoveredDevice newdev = new DiscoveredDevice(phipackage);
        for (DiscoveredDevice dev : mDeviceList) {
            if (newdev.equals(dev)) {
                dev.resetTTL();
                return;
            }
        }
        mDeviceList.add(newdev);
        notifyDeviceAdd(newdev);
    }

    public void notifyDeviceAdd(final DiscoveredDevice dev) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (PhiDiscoverListener listener : mDiscoverListeners) {
                    listener.onDeviceAdd(dev);
                }
            }
        });
    }

    public void notifyDeviceRemove(final DiscoveredDevice dev) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (PhiDiscoverListener listener : mDiscoverListeners) {
                    listener.onDeviceRemove(dev);
                }
            }
        });
    }

    void send(PhiDiscoverPackage phipacket) {
        byte[] data = phipacket.finish();
        DatagramPacket packet = new DatagramPacket(data, data.length, mGroupAddr, mPort);

        try {
            mSocket.send(packet);
        } catch (IOException e) {
            Log.e(TAG, "send(DNSOutgoing) - JmDNS can not parse what it sends!!!", e);
        }

    }

    private void openSocket(String addr, int port) throws IOException {
        try {
            mGroupAddr = InetAddress.getByName(addr);
            mPort = port;
            if (mSocket != null) {
                this.closeMulticastSocket();
            }
            mSocket = new MulticastSocket(port);

            mSocket.setTimeToLive(4);
            mSocket.joinGroup(mGroupAddr);
        } catch (UnknownHostException e) {
            Log.e(TAG, "UnknownHost error");
            throw e;
        } catch (IOException e) {
            Log.e(TAG, "MulticastSocket error when creating");
            throw e;
        }
    }

    private void closeMulticastSocket() {
        if (DEBUG) Log.d(TAG, "closeMulticastSocket()");
        if (mSocket != null) {
            try {
                mSocket.leaveGroup(mGroupAddr);
                mSocket.close();
            } catch (Exception exception) {
                Log.w(TAG, "closeMulticastSocket() Close socket exception ", exception);
            }
            mSocket = null;
        }
    }

    public boolean shouldIgnorePacket(DatagramPacket packet) {
        boolean result = false;
        if (mHostInfo !=null && mHostInfo.getAddress() != null) {
            InetAddress from = packet.getAddress();
            try {
                InetAddress host = InetAddress.getByName(mHostInfo.getAddress());
                if (from != null) {
                    if (from.isLinkLocalAddress() && (!host.isLinkLocalAddress())) {
                        // Ignore linklocal packets on regular interfaces, unless this is
                        // also a linklocal interface. This is to avoid duplicates. This is
                        // a terrible hack caused by the lack of an API to get the address
                        // of the interface on which the packet was received.
                        result = true;
                    }
                    if (from.isLoopbackAddress() && (!host.isLoopbackAddress())) {
                        // Ignore loopback packets on a regular interface unless this is
                        // also a loopback interface.
                        result = true;
                    }
                    if (from.equals(host)) {
                        result = true;
                    }
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void setClosed(boolean closed) {
        this.mClosed = closed;
    }

    public boolean isClosed() {
        return mClosed;
    }

    public MulticastSocket getSocket() {
        return mSocket;
    }

    public InetAddress getGroup() {
        return mGroupAddr;
    }

    public void setState(int state) {
        mState = state;
    }

    public int getState() {
        return mState;
    }

}
