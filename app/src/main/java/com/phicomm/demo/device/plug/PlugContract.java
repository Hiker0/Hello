package com.phicomm.demo.device.plug;

import com.phicomm.demo.BasePresenter;
import com.phicomm.demo.BaseView;

/**
 * Created by hzn on 17-5-11.
 */

public interface PlugContract {
    interface Presenter extends BasePresenter {
        void switchState();
    }

    interface View extends BaseView<Presenter> {
        void showState(boolean isOn);
    }
}
