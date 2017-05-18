package com.phicomm.iot.library.protocol.esp;

import com.google.gson.annotations.SerializedName;

/**
 * Author: allen.z
 * Date  : 2017-05-17
 * last modified: 2017-05-17
 */
public class Info {
    static public class Device {
        String product;
        String manufacturer;

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
        }
    }

    static public class Version {
        @SerializedName("hardware")
        String hardWare;
        @SerializedName("sdk_version")
        String sdkVersion;
        @SerializedName("iot_version")
        String iotVersion;

        public String getHardWare() {
            return hardWare;
        }

        public void setHardWare(String hardWare) {
            this.hardWare = hardWare;
        }

        public String getSdkVersion() {
            return sdkVersion;
        }

        public void setSdkVersion(String sdkVersion) {
            this.sdkVersion = sdkVersion;
        }

        public String getIotVersion() {
            return iotVersion;
        }

        public void setIotVersion(String iotVersion) {
            this.iotVersion = iotVersion;
        }
    }
    @SerializedName("Version")
    Version mVersion;
    @SerializedName("Device")
    Device mDevice;

    public Version getVersion() {
        return mVersion;
    }

    public void setVersion(Version mVersion) {
        this.mVersion = mVersion;
    }

    public Device getDevice() {
        return mDevice;
    }

    public void setDevice(Device mDevice) {
        this.mDevice = mDevice;
    }
}
