package com.phicomm.demo.user;

import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.phicomm.demo.DemoApp;
import com.phicomm.demo.user.UserContract.View;
import com.phicomm.demo.util.MD5Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hzn on 17-5-26.
 */

public class UserPresenter implements UserContract.Presenter {
    private View mView;

    public UserPresenter(View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {

    }

    private void phiLogin(String phoneNumber, String password) {
        String url = String.format("http://114.141.173.27:10006/v1/login?authorizationcode=%s&phonenumber=%s&password=%s",
                "feixun*123.SH_9976981",
                phoneNumber,
                MD5Utils.md5(password));

        JsonObjectRequest request = new JsonObjectRequest(Method.POST, url, null, new Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String accessToken = response.getString("access_token");
                    int accessTokenExpire = response.getInt("access_token_expire");
                    DemoApp.getInstance().store().setPhiToken(accessToken, accessTokenExpire);

                    DemoApp.getInstance().addToRequestQueue(espLogin(), "esp-login");
                } catch (JSONException e) {
                    throw new RuntimeException("JSON Error " + e.getMessage());
                }
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mView.showLoginFail();
            }
        });
        DemoApp.getInstance().addToRequestQueue(request, "phiLogin");
    }

    private JsonObjectRequest espLogin() {
        JSONObject params = new JSONObject();
        try {
            params.put("email", "huzhennan5793@163.com");
            params.put("password", "iot2017");
        } catch (JSONException e) {
            throw new RuntimeException("JSON ERROR" + e.getMessage());
        }

        JsonObjectRequest request = new JsonObjectRequest("https://iot.espressif.cn/v1/user/login/",
                params,
                new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            Log.d("hzn", "hzn login status" + status);
                            if (status == 200) {
                                String token = response.getJSONObject("key").getString("token");
                                String username = response.getJSONObject("user").getString("username");
                                Log.d("hzn", "hzn test token: " + token + " username: " + username);
                                DemoApp.getInstance().store().setIotToken(token);

                                mView.showLoginSuccess();
                            } else {
                                mView.showLoginFail();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException("JSON Error " + e.getMessage());
                        }
                    }
                },
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("hzn", "ERROR: " + error.getMessage());
                        mView.showLoginFail();
                    }
                });
        return request;
    }

    @Override
    public void login(String phoneNumber, String password) {
        mView.showLoading();

        phiLogin(phoneNumber, password);
    }
}
