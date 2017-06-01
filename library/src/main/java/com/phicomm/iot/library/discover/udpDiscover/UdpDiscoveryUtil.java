package com.phicomm.iot.library.discover.udpDiscover;

import android.util.Log;

import com.phicomm.iot.library.device.BaseDevice;
import com.phicomm.iot.library.device.IIotDevice;
import com.phicomm.iot.library.discover.MeshDiscoveryUtil;
import com.phicomm.iot.library.discover.PhiConstants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by chunya02.li on 2017/5/11.
 */

public class UdpDiscoveryUtil implements Runnable {

    private static final String TAG = "UdpDiscoveryUtil";
    List<BaseDevice> responseList = new ArrayList<BaseDevice>();
    MeshDiscoveryUtil mMeshUti;
    private Map<String, IIotDevice> mUdpLocalIotAddress;

    public UdpDiscoveryUtil(MeshDiscoveryUtil meshUtil) {
        mMeshUti = meshUtil;
        mUdpLocalIotAddress = new HashMap<>();
    }


    @Override
    public void run() {
        discoverUdpDevices();
    }

    /**
     * alloc the port number
     *
     * @param hostPort if -1<port<65536, it will allocate a random port, which could be used else it will allocate the
     *                 specified port first, after that it will allocate a random one
     * @return DatagramSocket
     */
    private DatagramSocket allocPort(int hostPort) {

        Log.d(TAG, "allocPort() entrance");

        boolean success = false;
        DatagramSocket socket = null;

        // try to allocate the specified port
        if (-1 < hostPort && hostPort < 65536) {
            try {
                socket = new DatagramSocket(hostPort);
                success = true;
                Log.d(TAG, "allocPort(hostPort=[" + hostPort + "]) suc");
                return socket;
            } catch (SocketException e) {
                Log.d(TAG, "allocPort(hostPort=[" + hostPort + "]) is used");
            }
        }
        // allocate a random port
        do {
            try {
                // [1024,65535] is the dynamic port range
                hostPort = 1024 + new Random().nextInt(65536 - 1024);
                socket = new DatagramSocket(hostPort);
                success = true;
            } catch (SocketException e) {
                e.printStackTrace();
            }
        } while (!success);
        return socket;
    }

    private List<BaseDevice> discoverUdpDevices() {

        DatagramSocket socket = null;
        byte buf_receive[] = new byte[PhiConstants.RECEIVE_LEN];
        DatagramPacket pack = null;
        String realData = PhiConstants.data;
        responseList.clear();
        try {
            // alloc port for the socket
            socket = allocPort(PhiConstants.IOT_APP_PORT);
            // set receive timeout
            socket.setSoTimeout(PhiConstants.SO_TIMEOUT);
            // broadcast content
            pack = new DatagramPacket(realData.getBytes(), realData.length(), PhiConstants.broadcastAddress, PhiConstants.IOT_DEVICE_PORT);
            socket.send(pack);
            Log.d(TAG, "discoverDevices socket send send port is =" + PhiConstants.IOT_DEVICE_PORT + " realData=" + realData);
            long start = System.currentTimeMillis();
            while (true) {
                Log.d(TAG, "receive data");
                pack.setData(buf_receive);
                socket.receive(pack);
                long consume = System.currentTimeMillis() - start;
                Log.d(TAG, "It is send consume=" + consume);
                BaseDevice mBaseDevice = UdpDataParser.parsePackage(pack);
                if (mBaseDevice != null && !mUdpLocalIotAddress.containsKey(mBaseDevice.getBssid())) {
                    mUdpLocalIotAddress.put(mBaseDevice.getBssid(),mBaseDevice);
                    responseList.add(mBaseDevice);
                    mMeshUti.notifyDeviceResultAdd(responseList);
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // ignore SocketTimeoutException
            if (e instanceof SocketTimeoutException) {
            } else {
                e.printStackTrace();
            }
        } finally {
            if (socket != null) {
                socket.disconnect();
                socket.close();
                Log.d(TAG, "discoverDevices: socket is not null, closed in finally");
            } else {
                Log.d(TAG, "discoverDevices: sockect is null, closed in finally");
            }
        }
        Log.d(TAG, "responseList.size()=" + responseList.size());
        return responseList;
    }
}
