package com.phicomm.iot.library.connect.udp;

import android.util.Log;

import com.phicomm.iot.library.connect.ConnectionManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class DatagramSocketServer extends Thread {
    static final String TAG = "Connection/SocketServer";
    DatagramSocket mServer;
    int mPort;
    boolean mClosed = false;
    List<IDatagramServerHandler> mResolvers;

    public DatagramSocketServer(int defaultPort) {
        Log.d(TAG,"DatagramSocketServer create:"+defaultPort);
        mPort = defaultPort;
        try {
            mServer = new DatagramSocket(defaultPort);
        } catch (SocketException e) {
            e.printStackTrace();
            Log.d(TAG,"DatagramSocketServer fail");
            mClosed = true;
        }
        mResolvers = new ArrayList<IDatagramServerHandler>();
    }

    @Override
    public void run() {
        while (!mClosed) {
            byte[] bys = new byte[1024];
            DatagramPacket dp = new DatagramPacket(bys, bys.length);
            try {
                mServer.receive(dp);
                synchronized (mResolvers) {
                    Iterator<IDatagramServerHandler> iterator = mResolvers.iterator();
                    while (iterator.hasNext()) {
                        if (iterator.next().handle(dp)) {
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                mClosed = true;
            }
        }
    }

    public void addDatagramSocketResolver(IDatagramServerHandler resolver) {
        synchronized (mResolvers) {
            mResolvers.add(resolver);
        }
        if(mResolvers.size() == 1){
            start();
        }
    }

    public void removeDatagramSocketResolver(IDatagramServerHandler resolver) {
        synchronized (mResolvers) {
            mResolvers.remove(resolver);
        }
        if(mResolvers.size() == 0){
            close();
        }
    }

    public DatagramSocket getSocket() {
        return mServer;
    }

    public int getPort() {
        return mPort;
    }

    public void close() {
        if (!mClosed) {
            mClosed = true;
            if(mServer != null && !mServer.isClosed()) {
                mServer.close();
            }
            ConnectionManager.getInstance().removeDatagramSocketServer(this);
        }
    }
}
