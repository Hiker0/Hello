package com.phicomm.iot.library.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.phicomm.iot.library.R;
import com.phicomm.iot.library.devices.switcher.ISwitcher;

/**
 * Created by allen.z on 2017-04-28.
 */
public class SwitcherView extends ImageView {
    Listener listener;
    boolean powered = false;

    public interface Listener {
        void onTurnOn();

        void onTurnOff();
    }

    public SwitcherView(Context context) {
        this(context, null);
    }

    public SwitcherView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitcherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        turnOff();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                float x = event.getX();
                if (x > 0 && x < getWidth() / 2 && powered) {
                    turnOff();
                    if (listener != null) {
                        listener.onTurnOff();
                    }
                } else if (x > getWidth() / 2 && x < getWidth() && !powered) {
                    turnOn();
                    if (listener != null) {
                        listener.onTurnOn();
                    }
                }
                break;
        }
        return true;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void turnOn() {
        setImageResource(R.drawable.switcher_on);
        powered = true;
    }

    public void turnOff() {
        setImageResource(R.drawable.switcher_off);
        powered = false;
    }

    public boolean isOn() {
        return powered;
    }
}
