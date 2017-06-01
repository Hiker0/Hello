package com.phicomm.iot.library.connect.tcp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: allen.z
 * Date  : 2017-05-26
 * last modified: 2017-05-26
 */

public class SocketClient {
    final static String TAG = "SocketClient";
    String mAddr;
    int mPort;
    Socket mSocket;
    ExecutorService mSingleThreadExecutor;
    BufferedWriter bw;
    BufferedReader br;
    InputStream input;
    OutputStream output;
    Listener mListener;
    STATUS status = STATUS.init;

    public enum STATUS {init, connecting, connectted, close}

    public SocketClient(String addr, int port) {
        mAddr = addr;
        mPort = port;
        mSocket = new Socket();
        status = STATUS.init;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void send(final String out) {
        Log.d(TAG,"send>>" +out);
        mSingleThreadExecutor.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    bw.write(out + "\n");
                    bw.flush();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                    disconnect();
                }
            }
        });

    }

    public void connect() {
        mSingleThreadExecutor = Executors.newSingleThreadExecutor();
        mSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"connectting" );
                status = STATUS.connecting;
                InetSocketAddress addr = new InetSocketAddress(mAddr, mPort);
                try {
                    mSocket.setKeepAlive(true);
                    mSocket.connect(addr, 3000);
                    output = mSocket.getOutputStream();
                    input = mSocket.getInputStream();
                    bw = new BufferedWriter(new OutputStreamWriter(output, "utf-8"));
                    br = new BufferedReader(new InputStreamReader(input, "utf-8"));
                    status = STATUS.connectted;
                    if (mListener != null) {
                        mListener.onConnect();
                    }
                    startListenThread();
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    disconnect();
                } catch (IOException e) {
                    disconnect();
                    e.printStackTrace();
                }

            }
        });

    }

    public STATUS getStatus() {
        return status;
    }

    private void startListenThread() {
        if (STATUS.connectted != status) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"start receive" );
                String line = null;
                try {
                    while ((line = br.readLine()) != null) {
                        Log.d(TAG,"receive<<"+line );
                        if (mListener != null) {
                            mListener.onReceive(line);
                        }
                    }
                } catch (IOException e) {
                    disconnect();
                }
            }
        }).start();
    }

    public void disconnect() {
        if (status == STATUS.close || status == STATUS.init) {
            return;
        }
        status = STATUS.close;

        try {
            if (mSocket != null && !mSocket.isClosed()) {
                mSocket.close();
            }
            if (br != null) {
                br.close();
            }

            if (bw != null) {
                bw.close();
            }

            if (input != null) {
                input.close();
            }

            if (output != null) {
                output.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mSingleThreadExecutor != null && !mSingleThreadExecutor.isShutdown()) {
            mSingleThreadExecutor.shutdown();
        }
        if (mListener != null) {
            mListener.onClose();
        }
    }

    public interface Listener {
        void onConnect();

        void onReceive(String json);

        void onClose();
    }

}
