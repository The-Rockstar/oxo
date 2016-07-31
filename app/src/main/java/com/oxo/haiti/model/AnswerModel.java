package com.oxo.haiti.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by jaswinderwadali on 19/05/16.
 */
public class AnswerModel implements Serializable {

    @SerializedName("survey_id")
    @Expose
    private String surveryId;
    @SerializedName("survey_pid")
    @Expose
    private String surveryPid;
    @SerializedName("survey_answers")
    @Expose
    private Stack<SuveryAnswer> suveryAnswers = new Stack<>();
    @SerializedName("stop_status")
    @Expose
    private String status;

    @SerializedName("generated_survey_id")
    @Expose
    private String generated_survey;


    @SerializedName("area_model")
    @Expose
    private AreaModel areaModel;

    public AreaModel getAreaModel() {
        return areaModel;
    }

    public void setAreaModel(AreaModel areaModel) {
        this.areaModel = areaModel;
    }

    public String getGenerated_survey() {
        return generated_survey;
    }

    public void setGenerated_survey(String generated_survey) {
        this.generated_survey = generated_survey;
    }

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
    public Stack<SuveryAnswer> getSuveryAnswers() {
        return suveryAnswers;
    }

    /**
     * @param suveryAnswers The suvery_answers
     */
    public void setSuveryAnswers(Stack<SuveryAnswer> suveryAnswers) {
        this.suveryAnswers = suveryAnswers;
    }


    public static class SuveryAnswer implements Serializable {

        @SerializedName("question_id")
        @Expose
        private String questionId;
        @SerializedName("question_key")
        @Expose
        private String questionKey;
        @SerializedName("answer")
        @Expose
        private String answer;


        @SerializedName("extra")
        @Expose
        private List<String> extra= new ArrayList<>();

        public List<String> getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra.add(extra);
        }

        @SerializedName("next_id")
        @Expose
        private int next_id = 0;


        @SerializedName("loaded")
        @Expose
        private boolean isLoaded = false;

        @SerializedName("loadedAge")
        @Expose
        private boolean isLoadedAge = false;

        @SerializedName("loadedName")
        @Expose
        private boolean isLoadedName = false;

        @SerializedName("loadedSex")
        @Expose
        private boolean isLoadedSex = false;


        @SerializedName("loopCount")
        @Expose
        private int loopCount = 0;

        @SerializedName("loopNumber")
        @Expose
        private int loopNumber = 0;

        @SerializedName("x")
        @Expose
        private boolean dob_;

        @SerializedName("y")
        @Expose
        private boolean dod_;

        @SerializedName("z")
        @Expose
        private boolean die_when_ansent_;


        @SerializedName("q")
        @Expose
        private boolean die_after_six_week_abortion_;

        @SerializedName("w")
        @Expose
        private boolean die_with_tb_drug_;


        public boolean isDob_() {
            return dob_;
        }

        public void setDob_(boolean dob_) {
            this.dob_ = dob_;
        }

        public boolean isDod_() {
            return dod_;
        }

        public void setDod_(boolean dod_) {
            this.dod_ = dod_;
        }

        public boolean isDie_when_ansent_() {
            return die_when_ansent_;
        }

        public void setDie_when_ansent_(boolean die_when_ansent_) {
            this.die_when_ansent_ = die_when_ansent_;
        }

        public boolean isDie_after_six_week_abortion_() {
            return die_after_six_week_abortion_;
        }

        public void setDie_after_six_week_abortion_(boolean die_after_six_week_abortion_) {
            this.die_after_six_week_abortion_ = die_after_six_week_abortion_;
        }

        public boolean isDie_with_tb_drug_() {
            return die_with_tb_drug_;
        }

        public void setDie_with_tb_drug_(boolean die_with_tb_drug_) {
            this.die_with_tb_drug_ = die_with_tb_drug_;
        }


        public int getNext_id() {
            return next_id;
        }

        public void setNext_id(int next_id) {
            this.next_id = next_id;
        }

        public int getLoopCount() {
            return loopCount;
        }

        public void setLoopCount(int loopCount) {
            this.loopCount = loopCount;
        }

        public int getLoopNumber() {
            return loopNumber;
        }

        public void setLoopNumber(int loopNumber) {
            this.loopNumber = loopNumber;
        }

        public boolean isLoadedAge() {
            return isLoadedAge;
        }

        public void setLoadedAge(boolean loadedAge) {
            isLoadedAge = loadedAge;
        }

        public boolean isLoadedName() {
            return isLoadedName;
        }

        public void setLoadedName(boolean loadedName) {
            isLoadedName = loadedName;
        }

        public boolean isLoadedSex() {
            return isLoadedSex;
        }

        public void setLoadedSex(boolean loadedSex) {
            isLoadedSex = loadedSex;
        }

        public boolean isLoaded() {

            return isLoaded;
        }

        public void setLoaded(boolean loaded) {
            isLoaded = loaded;
        }

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
