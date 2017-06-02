package com.phicomm.iot.library.discover.internetDiscover;

import android.util.Log;

import com.phicomm.iot.library.device.BRAND;
import com.phicomm.iot.library.device.BaseDevice;
import com.phicomm.iot.library.device.IIotDevice;
import com.phicomm.iot.library.device.TYPE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.phicomm.iot.library.discover.internetDiscover.IPhiCommand.Authorization;
import static com.phicomm.iot.library.discover.internetDiscover.IPhiCommand.Bssid;
import static com.phicomm.iot.library.discover.internetDiscover.IPhiCommand.Key;
import static com.phicomm.iot.library.discover.internetDiscover.IPhiCommand.Ptype;
import static com.phicomm.iot.library.discover.internetDiscover.IPhiCommand.Status;
import static com.phicomm.iot.library.discover.internetDiscover.IPhiCommand.Token;

/**
 * Created by chunya02.li on 2017/5/27.
 */

public class PhiCommandDeviceSynchronizeInternet implements ICommandDeviceSynchronizeInternet{

    protected OkHttpClient mClient;
    private static String TAG = "PhiCommandDeviceSynchronizeInternet";
    ArrayList<IIotDevice> mInternetDeviceList = new ArrayList<>();
    private Map<String, IIotDevice> mInternetIotAddress;

    public PhiCommandDeviceSynchronizeInternet() {
        mInternetIotAddress = new HashMap<>(0);
        mClient = new OkHttpClient();
    }

    @Override
    public ArrayList<IIotDevice> doCommandSynchronizeInternet(String userKey) {
        mInternetDeviceList.clear();
        JSONObject jsonObjectResult = getJSONObject(userKey);
        return getDeviceList(jsonObjectResult);
    }

    @Override
    public ArrayList<IIotDevice> getInternetDeviceList() {
        return mInternetDeviceList;
    }
    public ArrayList<IIotDevice> getDeviceList(JSONObject jsonObjectResult) {
        BaseDevice mBaseDevice;
        String mToken;
        String mBssid;
        int mTypeSerial;
        if (jsonObjectResult != null) {
            try {
                int status = jsonObjectResult.getInt(Status);
                Log.d(TAG, "status="+status);
                if (status == 200) {
                    JSONArray jsonArray = jsonObjectResult.getJSONArray(Devices);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject deviceJsonObject = jsonArray.getJSONObject(i);
                        Log.d(TAG, "deviceJsonObject=" + deviceJsonObject.toString());
                        mTypeSerial = deviceJsonObject.getInt(Ptype);
                        mBssid = deviceJsonObject.getString(Bssid);
                        JSONObject keyObject = (JSONObject) deviceJsonObject.get(Key);
                        mToken = keyObject.getString(Token);
                        TYPE type = TYPE.getTypeEnumBySerial(mTypeSerial);
                        mBaseDevice = new BaseDevice(type, "", mBssid, BRAND.PHICOMM, false);
                        if (mInternetIotAddress.containsKey(mBssid)) {
                            continue;
                        } else {
                            mInternetIotAddress.put(mBssid, mBaseDevice);
                            mInternetDeviceList.add(mBaseDevice);
                        }
                    }
                    return mInternetDeviceList;
                }
                return null;
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
        Log.d(TAG, "request head ="+request.headers());
        try {
            response = mClient.newCall(request).execute();
        } catch (IOException e) {
            Log.d(TAG,"IOException");
            e.printStackTrace();
        }
        if(response != null) {
            try {
                strBody=response.body().string();
                Log.d(TAG,"strBody="+strBody);
                statusCode = response.header(Status,"404");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            resultJson = new JSONObject(strBody);
            if (!resultJson.has(Status))
            {
                resultJson.put(Status, statusCode);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultJson;
    }

}
