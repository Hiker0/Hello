package com.phicomm.demo.device.light;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.phicomm.demo.R;
import com.phicomm.iot.library.widgets.LEDView;
import com.phicomm.iot.library.widgets.LightView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class LightFragment extends Fragment implements LightContract.View {
    private LightContract.Presenter mPlugPresenter;
    @BindView(R.id.progress)
    SeekBar progressView;;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.brightness)
    TextView brightView;
    public LightFragment() {
        // Required empty public constructor
    }

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_light, container, false);
    }

    @Override
    public void onViewCreated(android.view.View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        initView();
    }

    void initView(){

        progressView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    mPlugPresenter.setBrightness(progress);
                    brightView.setText(""+progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPlugPresenter.refresh();
                mRefresh.setRefreshing(false);
            }
        });

    }

    public static LightFragment newInstance() {
        return new LightFragment();
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
    public void setPresenter(LightContract.Presenter presenter) {
        mPlugPresenter = presenter;
    }

    @Override
    public void notifyState(int brightness) {
        progressView.setProgress(brightness);
        brightView.setText(""+brightness);
    }
}
