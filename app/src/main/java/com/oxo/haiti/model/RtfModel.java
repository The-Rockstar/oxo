package com.oxo.haiti.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jaswinderwadali on 10/06/16.
 */
public class RtfModel {

    @SerializedName("_id")
    @Expose
    private String userId;

    /**
     * @return The questionType
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId The question_type
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }


    @SerializedName("name")
    @Expose
    private String name;

    /**
     * @return The questionType
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The question_type
     */
    public void setName(String name) {
        this.name = name;
    }
    @SerializedName("survey_id")
    @Expose
    private String surveyId;

    /**
     * @return The questionType
     */
    public String getSurveyId() {
        return surveyId;
    }

    /**
     * @param surveyId The question_type
     */
    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }


}
