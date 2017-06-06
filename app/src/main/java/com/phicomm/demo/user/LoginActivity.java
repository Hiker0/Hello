package com.phicomm.demo.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.phicomm.demo.R;
import com.phicomm.demo.util.ActivityUtils;

public class LoginActivity extends AppCompatActivity {
    private UserPresenter mUserPresenter;

    public static void start(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.layout_single_container);
        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), loginFragment, R.id.layout_single_container);
        }

        mUserPresenter = new UserPresenter(loginFragment);
    }
}
