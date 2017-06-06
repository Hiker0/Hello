package com.phicomm.demo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Date;

/**
 * Created by hzn on 17-6-6.
 */

public class PreferenceStore {
    private final Context mContext;

    public PreferenceStore(Context context) {
        mContext = context;
    }

    public String getIotToken() {
        SharedPreferences preference = mContext.getSharedPreferences("User-Info", Context.MODE_PRIVATE);
        return preference.getString("iot-token", "");
    }

    public void setIotToken(String token) {
        SharedPreferences preference = mContext.getSharedPreferences("User-Info", Context.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.putString("iot-token", token);
        editor.apply();
    }

    public void setPhiToken(String token, int expire_seconds) {
        SharedPreferences preferences = mContext.getSharedPreferences("User-Info", Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString("phi-token", token);
        long expired_date_in_seconds = DateUtils.date2seconds(new Date()) + expire_seconds;
        editor.putLong("phi-token-expired-date", expired_date_in_seconds);

        editor.apply();
    }

    public String getPhiToken() {
        SharedPreferences preferences = mContext.getSharedPreferences("User-Info", Context.MODE_PRIVATE);
        String phiToken = preferences.getString("phi-token", "");
        long expiredDate = preferences.getLong("phi-token-expired-date", 0);
        if (!phiToken.isEmpty() && expiredDate > DateUtils.date2seconds(new Date())) {
            return phiToken;
        }
        return "";
    }
}
