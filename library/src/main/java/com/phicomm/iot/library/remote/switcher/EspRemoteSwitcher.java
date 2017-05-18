package com.phicomm.iot.library.remote.switcher;

import com.google.gson.Gson;
import com.phicomm.iot.library.connect.NanoHTTPD;
import com.phicomm.iot.library.device.BaseDevice;
import com.phicomm.iot.library.device.SmartDevice;
import com.phicomm.iot.library.devices.switcher.ISwitcher;
import com.phicomm.iot.library.protocol.esp.EspHttpServer;
import com.phicomm.iot.library.protocol.esp.Info;
import com.phicomm.iot.library.protocol.esp.StatusResponse;

import java.io.IOException;

/**
 * Author: allen.z
 * Date  : 2017-05-17
 * last modified: 2017-05-17
 */
public class EspRemoteSwitcher extends SmartDevice implements IRemoteSwitcher  {

    SwitcherHttpServer mServer;
    ISwitcher mSwitcher;
    EspRemoteSwitcher(BaseDevice device, ISwitcher switcher){
        super(device);
        mServer = new SwitcherHttpServer();
        mSwitcher = switcher;
    }

    @Override
    public void reportStatus(boolean on) {

    }

    @Override
    public void start() {
        try {
            mServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        mServer.stop();
    }


    class SwitcherHttpServer extends EspHttpServer{
        SwitcherHttpServer() {

        }

        @Override
        protected Response handleConfigCommand(String cmd) {
            if(cmd.equals("switch")){
                StatusResponse re = new StatusResponse();
                re.setStatus(mSwitcher.isOn()? 1 : 0);
                Gson gson =new Gson();
                return new Response(Response.Status.OK, MIME_PLAINTEXT, gson.toJson(re));
            }
            return super.handleConfigCommand(cmd);
        }

        @Override
        protected Response handleConfig(String cmd, String body) {
            if(cmd.equals("switch")){
                Gson gson =new Gson();
                StatusResponse status = gson.fromJson(body, StatusResponse.class);
                if(status != null){
                    if(status.getStatus() == 0){
                        mSwitcher.turnOff();
                    }else{
                        mSwitcher.turnOn();
                    }
                }

                StatusResponse re = new StatusResponse();
                re.setStatus(mSwitcher.isOn()? 1 : 0);
//                Gson ss =new Gson();
                return new Response(Response.Status.OK, MIME_PLAINTEXT, gson.toJson(re));

            }
            return super.handleConfig(cmd, body);
        }

        @Override
        protected Info getInfo() {
            Info info = new Info();
            Info.Device device = new Info.Device();
            device.setProduct(getType().toString());
            device.setManufacturer("Espressif Systems");
            Info.Version version = new Info.Version();
            version.setHardWare("0.1");
            version.setIotVersion("2.0.0(656edbf) ");
            version.setSdkVersion("v1.0.5t45772 (a) ");

            info.setDevice(device);
            info.setVersion(version);

            return info;
        }
    }

}
