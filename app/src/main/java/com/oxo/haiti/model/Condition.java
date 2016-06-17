package com.oxo.haiti.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaswinderwadali on 15/06/16.
 */
public class Condition  implements Serializable{

    @SerializedName("question_id")
    @Expose
    private String question_id;
    @SerializedName("value")
    @Expose
    private Value value;

    /**
     *
     * @return
     * The key
     */
    public String getKey() {
        return question_id;
    }

    /**
     *
     * The key
     */
    public void setKey(String question_id) {
        this.question_id = question_id;
    }

    /**
     *
     * @return
     * The value
     */
    public Value getValue() {
        return value;
    }

    /**
     *
     * @param value
     * The value
     */
    public void setValue(Value value) {
        this.value = value;
    }


    public static class Value implements Serializable{

        @SerializedName("relation")
        @Expose
        private String relation;
        @SerializedName("question_next")
        @Expose
        private Integer questionNext;
        @SerializedName("conditions")
        @Expose
        private List<ConditionMode> conditions = new ArrayList<>();

        /**
         *
         * @return
         * The relation
         */
        public String getRelation() {
            return relation;
        }

        /**
         *
         * @param relation
         * The relation
         */
        public void setRelation(String relation) {
            this.relation = relation;
        }

        /**
         *
         * @return
         * The questionNext
         */
        public Integer getQuestionNext() {
            return questionNext;
        }

        /**
         *
         * @param questionNext
         * The question_next
         */
        public void setQuestionNext(Integer questionNext) {
            this.questionNext = questionNext;
        }

        /**
         *
         * @return
         * The conditions
         */
        public List<ConditionMode> getConditions() {
            return conditions;
        }

        /**
         *
         * @param conditions
         * The conditions
         */
        public void setConditions(List<ConditionMode> conditions) {
            this.conditions = conditions;
        }

    }

    public static  class ConditionMode implements Serializable{

        @SerializedName("question_order")
        @Expose
        private String questionOrder;
        @SerializedName("value")
        @Expose
        private Integer value;
        @SerializedName("compare")
        @Expose
        private String compare;

        /**
         *
         * @return
         * The questionOrder
         */
        public String getQuestionOrder() {
            return questionOrder;
        }

        /**
         *
         * @param questionOrder
         * The question_order
         */
        public void setQuestionOrder(String questionOrder) {
            this.questionOrder = questionOrder;
        }

        /**
         *
         * @return
         * The value
         */
        public Integer getValue() {
            return value;
        }

        /**
         *
         * @param value
         * The value
         */
        public void setValue(Integer value) {
            this.value = value;
        }

        /**
         *
         * @return
         * The compare
         */
        public String getCompare() {
            return compare;
        }

        /**
         *
         * @param compare
         * The compare
         */
        public void setCompare(String compare) {
            this.compare = compare;
        }

    }


}
