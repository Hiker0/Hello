package com.phicomm.demo.device.switcher;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.phicomm.demo.R;
import com.phicomm.demo.device.switcher.SwitcherContract.Presenter;
import com.phicomm.iot.library.widgets.LEDView;
import com.phicomm.iot.library.widgets.SwitcherView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SwitcherFragment extends Fragment implements SwitcherContract.View {
    private Presenter mPlugPresenter;
    @BindView(R.id.switcher)
    SwitcherView mSwitcherView;
    @BindView(R.id.led)
    LEDView mLedView;
    @BindView(R.id.logo)
    ImageView mLogoView;

    public SwitcherFragment() {
        // Required empty public constructor
    }

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_switcher, container, false);
    }

    @Override
    public void onViewCreated(android.view.View view, @Nullable Bundle savedInstanceState) {
//        ButterKnife.bind(this, view);
        mSwitcherView = (SwitcherView) view.findViewById(R.id.switcher);
        mLedView = (LEDView) view.findViewById(R.id.led);
        initView();
    }

    void initView(){

        mSwitcherView.setListener(new SwitcherView.Listener() {
            @Override
            public void onTurnOn() {
                mPlugPresenter.turnOn();
            }

            @Override
            public void onTurnOff() {
                mPlugPresenter.turnOff();
            }
        });
    }

    public static SwitcherFragment newInstance() {
        return new SwitcherFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPlugPresenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPlugPresenter.stop();
    }

    @Override
    public void setPresenter(SwitcherContract.Presenter presenter) {
        mPlugPresenter = presenter;
    }

    @Override
    public void notifyState(boolean on) {
        if(mLedView == null || mSwitcherView ==null){
            return;
        }

        if(on){
            mLedView.turnOn();
            mSwitcherView.turnOn();
        }else{
            mLedView.turnOff();
            mSwitcherView.turnOff();
        }
    }
}
