package com.oxo.haiti.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by jaswinderwadali on 15/06/16.
 */
public class XBlood implements Serializable {

    @SerializedName("B2")
    @Expose
    private String B2;


    @SerializedName("B3")
    @Expose
    private String B3;

    @SerializedName("B5")
    @Expose
    private String B5;

    @SerializedName("B6")
    @Expose
    private String B6;

    @SerializedName("B8")
    @Expose
    private String B8;

    @SerializedName("B9")
    @Expose
    private String B9;

    @SerializedName("B11")
    @Expose
    private String B11;

    @SerializedName("B12")
    @Expose
    private String B12;

    @SerializedName("B13")
    @Expose
    private String B13;

    public String getB2() {
        return B2;
    }

    public void setB2(String b2) {
        B2 = b2;
    }

    public String getB3() {
        return B3;
    }

    public void setB3(String b3) {
        B3 = b3;
    }

    public String getB5() {
        return B5;
    }

    public void setB5(String b5) {
        B5 = b5;
    }

    public String getB6() {
        return B6;
    }

    public void setB6(String b6) {
        B6 = b6;
    }

    public String getB8() {
        return B8;
    }

    public void setB8(String b8) {
        B8 = b8;
    }

    public String getB9() {
        return B9;
    }

    public void setB9(String b9) {
        B9 = b9;
    }

    public String getB11() {
        return B11;
    }

    public void setB11(String b11) {
        B11 = b11;
    }

    public String getB12() {
        return B12;
    }

    public void setB12(String b12) {
        B12 = b12;
    }

    public String getB13() {
        return B13;
    }

    public void setB13(String b13) {
        B13 = b13;
    }
}
