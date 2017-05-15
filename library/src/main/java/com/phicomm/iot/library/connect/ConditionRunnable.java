package com.phicomm.iot.library.connect;

/**
 * Created by allen.z on 2017-05-10.
 */
public class ConditionRunnable implements Runnable {
    protected boolean closed = false;
    @Override
    public void run() {
        while(!closed){

        }
    }
    public boolean isClosed(){
        return closed;
    }

    public void close(){
        closed = true;
    }
}
