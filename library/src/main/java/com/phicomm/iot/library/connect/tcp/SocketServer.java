package com.phicomm.iot.library.connect.tcp;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Johnson on 2017-04-25.
 */
public class SocketServer extends  Thread {
    private final static String TAG = "Connection/ScoketServer";
    boolean closed = false;
    int port = 0;
    ServerSocket serverSocket;

    public SocketServer(int port){
        this.port =  port;
        try {
            serverSocket= new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            closed = true;
        }
    };

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public void run() {
        Log.d(TAG, "start accept port="+port);
        while(!closed){
            try {
                Socket s = serverSocket.accept();
                Log.d(TAG,"accept:" + s.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close(){
        closed = true;
        if(!serverSocket.isClosed()){
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
