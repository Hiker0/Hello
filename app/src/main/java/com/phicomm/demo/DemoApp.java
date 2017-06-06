package com.phicomm.demo;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.phicomm.demo.util.PreferenceStore;

/**
 * Created by hzn on 17-5-26.
 */

public class DemoApp extends Application {
    public static final String TAG = DemoApp.class.getSimpleName();
    private static DemoApp mInstance;
    private RequestQueue mRequestQueue;
    private PreferenceStore mStore;

    public static synchronized DemoApp getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        if (BuildConfig.DEBUG) {
            VolleyLog.DEBUG = true;
        }
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public PreferenceStore store() {
        if (mStore == null) {
            mStore = new PreferenceStore(this);
        }

        return mStore;
    }
}
