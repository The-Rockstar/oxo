package com.oxo.haiti.service;

import com.oxo.haiti.model.QuestionsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jaswinderwadali on 08/06/16.
 */
public interface ApiServiceMain {

    @GET("?action=import&apikey=CAX4RYDJBUKM8BSYQEZP")
    Call<List<QuestionsModel>> getFirstSurvey(@Query("id") String id);

}
