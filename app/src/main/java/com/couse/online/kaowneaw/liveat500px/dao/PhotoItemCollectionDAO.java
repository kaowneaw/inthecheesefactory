package com.couse.online.kaowneaw.liveat500px.dao;

import android.widget.ListView;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Kaowneaw on 4/17/2017.
 */

public class PhotoItemCollectionDAO {

    @SerializedName("success")
    private boolean success;
    @SerializedName("data")
    private List<PhotoItemDAO> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<PhotoItemDAO> getData() {
        return data;
    }

    public void setData(List<PhotoItemDAO> data) {
        this.data = data;
    }
}
