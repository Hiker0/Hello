package com.phicomm.iot.library.connect;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ConnectionManager {

    static final String TAG = "Connection/ConnectionManager";
    static ConnectionManager mInstance;
    private Map<Integer, DatagramSocketServer> mDatagramServerMap;
    private Map<Integer, SocketServer> mScoketServerrMap;

    public static ConnectionManager getInstance() {
        if (mInstance == null) {
            mInstance = new ConnectionManager();
        }
        return mInstance;
    }

    ConnectionManager() {
        mDatagramServerMap = new HashMap<Integer, DatagramSocketServer>();
        mScoketServerrMap = new HashMap<Integer, SocketServer>();
    }

    public static DatagramSender getDatagramSender() {
        return DatagramSender.getInstance();
    }

    public DatagramSocketServer openDatagramSocketServer(int defaultPort, boolean force) {
        DatagramSocketServer server = mDatagramServerMap.get(defaultPort);
        if (server == null) {
            server = new DatagramSocketServer(defaultPort);
            mDatagramServerMap.put(server.getPort(), server);
        }
        return server;
    }


    public void removeDatagramSocketServer(DatagramSocketServer server) {
        server.close();
        mDatagramServerMap.remove(server.getPort());
    }
}
