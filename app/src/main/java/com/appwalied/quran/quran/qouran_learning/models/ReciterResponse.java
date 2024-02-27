package com.appwalied.quran.quran.qouran_learning.models;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Keep
public class ReciterResponse implements Serializable
{

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<Reciter> data = null;
    private final static long serialVersionUID = 4907189904257490516L;

    public ReciterResponse() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Reciter> getData() {
        return data;
    }

    public void setData(List<Reciter> data) {
        this.data = data;
    }

}