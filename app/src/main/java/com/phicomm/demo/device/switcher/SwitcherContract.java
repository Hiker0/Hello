package com.phicomm.demo.device.switcher;

import com.phicomm.demo.BasePresenter;
import com.phicomm.demo.BaseView;
import com.phicomm.iot.library.device.switcher.ISwitcher;

/**
 * Created by hzn on 17-5-11.
 */

public interface SwitcherContract {
    interface Presenter extends BasePresenter , ISwitcher {

    }

    interface View extends BaseView<Presenter>{
        void notifyState(boolean on);
    }
}
