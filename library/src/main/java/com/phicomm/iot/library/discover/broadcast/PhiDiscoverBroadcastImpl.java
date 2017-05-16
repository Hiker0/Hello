package com.phicomm.iot.library.discover.broadcast;

import android.os.Handler;
import android.util.Log;

import com.phicomm.iot.library.connect.ConnectionManager;
import com.phicomm.iot.library.connect.udp.DatagramSender;
import com.phicomm.iot.library.connect.udp.DatagramSocketServer;
import com.phicomm.iot.library.connect.udp.IDatagramServerHandler;
import com.phicomm.iot.library.device.BaseDevice;
import com.phicomm.iot.library.device.TYPE;
import com.phicomm.iot.library.discover.PhiConstants;
import com.phicomm.iot.library.discover.DiscoveredDevice;
import com.phicomm.iot.library.discover.PhiDiscover;
import com.phicomm.iot.library.discover.PhiDiscoverListener;
import com.phicomm.iot.library.discover.PhiDiscoverPackage;
import com.phicomm.iot.library.message.PhiMessage;

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
    private Timer mTimer;
    private TimerTask mTask;
    private Handler mHandler;
    private DatagramSender mSender;
    private DatagramSocketServer mServer;
    private ServerHandler mServerHandler;
    int mSendPort;
    int mReceivPort;


    public PhiDiscoverBroadcastImpl(int sendPort, int receivePort) throws IOException {
        mSendPort = sendPort;
        mReceivPort = receivePort;
        mDiscoverListeners = Collections.synchronizedList(new ArrayList());
        mDeviceList = Collections.synchronizedList(new ArrayList());
        mTimer = new Timer();
        mHandler = new Handler();
        mServerHandler = new ServerHandler();
    }

    @Override
    public void startResearchDevice() throws Exception {
        if (mState != PhiConstants.STATE_CANCELED) {
            Log.w(TAG, "PhiDiscover is already running");
            throw new Exception();
        }
        mDeviceList.clear();
        mSender = ConnectionManager.getDatagramSender();
        mServer = ConnectionManager.getInstance().openDatagramSocketServer(mReceivPort, true);
        mServer.addDatagramSocketResolver(mServerHandler);
        mState = PhiConstants.STATE_ANOUNCE;
        startAnouncher();
    }

    @Override
    public void cancel() {
        if (mState != PhiConstants.STATE_CANCELED) {
            mState = PhiConstants.STATE_CANCELED;
            if(mSender != null) {
                mSender.release();
                mSender = null;
            }

            if(mServer != null) {
                mServer.close();
                mServer.removeDatagramSocketResolver(mServerHandler);
                mServer = null;
            }
            mHandler.removeCallbacksAndMessages(null);
            mDiscoverListeners.clear();
            mDeviceList.clear();
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
                if(mSender != null) {
                    mSender.release();
                }
                mSender = null;
                if(mServer != null) {
                    mServer.close();
                    mServer.removeDatagramSocketResolver(mServerHandler);
                    mServer = null;
                }
                mDiscoverListeners = null;
                mDeviceList.clear();
                mDeviceList = null;
                if (mTask != null) {
                    mTask.cancel();
                    mTask = null;
                }
                mSender = ConnectionManager.getDatagramSender();
                mServer = ConnectionManager.getInstance().openDatagramSocketServer(mReceivPort, true);
                mState = PhiConstants.STATE_ANOUNCE;
                startAnouncher();

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

    public void handleQurey() {
        Log.d(TAG, "handleQurey");
        handleQurey(null);
    }

    public void handleQurey(PhiDiscoverPackage phipackage) {
        if (mHostInfo != null) {
            TYPE type = mHostInfo.getType();
            PhiDiscoverPackage packet = new PhiDiscoverPackage(mHostInfo.getBrand().name(), type.name(), mHostInfo.getName(), mHostInfo.getBssid());
            String data = EspressMessage.getAnserData(type, mHostInfo.getBssid(),mHostInfo.getAddress() );
            send(data.getBytes());
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
    void send(byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length, EspressMessage.getAddress(), mSendPort);
        mSender.sendDatagram(packet);
    }

    void send(DatagramPacket packet) {
        mSender.sendDatagram(packet);
    }

    public boolean shouldIgnorePacket(DatagramPacket packet) {
        boolean result = false;
        if (mHostInfo != null && mHostInfo.getAddress() != null) {
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
    class ServerHandler implements IDatagramServerHandler {
        @Override
        public boolean handle(DatagramPacket datagramPacket) {
            Log.d(TAG, "handle datagramPacket");

            if (datagramPacket.getAddress().getHostAddress().equals(mHostInfo.getAddress())) {
                Log.d(TAG, "ignore");
                return false;
            }

            byte[] data = datagramPacket.getData();
            int len = datagramPacket.getLength();
            int off = datagramPacket.getOffset();
            String strMsg = new String(data, off, len).trim();
            if(strMsg.equals(EspressMessage.data)){
                handleQurey();
            }

            return true;
        }
    }
    public void setClosed(boolean closed) {
        this.mClosed = closed;
    }

    public boolean isClosed() {
        return mClosed;
    }

    public void setState(int state) {
        mState = state;
    }

    public int getState() {
        return mState;
    }

}
