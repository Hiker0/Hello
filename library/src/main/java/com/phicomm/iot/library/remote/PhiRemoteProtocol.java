package com.phicomm.iot.library.remote;

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
import com.phicomm.iot.library.protocol.IProtocol;

import java.net.DatagramPacket;

/**
 * Created by allen.z on 2017-04-25.
 */
public abstract class PhiRemoteProtocol implements IProtocol {

    public final static String TAG = "PhiProtocol/Remote";
    final static int DEFAULT_PORT = 3434;
    final static long DEFAULT_TIMEOUT = 500;

    final static int STATE_CLOSED = 0;
    final static int STATE_INITED = 1;

    int mState = STATE_CLOSED;

    private SmartDevice mDevice;
    private DatagramSender mSender;
    private DatagramSocketServer mServer;
    private ServerHandler mServerHandler;

    String hostAddr ;
    int  hostPort ;

    protected PhiRemoteProtocol(SmartDevice device) {
        mDevice = device;
        mServerHandler = new ServerHandler();
        mState = STATE_INITED;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };
    protected abstract void handleDeviceMessage(PhiMessage msg);
    protected abstract void onBind();

    private void confirmDevice(PhiMessage msg) {
        Log.d(TAG,"on confirmDevice");
        boolean confirm = false;
        byte[] data = msg.getInfo();
        if(data.length > 0 ){
            String strMsg = new String(data, 0, data.length).trim();
            Log.d(TAG,"confirmDevice:"+strMsg);
            if(strMsg.equals(mDevice.getBssid())){
                confirm = true;
            }
        }

        PhiMessage outMessage = PhiSystemMessage.replyCheckMessage(confirm);
        outMessage.setPort(msg.getPort());
        outMessage.setAddress(msg.getAddress());
        sendMessageInner(outMessage);
    }

    private void bindDevice(PhiMessage msg) {
        Log.d(TAG,"on bindDevice");
        hostAddr = msg.getAddress();
        hostPort = msg.getPort();

        PhiMessage outMessage = PhiSystemMessage.replyBindMessage(true);
        outMessage.setPort(msg.getPort());
        outMessage.setAddress(msg.getAddress());
        sendMessageInner(outMessage);
        onBind();
    }

    boolean handleIncomingMessage(PhiMessage msg) {
        Log.d(TAG,"handle message");
        if(msg.getFlag() == PhiSystemMessage.REQ_CONFIRM){
            confirmDevice(msg);
            return true;
        }

        if( msg.getFlag() == PhiSystemMessage.REQ_BIND){
            bindDevice(msg);
            return true;
        }
        return false;
    }

    void sendMessageInner(BaseMessage msg){
        mSender.sendDatagram(msg, mServer.getSocket());
    }
    @Override
    public void sendMessage(BaseMessage msg) {
        Log.d(TAG,"send:"+msg);
        msg.setAddress(hostAddr);
        msg.setPort(hostPort);
        sendMessageInner(msg);
    }

    @Override
    public void start() {
        mSender = ConnectionManager.getDatagramSender();
        mServer = ConnectionManager.getInstance().openDatagramSocketServer(DEFAULT_PORT, true);
        mServer.addDatagramSocketResolver(mServerHandler);
    }

    @Override
    public void stop() {
        mState = STATE_CLOSED;
        mDevice = null;
        mHandler.removeCallbacksAndMessages(null);
        mSender.release();
        mSender = null;
        mServer.removeDatagramSocketResolver(mServerHandler);
        mServer = null;
    }

    class ServerHandler implements IDatagramServerHandler {

        @Override
        public boolean handle(DatagramPacket datagramPacket) {
            Log.d(TAG,"on handle");
//            if (!datagramPacket.getAddress().equals(mDevice.getAddress())) {
//                Log.d(TAG,"ignore Packet");
//                return false;
//            }
            PhiMessage msg = PhiMessage.fromDatagram(datagramPacket);
            if (msg == null) {
                Log.d(TAG,"unregnized msg");
                return false;
            }

            if (handleIncomingMessage(msg)) {
                return true;
            }

            mHandler.post(new IncomeTask(msg));
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
            Log.d(TAG,"handleDeviceMessage:"+ msg);
            handleDeviceMessage(msg);
        }
    }

}
