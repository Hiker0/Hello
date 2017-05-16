package com.phicomm.iot.library.connect.udp;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.phicomm.iot.library.message.BaseMessage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


/**
 * Created by Johnson on 2017-04-25.
 */
public class DatagramSender {
    static final String TAG = "Connection/Sender";
    Handler mHandler;
    HandlerThread mThread;
    DatagramSocket mSocket;
    static DatagramSender mInstance;
    static int mRef = 0;

    public static DatagramSender getInstance() {
        if (mInstance == null) {
            mInstance = new DatagramSender();
            mRef = 0;
        }
        mRef++;
        Log.d(TAG,"getInstance mRef ="+mRef);
        return mInstance;
    }

    private DatagramSender() {
        mThread = new HandlerThread("UdpSender");
        mThread.start();
        mHandler = new Handler(mThread.getLooper());
        try {
            mSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void sendDatagram(int port, InetAddress address, byte[] data, int length) {
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        sendDatagram(packet);
    }

    public void sendDatagram(DatagramPacket packet) {
        mHandler.post(new DatagramSendTask(mSocket, packet));
    }

    public void sendDatagram(BaseMessage msg, DatagramSocket socket) {
        InetAddress addr = null;
        try {
            addr = InetAddress.getByName(msg.getAddress());
            DatagramPacket packet = new DatagramPacket(msg.toByte(), msg.dataLength(),addr, msg.getPort());
            mHandler.post(new DatagramSendTask(socket, packet));
        } catch (UnknownHostException e) {
            Log.e(TAG,"Error InetAddress when sending");
            e.printStackTrace();
        }
    }

    public void release() {
        mRef--;
        Log.d(TAG,"release mRef ="+mRef);
        if (mRef == 0) {
            mInstance = null;
            if (!mSocket.isClosed()) {
                mSocket.close();
            }
            mHandler.removeCallbacksAndMessages(null);
            mThread.quitSafely();
        }
    }
}
