package com.oxo.haiti.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaswinderwadali on 18/05/16.
 */
public class QuestionsModel {

    @SerializedName("question_key")
    @Expose
    private String questionKey;
    @SerializedName("question_id")
    @Expose
    private String questionId;
    @SerializedName("question_order")
    @Expose
    private Integer questionOrder;
    @SerializedName("question_text")
    @Expose
    private String questionText;
    @SerializedName("question_desc")
    @Expose
    private String questionDesc;
    @SerializedName("question_type")
    @Expose
    private String questionType;
    @SerializedName("question_message")
    @Expose
    private String questionMessage;

    @SerializedName("min")
    @Expose
    private int min = 0;

    @SerializedName("max")
    @Expose
    private int max = 99999999;


    @SerializedName("answers")
    @Expose
    private List<Answer> answers = new ArrayList<>();


    @SerializedName("pattern")
    @Expose
    private String pattern;

    @SerializedName("match_with")
    @Expose
    private String match_with;


    public String getMatch_with() {
        return match_with;
    }

    public void setMatch_with(String match_with) {
        this.match_with = match_with;
    }

    public String getPattern() {
        return pattern;
    }


    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * @return The questionKey
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min The question_key
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return The max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max The question_key
     */
    public void setMax(int max) {
        this.max = max;
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
     * @return The questionOrder
     */
    public Integer getQuestionOrder() {
        return questionOrder;
    }

    /**
     * @param questionOrder The question_order
     */
    public void setQuestionOrder(Integer questionOrder) {
        this.questionOrder = questionOrder;
    }

    /**
     * @return The questionText
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * @param questionText The question_text
     */
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    /**
     * @return The questionDesc
     */
    public String getQuestionDesc() {
        return questionDesc;
    }

    /**
     * @param questionDesc The question_desc
     */
    public void setQuestionDesc(String questionDesc) {
        this.questionDesc = questionDesc;
    }

    /**
     * @return The questionType
     */
    public String getQuestionType() {
        return questionType;
    }

    /**
     * @param questionType The question_type
     */
    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    /**
     * @return The questionMessage
     */
    public String getQuestionMessage() {
        return questionMessage;
    }

    /**
     * @param questionMessage The question_message
     */
    public void setQuestionMessage(String questionMessage) {
        this.questionMessage = questionMessage;
    }

    /**
     * @return The answers
     */
    public List<Answer> getAnswers() {
        return answers;
    }

    /**
     * @param answers The answers
     */
    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public static class Answer {

        @SerializedName("option_type")
        @Expose
        private String optionType;
        @SerializedName("option_text")
        @Expose
        private String optionText;
        @SerializedName("option_value")
        @Expose
        private String optionValue;
        @SerializedName("option_next")
        @Expose
        private int optionNext;
        @SerializedName("option_prompt")
        @Expose
        private Object optionPrompt;
        @SerializedName("min")
        @Expose
        private int min = 0;
        @SerializedName("max")
        @Expose
        private int max = 99999999;


        @SerializedName("option_status")
        @Expose
        private Object optionStatus;

        @SerializedName("repeat")
        @Expose
        private boolean repeat;

        /**
         * @return The questionKey
         */
        public boolean isRepeater() {
            return repeat;
        }

        /**
         * @param repeat The question_key
         *               this will false if there is no param in json form api
         */
        public void setRepeat(boolean repeat) {
            this.repeat = repeat;
        }

        /**
         * @return The optionType
         */
        public String getOptionType() {
            return optionType;
        }

        /**
         * @param optionType The option_type
         */
        public void setOptionType(String optionType) {
            this.optionType = optionType;
        }

        /**
         * @return The optionText
         */
        public String getOptionText() {
            return optionText;
        }

        /**
         * @param optionText The option_text
         */
        public void setOptionText(String optionText) {
            this.optionText = optionText;
        }

        /**
         * @return The optionValue
         */
        public String getOptionValue() {
            return optionValue;
        }

        /**
         * @param optionValue The option_value
         */
        public void setOptionValue(String optionValue) {
            this.optionValue = optionValue;
        }

        /**
         * @return The optionNext
         */
        public int getOptionNext() {
            return optionNext;
        }

        /**
         * @param optionNext The option_next
         */
        public void setOptionNext(int optionNext) {
            this.optionNext = optionNext;
        }

        /**
         * @return The optionPrompt
         */
        public Object getOptionPrompt() {
            return optionPrompt;
        }

        /**
         * @param optionPrompt The option_prompt
         */
        public void setOptionPrompt(Object optionPrompt) {
            this.optionPrompt = optionPrompt;
        }

        /**
         * @return The questionKey
         */
        public int getMin() {
            return min;
        }

        /**
         * @param min The question_key
         */
        public void setMin(int min) {
            this.min = min;
        }

        /**
         * @return The max
         */
        public int getMax() {
            return max;
        }

        /**
         * @param max The question_key
         */
        public void setMax(int max) {
            this.max = max;
        }

        /**
         * @return The max
         */
        public Object getOptionStatus() {
            return optionStatus;
        }

        /**
         * @param optionStatus The question_key
         */
        public void setMax(Object optionStatus) {
            this.optionStatus = optionStatus;
        }


    }


}
