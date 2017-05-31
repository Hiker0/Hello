package com.phicomm.iot.library.discover.internetDiscover;

import com.phicomm.iot.library.device.BaseDevice;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.phicomm.iot.library.discover.internetDiscover.IPhiCommand.Authorization;
import static com.phicomm.iot.library.discover.internetDiscover.IPhiCommand.Status;
import static com.phicomm.iot.library.discover.internetDiscover.IPhiCommand.Token;

/**
 * Created by chunya02.li on 2017/5/27.
 */

public class PhiCommandDeviceSynchronizeInternet implements ICommandDeviceSynchronizeInternet{

    protected OkHttpClient mClient;
    private final static String KEY_STATUS = "status";
    private static String TAG = "PhiCommandDeviceSynchronizeInternet";

    public PhiCommandDeviceSynchronizeInternet() {
        mClient = new OkHttpClient();
    }

    @Override
    public List<BaseDevice> doCommandSynchronizeInternet(String userKey) {
        JSONObject jsonObjectResult = getJSONObject(userKey);
        return getDeviceList(jsonObjectResult);
    }

    public List<BaseDevice> getDeviceList(JSONObject jsonObjectResult) {
        if (jsonObjectResult != null) {
            try {
                int status = jsonObjectResult.getInt(Status);
                if (status == 200) {
                    return (List<BaseDevice>)jsonObjectResult.get(Devices);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  null;
    }

    public JSONObject getJSONObject(String userKey) {
        //"Authorization: token HERE_IS_THE_USER_KEY" https://iot.espressif.cn/v1/user/devices/
        //{"status": 200, "deviceGroups": [...]}
        //HeaderPair headerAuthorization = new HeaderPair(Authorization, Token + " " + userKey);
        //String headerAuthorization = Authorization+" "+Token+" "+userKey;
        Response response=null;
        String strBody = null;
        String statusCode =null;
        JSONObject resultJson = null;
        Request request = new Request.Builder()
                .addHeader(Authorization,Token + " " + userKey)
                .url(URL)
                .build();
        try {
            response = mClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response != null) {
            try {
                strBody=response.body().string();
                statusCode = response.header(Status,"404");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            resultJson = new JSONObject(strBody);
            if (!resultJson.has(KEY_STATUS))
            {
                resultJson.put(KEY_STATUS, statusCode);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultJson;
    }

}
