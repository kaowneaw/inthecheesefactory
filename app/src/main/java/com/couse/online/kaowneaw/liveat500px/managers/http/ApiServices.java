package com.couse.online.kaowneaw.liveat500px.managers.http;

import com.couse.online.kaowneaw.liveat500px.dao.PhotoItemCollectionDAO;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Kaowneaw on 4/17/2017.
 */

public interface ApiServices {

    @POST("list")
    Call<PhotoItemCollectionDAO> loadPhotoList();

    @GET("list/after/{id}")
    Call<PhotoItemCollectionDAO> loadPhotoListAfterId(@Path("id") int id);

    @GET("list/before/{id}")
    Call<PhotoItemCollectionDAO> loadPhotoListBeforeId(@Path("id") int id);
}
