package com.phicomm.iot.library.connect;

import java.net.DatagramPacket;

/**
 * Created by allen.z on 2017-04-25.
 */
public interface IDatagramServerHandler {
    boolean handle(DatagramPacket datagramPacket);
}
