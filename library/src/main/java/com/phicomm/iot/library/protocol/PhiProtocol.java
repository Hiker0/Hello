package com.phicomm.iot.library.protocol;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.phicomm.iot.library.connect.ConnectionManager;
import com.phicomm.iot.library.connect.udp.DatagramSender;
import com.phicomm.iot.library.connect.udp.DatagramSocketServer;
import com.phicomm.iot.library.connect.udp.IDatagramServerHandler;
import com.phicomm.iot.library.device.SmartDevice;
import com.phicomm.iot.library.message.BaseMessage;
import com.phicomm.iot.library.message.PhiMessage;
import com.phicomm.iot.library.message.PhiSystemMessage;

import java.net.DatagramPacket;

/**
 * Created by allen.z on 2017-04-25.
 */
abstract public class PhiProtocol implements IProtocol {
    public final static String TAG = "PhiProtocol/PhiProtocol";
    final static int DEFAULT_PORT = 5353;
    final static int DEVICE_PORT = 5353;
    final static long DEFAULT_TIMEOUT = 1000;

    final static int STATE_CLOSED = 0;
    final static int STATE_INITED = 1;
    final static int STATE_CONFIRMED = 2;
    final static int STATE_BIND = 3;

    final static int MSG_INIT = 101;
    final static int MSG_CONFIRM = 102;
    final static int MSG_BIND = 103;
    final static int MSG_CONFIRM_TIMEOUT = 104;
    final static int MSG_BIND_TIMEOUT = 105;
    final static int MSG_REPLY_ALIVE = 106;

    int mState = STATE_CLOSED;

    private SmartDevice mDevice;
    private DatagramSender mSender;
    private DatagramSocketServer mServer;
    private ServerHandler mServerHandler;

    protected PhiProtocol(SmartDevice device) {
        Log.d(TAG,"PhiProtocol create");
        mDevice = device;
        mServerHandler = new ServerHandler();
        mState = STATE_CLOSED;
    }


    @Override
    public void start() {
        Log.d(TAG,"PhiProtocol start");
        mSender = ConnectionManager.getDatagramSender();
        mServer = ConnectionManager.getInstance().openDatagramSocketServer(DEFAULT_PORT, true);
        mServer.addDatagramSocketResolver(mServerHandler);
        mState = STATE_INITED;
        mHandler.sendEmptyMessage(MSG_CONFIRM);
    }

    @Override
    public void stop() {
        mState = STATE_CLOSED;
        mHandler.removeCallbacksAndMessages(null);
        if(mSender != null) {
            mSender.release();
        }
        mSender = null;
        if(mServer != null) {
            mServer.removeDatagramSocketResolver(mServerHandler);
        }
        mDevice = null;
        mServer = null;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CONFIRM:
                    confirmDevice();
                    Message mm = mHandler.obtainMessage(MSG_CONFIRM_TIMEOUT);
                    mHandler.sendMessageDelayed(mm, DEFAULT_TIMEOUT);
                    break;
                case MSG_BIND:
                    bindDevice();
                    mHandler.sendEmptyMessageDelayed(MSG_BIND_TIMEOUT, DEFAULT_TIMEOUT);
                    break;
                case MSG_CONFIRM_TIMEOUT:
                    Log.d(TAG, "MSG_CONFIRM_TIMEOUT");
                    stop();
                    mHandler.removeMessages(MSG_CONFIRM_TIMEOUT);
                    break;
                case MSG_BIND_TIMEOUT:
                    Log.d(TAG, "MSG_BIND_TIMEOUT");
                    stop();
                    mHandler.removeMessages(MSG_BIND_TIMEOUT);
                    break;
                case MSG_REPLY_ALIVE:
                    reponseAlive();
            }
        }
    };

    private void reponseAlive(){
        Log.d(TAG, "reponseAlive");
        byte state = PhiSystemMessage.STATE_ALIVE;
        if(mState == STATE_CLOSED){
            state  = PhiSystemMessage.STATE_UN_ALIVE;
        }
        PhiMessage msg = PhiSystemMessage.getReAliveMessage(state);
        sendMessage(msg);
    }
    private void confirmDevice() {
        Log.d(TAG, "confirmDevice");
        PhiMessage msg = PhiSystemMessage.getCheckMessage(mDevice.getBssid());
        sendMessage(msg);
    }

    private void bindDevice() {
        Log.d(TAG, "bindDevice");
        PhiMessage msg = PhiSystemMessage.getBindMessage();
        sendMessage(msg);;
    }

    boolean handleIncomingMessage(PhiMessage msg) {
        Log.d(TAG, "handleIncomingMessage:"+ msg);
        if(msg.getFlag() == PhiSystemMessage.ACK_CONFIRM){
            mHandler.removeMessages(MSG_CONFIRM_TIMEOUT);
            byte[] data = msg.getInfo();
            if(data.length > 0 && data[0] == PhiSystemMessage.CONFIRM_SUCCESS){
                mState = STATE_CONFIRMED;
                Log.d(TAG, "confirm success");
                mHandler.sendEmptyMessage(MSG_BIND);
            }
            return true;
        }

        if( msg.getFlag() == PhiSystemMessage.ACK_BIND){
            mHandler.removeMessages(MSG_BIND_TIMEOUT);
            byte[] data = msg.getInfo();
            if(data.length > 0 && data[0] == PhiSystemMessage.BIND_SUCCESS){
                mState = STATE_BIND;
                Log.d(TAG, "bind success");
            }
            return true;
        }

        if(msg.getFlag() == PhiSystemMessage.REQ_CHECK_LIVE){
            mHandler.sendEmptyMessage(MSG_REPLY_ALIVE);
            return true;
        }

        mHandler.post(new IncomeTask(msg));
        return false;
    }

    @Override
    public void sendMessage(BaseMessage msg) {
        if(mState != STATE_CLOSED) {
            Log.d(TAG,"sendMessage:"+ msg);
            msg.setAddress(mDevice.getAddress());
            msg.setPort(DEVICE_PORT);
            mSender.sendDatagram(msg, mServer.getSocket());
        }
    }

    class ServerHandler implements IDatagramServerHandler {
        @Override
        public boolean handle(DatagramPacket datagramPacket) {
            Log.d(TAG, "handle datagramPacket");
            if(mState == STATE_CLOSED){
                return false;
            }

            if (!datagramPacket.getAddress().getHostAddress().equals(mDevice.getAddress())) {
                Log.d(TAG, "ignore");
                return false;
            }

            PhiMessage msg = PhiMessage.fromDatagram(datagramPacket);
            if (msg == null) {
                Log.d(TAG, "this is not a phicomm message");
                return false;
            }
            handleIncomingMessage(msg);
            return true;
        }
    }
    class IncomeTask implements Runnable{
        PhiMessage msg;
        IncomeTask(PhiMessage msg){
            this.msg = msg;
        }

        @Override
        public void run() {
            Log.d(TAG, "handleDeviceMessage");
            handleDeviceMessage(msg);
        }
    }

    protected abstract void handleDeviceMessage(PhiMessage msg);
}
