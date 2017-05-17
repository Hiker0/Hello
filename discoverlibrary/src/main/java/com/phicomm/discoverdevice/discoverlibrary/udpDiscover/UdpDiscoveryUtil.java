package com.phicomm.discoverdevice.discoverlibrary.udpDiscover;

import android.util.Log;

import com.phicomm.discoverdevice.discoverlibrary.ContantString;
import com.phicomm.discoverdevice.discoverlibrary.MeshDiscoveryUtil;
import com.phicomm.discoverdevice.discoverlibrary.PhiIotDevice;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by chunya02.li on 2017/5/11.
 */

public class UdpDiscoveryUtil implements Runnable {

    private static final String TAG = "UdpDiscoveryUtil";
    List<PhiIotDevice> responseList = new ArrayList<PhiIotDevice>();
    MeshDiscoveryUtil mMeshUti;

    public UdpDiscoveryUtil(MeshDiscoveryUtil meshUtil) {
        mMeshUti = meshUtil;
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

    private List<PhiIotDevice> discoverUdpDevices() {

        DatagramSocket socket = null;
        byte buf_receive[] = new byte[ContantString.RECEIVE_LEN];
        DatagramPacket pack = null;
        String realData = ContantString.data;
        responseList.clear();
        try {
            // alloc port for the socket
            socket = allocPort(ContantString.IOT_APP_PORT);
            // set receive timeout
            socket.setSoTimeout(ContantString.SO_TIMEOUT);
            // broadcast content
            pack = new DatagramPacket(realData.getBytes(), realData.length(), ContantString.broadcastAddress, ContantString.IOT_DEVICE_PORT);
            socket.send(pack);
            Log.d(TAG, "discoverDevices socket send send port is =" + ContantString.IOT_DEVICE_PORT + " realData=" + realData);
            long start = System.currentTimeMillis();
            while (true) {
                Log.d(TAG, "receive data");
                pack.setData(buf_receive);
                socket.receive(pack);
                long consume = System.currentTimeMillis() - start;
                Log.d(TAG, "It is send consume=" + consume);
                PhiIotDevice mPhiIotDevice = UdpDataParser.parsePackage(pack);
                if (mPhiIotDevice != null && !responseList.contains(mPhiIotDevice)) {
                    responseList.add(mPhiIotDevice);
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
        mMeshUti.notifyDeviceResultAdd(responseList);
        return responseList;
    }
}
