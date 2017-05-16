package com.phicomm.iot.library.device;

/**
 * Created by allen.z on 2017-05-15.
 */
public enum TYPE {
    NEW(-1, "New", true),
    ROOT(-2, "U", false),
    UNKOWW(0, "Unkown", false),
    SWITCHER(12102, "Switcher", true),
    PLUG(23701, "Plug", true),
    LIGHT(45772, "Light", true),
    HUMITURE(12335, "Humiture", false),
    FLAMMABLE(3835, "Flammable Gas", false),
    VOLTAGE(68574, "Voltage", false),
    REMOTE(2355, "Remote", true),
    PLUGS(47446, "Plugs", true),
    SOUNDBOX(43902, "Soundbox", true);

    private final int mSerial;

    private final String mString;

    private final boolean mIsLocalSupport;

    private TYPE(int serial, String string, boolean isLocalSupport) {
        this.mSerial = serial;
        this.mString = string;
        this.mIsLocalSupport = isLocalSupport;
    }

    /**
     * Check whether the type of device support local
     *
     * @return whether the type of device support local
     */
    public boolean isLocalSupport() {
        return this.mIsLocalSupport;
    }

    /**
     * Get the serial of the device type defined by Server
     *
     * @return the serial of the device type defined by Server
     */
    public int getSerial() {
        return mSerial;
    }

    @Override
    public String toString() {
        return this.mString;
    }

    /**
     * Get DeviceType by its serial
     *
     * @param serial the serial of the device type defined by Server
     * @return the DeviceType
     */
    public static TYPE getTypeEnumBySerial(int serial) {
        if (serial == NEW.getSerial()) {
            return NEW;
        } else if (serial == PLUG.getSerial()) {
            return PLUG;
        }else if (serial == SWITCHER.getSerial()) {
            return SWITCHER;
        } else if (serial == LIGHT.getSerial()) {
            return LIGHT;
        } else if (serial == HUMITURE.getSerial()) {
            return HUMITURE;
        } else if (serial == FLAMMABLE.getSerial()) {
            return FLAMMABLE;
        } else if (serial == VOLTAGE.getSerial()) {
            return VOLTAGE;
        } else if (serial == REMOTE.getSerial()) {
            return REMOTE;
        } else if (serial == ROOT.getSerial()) {
            return ROOT;
        } else if (serial == PLUGS.getSerial()) {
            return PLUGS;
        } else if (serial == SOUNDBOX.getSerial()) {
            return SOUNDBOX;
        }
        return UNKOWW;
    }

    /**
     * Get DeviceType by its typeEnum String
     *
     * @param typeEnumString the type enum String of the device
     * @return the DeviceType
     */
    public static TYPE getTypeEnumByString(String typeEnumString) {
        if (typeEnumString.equals(TYPE.NEW.toString())) {
            return NEW;
        } else if (typeEnumString.equals(TYPE.PLUG.toString())) {
            return PLUG;
        } else if (typeEnumString.equals(TYPE.SWITCHER.toString())) {
            return SWITCHER;
        }else if (typeEnumString.equals(TYPE.LIGHT.toString())) {
            return LIGHT;
        } else if (typeEnumString.equals(TYPE.HUMITURE.toString())) {
            return HUMITURE;
        } else if (typeEnumString.equals(TYPE.FLAMMABLE.toString())) {
            return FLAMMABLE;
        } else if (typeEnumString.equals(TYPE.VOLTAGE.toString())) {
            return VOLTAGE;
        } else if (typeEnumString.equals(TYPE.REMOTE.toString())) {
            return REMOTE;
        } else if (typeEnumString.equals(TYPE.ROOT.toString())) {
            return ROOT;
        } else if (typeEnumString.equals(TYPE.PLUGS.toString())) {
            return PLUGS;
        } else if (typeEnumString.equals(TYPE.SOUNDBOX.toString())) {
            return SOUNDBOX;
        }
        return UNKOWW;
    }

}
