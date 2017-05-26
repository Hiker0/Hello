package com.phicomm.demo.device.light;

import com.phicomm.demo.BasePresenter;
import com.phicomm.demo.BaseView;

/**
 * Created by hzn on 17-5-11.
 */

public interface LightContract {
    interface Presenter extends BasePresenter {
        void setBrightness(int brightness);
        void refresh();
    }

    interface View extends BaseView<Presenter> {
        void notifyState(int brightness);
    }
}
