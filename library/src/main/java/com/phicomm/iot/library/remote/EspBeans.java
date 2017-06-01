package com.phicomm.iot.library.remote;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Author: allen.z
 * Date  : 2017-05-27
 * last modified: 2017-05-27
 */

public class EspBeans {
    static class IdentifyBean{

        /**
         * path : /v1/device/identify/
         * method : POST
         * meta : {"Authorization":"token HERE_IS_THE_MASTER_DEVICE_KEY"}
         */

        private String path;
        private String method;
        private MetaBean meta;

        IdentifyBean(String token){
            path = "/v1/device/identify/";
            method = "POST";
            meta = new MetaBean(token);
        }

    }

    static class PingBean{

        /**
         * path : /v1/ping/
         * method : GET
         * meta : {"Authorization":"token HERE_IS_THE_DEVICE_KEY"}
         */

        private String path;
        private String method;
        private MetaBean meta;

        PingBean(String token){
            path = "/v1/ping/";
            method = "GET";
            meta = new MetaBean(token);
        }
    }


    public static class MetaBean {
        /**
         * Authorization : token HERE_IS_THE_DEVICE_KEY
         */
        private String Authorization;
        MetaBean(String token){
            Authorization="token " +token;
        }
    }

    public static  class DataPoint<T>{

        /**
         * path : /v1/datastreams/:stream_name/datapoints/
         * method : POST
         * meta : {"Authorization":"token HERE_IS_THE_DEVICE_KEY"}
         * body : {"datapoints":[{"at":"2014-05-05 17:31:23","x":1}]}
         */

        private String path;
        private String method;
        private MetaBean meta;
        private T body;

        public DataPoint(String token, String streamName, T body){
            method = "POST";
            path = "/v1/datastreams/"+streamName+"/datapoints/";
            meta = new MetaBean(token);
            this.body = body;
        }
    }

    static class IdentifyResBean{

        /**
         * datetime : 2017-05-31 15:03:07
         * device : {"id":158324,"created":"2017-05-22 16:46:41","updated":"2017-05-27 21 :04:37","visibly":1,"ptype":23701,"product_id":4567,"productbatch_id":0,"name":"switcher","description":"","serial":"15aef2d0","bssid":"","is_private":0,"is_frozen":0,"status":1,"metadata":"","key_id":199777,"loca tion":"","last_push":"0001-01-01 08:05:57","last_pull":"0001-01-01 08:05:57","last_active":"2017-05-27 21:02:37","last_activated_at":"0001-01-01 08:05:57","activate_status":0,"activated_at":"0001-01-01 08:05:57","rom_version":"","latest_rom_version":""}
         * message : device identified
         * status : 200
         */

        private String datetime;
        private DeviceBean device;
        private String message;
        private int status;

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public DeviceBean getDevice() {
            return device;
        }

        public void setDevice(DeviceBean device) {
            this.device = device;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public static class DeviceBean {
            /**
             * id : 158324
             * created : 2017-05-22 16:46:41
             * updated : 2017-05-27 21 :04:37
             * visibly : 1
             * ptype : 23701
             * product_id : 4567
             * productbatch_id : 0
             * name : switcher
             * description :
             * serial : 15aef2d0
             * bssid :
             * is_private : 0
             * is_frozen : 0
             * status : 1
             * metadata :
             * key_id : 199777
             * loca tion :
             * last_push : 0001-01-01 08:05:57
             * last_pull : 0001-01-01 08:05:57
             * last_active : 2017-05-27 21:02:37
             * last_activated_at : 0001-01-01 08:05:57
             * activate_status : 0
             * activated_at : 0001-01-01 08:05:57
             * rom_version :
             * latest_rom_version :
             */

            private int id;
            private String created;
            private String updated;
            private int visibly;
            private int ptype;
            private int product_id;
            private int productbatch_id;
            private String name;
            private String description;
            private String serial;
            private String bssid;
            private int is_private;
            private int is_frozen;
            private int status;
            private String metadata;
            private int key_id;
            @SerializedName("loca tion")
            private String _$LocaTion194; // FIXME check this code
            private String last_push;
            private String last_pull;
            private String last_active;
            private String last_activated_at;
            private int activate_status;
            private String activated_at;
            private String rom_version;
            private String latest_rom_version;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCreated() {
                return created;
            }

            public void setCreated(String created) {
                this.created = created;
            }

            public String getUpdated() {
                return updated;
            }

            public void setUpdated(String updated) {
                this.updated = updated;
            }

            public int getVisibly() {
                return visibly;
            }

            public void setVisibly(int visibly) {
                this.visibly = visibly;
            }

            public int getPtype() {
                return ptype;
            }

            public void setPtype(int ptype) {
                this.ptype = ptype;
            }

            public int getProduct_id() {
                return product_id;
            }

            public void setProduct_id(int product_id) {
                this.product_id = product_id;
            }

            public int getProductbatch_id() {
                return productbatch_id;
            }

            public void setProductbatch_id(int productbatch_id) {
                this.productbatch_id = productbatch_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getSerial() {
                return serial;
            }

            public void setSerial(String serial) {
                this.serial = serial;
            }

            public String getBssid() {
                return bssid;
            }

            public void setBssid(String bssid) {
                this.bssid = bssid;
            }

            public int getIs_private() {
                return is_private;
            }

            public void setIs_private(int is_private) {
                this.is_private = is_private;
            }

            public int getIs_frozen() {
                return is_frozen;
            }

            public void setIs_frozen(int is_frozen) {
                this.is_frozen = is_frozen;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getMetadata() {
                return metadata;
            }

            public void setMetadata(String metadata) {
                this.metadata = metadata;
            }

            public int getKey_id() {
                return key_id;
            }

            public void setKey_id(int key_id) {
                this.key_id = key_id;
            }

            public String get_$LocaTion194() {
                return _$LocaTion194;
            }

            public void set_$LocaTion194(String _$LocaTion194) {
                this._$LocaTion194 = _$LocaTion194;
            }

            public String getLast_push() {
                return last_push;
            }

            public void setLast_push(String last_push) {
                this.last_push = last_push;
            }

            public String getLast_pull() {
                return last_pull;
            }

            public void setLast_pull(String last_pull) {
                this.last_pull = last_pull;
            }

            public String getLast_active() {
                return last_active;
            }

            public void setLast_active(String last_active) {
                this.last_active = last_active;
            }

            public String getLast_activated_at() {
                return last_activated_at;
            }

            public void setLast_activated_at(String last_activated_at) {
                this.last_activated_at = last_activated_at;
            }

            public int getActivate_status() {
                return activate_status;
            }

            public void setActivate_status(int activate_status) {
                this.activate_status = activate_status;
            }

            public String getActivated_at() {
                return activated_at;
            }

            public void setActivated_at(String activated_at) {
                this.activated_at = activated_at;
            }

            public String getRom_version() {
                return rom_version;
            }

            public void setRom_version(String rom_version) {
                this.rom_version = rom_version;
            }

            public String getLatest_rom_version() {
                return latest_rom_version;
            }

            public void setLatest_rom_version(String latest_rom_version) {
                this.latest_rom_version = latest_rom_version;
            }
        }
    }
}
