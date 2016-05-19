package com.oxo.haiti.service;

import com.oxo.haiti.model.CommonModel;
import com.oxo.haiti.model.QuestionsModel;
import com.oxo.haiti.model.UsersModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jaswinderwadali on 5/16/2016.
 */
public interface ApiService {

    @GET("?action=activate&apikey=CAX4RYDJBUKM8BSYQEZP")
    Call<UsersModel> getUsers();


    @GET("?action=import&apikey=CAX4RYDJBUKM8BSYQEZP")
    Call<List<QuestionsModel>> getFirstSurvey(@Query("id") String id);

    @FormUrlEncoded
    @POST("?action=export&apikey=CAX4RYDJBUKM8BSYQEZP")
    Call<CommonModel> syncData(@Field("data") String data);





}
