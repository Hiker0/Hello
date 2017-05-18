package com.phicomm.iot.library.protocol.esp;

import android.util.Log;

import com.google.gson.Gson;
import com.phicomm.iot.library.connect.NanoHTTPD;
import com.phicomm.iot.library.device.BaseDevice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Author: allen.z
 * Date  : 2017-05-17
 * last modified: 2017-05-17
 */
public class EspHttpServer extends NanoHTTPD {
    final static String TAG = "Esp/HttpServer";
    public EspHttpServer() {
        super(8080);
    }

    public EspHttpServer(int port) {
        super(port);
    }

    protected Response handleClientCommand(String cmd) {
        if(cmd.equals("info")){
            Info info = getInfo();
            Gson gson =new Gson();
            return new Response(Response.Status.OK, MIME_PLAINTEXT, gson.toJson(info));
        }
        return new Response(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "unkown command");
    }

    protected Response handleConfigCommand(String cmd) {

        return new Response(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "unkown command");
    }

    protected Response handleConfig(String cmd, String body) {

        return new Response(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "unkown command");
    }

    protected Info getInfo(){
        Info info = new Info();
        Info.Device device = new Info.Device();
        device.product = "Unkown";
        device.manufacturer = "Espressif Systems";
        Info.Version version = new Info.Version();
        version.hardWare = "0.1";
        version.iotVersion = "2.0.0(656edbf) ";
        version.sdkVersion = "v1.0.5t45772 (a) ";

        info.mDevice = device;
        info.mVersion = version;
        return info;
    }

    @Override
    public Response serve(IHTTPSession session) {

        String uri = session.getUri();
        Method method = session.getMethod();
        Map<String, String> parms = session.getParms();
        Log.d(TAG, "serve:uri=" + uri + " ,method=" + method);
        if (uri.equals("/client")) {
            if (Method.GET.equals(method)) {
                Iterator<String> e = parms.keySet().iterator();
                while (e.hasNext()) {
                    String parm = e.next();
                    if (parm.equals("command")) {
                        return handleClientCommand(parms.get(parm));
                    }
                }
            }
        }else if(uri.equals("/config")) {
            if (Method.GET.equals(method)) {
                Iterator<String> e = parms.keySet().iterator();
                while (e.hasNext()) {
                    String parm = e.next();
                    if (parm.equals("command")) {
                        return handleConfigCommand(parms.get(parm));
                    }
                }
            }
            if(Method.POST.equals(method)) {
                Map<String, String> files = new HashMap<String, String>();
                try {
                    session.parseBody(files);
                    String body = session.getQueryParameterString();
                    Iterator<String> e = parms.keySet().iterator();
                    while (e.hasNext()) {
                        String parm = e.next();
                        if (parm.equals("command")) {
                            return handleConfig(parms.get(parm), body);
                        }
                    }
                } catch (ResponseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return super.serve(session);
    }
}
