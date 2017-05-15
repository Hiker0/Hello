package com.phicomm.iot.library.connect;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Johnson on 2017-04-25.
 */
public class DatagramSendTask implements Runnable {
    static final String TAG = "Connection/SendTask";

    DatagramSocket datagramSocket;
    DatagramPacket datagramPacket;
    DatagramSendTask( DatagramSocket socket, DatagramPacket packet) {
        datagramSocket = socket;
        datagramPacket = packet;
    }

    @Override
    public void run() {
        try {
            Log.d(TAG,"send:addr=" + datagramPacket.getAddress().getHostAddress() +", Port=" + datagramPacket.getPort());
            datagramSocket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
