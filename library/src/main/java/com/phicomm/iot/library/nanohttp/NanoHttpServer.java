package com.phicomm.iot.library.nanohttp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by allen.z on 2017-05-17.
 */
public class NanoHttpServer extends  NanoHTTPD{
    public NanoHttpServer(int port) {
        super(8080);
    }

    @Override
    public Response serve(IHTTPSession session) {
        return super.serve(session);
    }
}
