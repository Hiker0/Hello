package com.phicomm.iot.library.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.phicomm.iot.library.R;
import com.phicomm.iot.library.devices.switcher.ISwitcher;

/**
 * Created by allen.z on 2017-04-28.
 */
public class LightView extends ImageView{

    private boolean isOn= false;
    public LightView(Context context) {
        this(context,null);
    }

    public LightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        turnOff();
    }

    public LightView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setBrightness(int level){
       float  alpha = level * 0.01f;
        setAlpha(alpha);
    }

    public void turnOn(){
        setImageResource(R.drawable.light_on);
        isOn = true;
    }

    public void turnOff(){
        setImageResource(R.drawable.light_off);
        isOn = false;
    }

    public boolean isOn(){
        return isOn;
    }

}
