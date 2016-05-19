package com.oxo.haiti.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jaswinderwadali on 19/05/16.
 */
public class CommonModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("messages")
    @Expose
    private Messages messages;

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The messages
     */
    public Messages getMessages() {
        return messages;
    }

    /**
     * @param messages The messages
     */
    public void setMessages(Messages messages) {
        this.messages = messages;
    }


    public class Messages {

        @SerializedName("m1")
        @Expose
        private String m1;

        /**
         * @return The m1
         */
        public String getM1() {
            return m1;
        }

        /**
         * @param m1 The m1
         */
        public void setM1(String m1) {
            this.m1 = m1;
        }

    }
}
