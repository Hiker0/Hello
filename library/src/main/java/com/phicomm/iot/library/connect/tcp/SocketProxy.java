package com.phicomm.iot.library.connect.tcp;

import java.io.IOException;
import java.net.Socket;

/**
 * Author: Johnson
 * Date  : 2017-05-23
 * last modified: 2017-05-23
 */

public class SocketProxy {
    String mHostname;
    int mPort;
    Socket mSocket;
    SocketProxy(String host, int port) throws IOException {
        mHostname = host;
        mPort = port;
        mSocket = new Socket(mHostname,mPort);

    }

    void close(){
        if(mSocket != null){
            if(!mSocket.isClosed()){
                try {
                    mSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
