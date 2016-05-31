package com.oxo.haiti.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaswinderwadali on 19/05/16.
 */
public class AnswerModel implements Serializable {

    @SerializedName("survery_id")
    @Expose
    private String surveryId;
    @SerializedName("survery_pid")
    @Expose
    private String surveryPid;
    @SerializedName("suvery_answers")
    @Expose
    private List<SuveryAnswer> suveryAnswers = new ArrayList<>();
    @SerializedName("stop_status")
    @Expose
    private String status;

    /**
     * @return The answer
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The answer
     */
    public void setStatus(String status) {
        this.status = status;
    }


    /**
     * @return The surveryId
     */
    public String getSurveryId() {
        return surveryId;
    }

    /**
     * @param surveryId The survery_id
     */
    public void setSurveryId(String surveryId) {
        this.surveryId = surveryId;
    }

    /**
     * @return The surveryPid
     */
    public String getSurveryPid() {
        return surveryPid;
    }

    /**
     * @param surveryPid The survery_pid
     */
    public void setSurveryPid(String surveryPid) {
        this.surveryPid = surveryPid;
    }

    /**
     * @return The suveryAnswers
     */
    public List<SuveryAnswer> getSuveryAnswers() {
        return suveryAnswers;
    }

    /**
     * @param suveryAnswers The suvery_answers
     */
    public void setSuveryAnswers(List<SuveryAnswer> suveryAnswers) {
        this.suveryAnswers = suveryAnswers;
    }


    public static class SuveryAnswer  implements Serializable{

        @SerializedName("question_id")
        @Expose
        private String questionId;
        @SerializedName("question_key")
        @Expose
        private String questionKey;
        @SerializedName("answer")
        @Expose
        private String answer;

        @SerializedName("next_id")
        @Expose
        private int next_id = 0;

        /**
         * @return The nextid
         */
        public int getNextId() {
            return next_id;
        }

        /**
         * @param next_id The survery_id
         */
        public void setNextId(int next_id) {
            this.next_id = next_id;
        }


        /**
         * @return The questionId
         */
        public String getQuestionId() {
            return questionId;
        }

        /**
         * @param questionId The question_id
         */
        public void setQuestionId(String questionId) {
            this.questionId = questionId;
        }

        /**
         * @return The questionKey
         */
        public String getQuestionKey() {
            return questionKey;
        }

        /**
         * @param questionKey The question_key
         */
        public void setQuestionKey(String questionKey) {
            this.questionKey = questionKey;
        }

        /**
         * @return The answer
         */
        public String getAnswer() {
            return answer;
        }

        /**
         * @param answer The answer
         */
        public void setAnswer(String answer) {
            this.answer = answer;
        }


    }

}
