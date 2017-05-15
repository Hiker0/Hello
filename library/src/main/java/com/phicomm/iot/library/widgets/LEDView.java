package com.phicomm.iot.library.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.phicomm.iot.library.R;
import com.phicomm.iot.library.device.switcher.ISwitcher;

/**
 * Created by allen.z on 2017-04-28.
 */
public class LEDView extends ImageView{

    private boolean isOn= false;
    public LEDView(Context context) {
        this(context,null);
    }

    public LEDView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        turnOff();
    }

    public LEDView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void turnOn(){
        setImageResource(R.drawable.led_on);
        isOn = true;
    }

    public void turnOff(){
        setImageResource(R.drawable.led_off);
        isOn = false;
    }

    public boolean isOn(){
        return isOn;
    }

}
