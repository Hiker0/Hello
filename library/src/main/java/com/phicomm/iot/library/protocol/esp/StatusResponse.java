package com.phicomm.iot.library.protocol.esp;

import com.google.gson.annotations.SerializedName;

/**
 * Author: allen.z
 * Date  : 2017-05-17
 * last modified: 2017-05-17
 */
public class StatusResponse {
    static class Response{
        int status;
    }
    @SerializedName("Response")
    Response mResponse = new Response();

    public int getStatus(){
        return  mResponse.status;
    }

    public void setStatus(int status){
        mResponse.status = status;
    }

}
