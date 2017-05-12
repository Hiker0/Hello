package com.phicomm.demo.discovery.udpDiscovery;


import android.util.Log;

import com.phicomm.demo.discovery.SampleIotAddress;

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

public class UdpDiscoveryUtil {

    private static final String TAG = "UdpDiscoveryUtil";
    List<SampleIotAddress> responseList = new ArrayList<SampleIotAddress>();

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

    public List<SampleIotAddress> discoverUdpDevices() {
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
            Log.d(TAG, "discoverDevices socket send");
            socket.send(pack);
            pack.setData(buf_receive);
            long start = System.currentTimeMillis();
            while (true) {
                socket.receive(pack);
                long consume = System.currentTimeMillis() - start;
                Log.d(TAG, "It is send consume=" + consume);
                SampleIotAddress mSampleIotAddress = UdpDataParser.parsePackage(pack);
                if (mSampleIotAddress != null && !responseList.contains(mSampleIotAddress)) {
                    responseList.add(mSampleIotAddress);
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
        return responseList;
    }
}

