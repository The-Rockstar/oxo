package com.oxo.haiti.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jaswinderwadali on 14/06/16.
 */
public class PersonModel {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("sex")
    @Expose
    private String sex;

    @SerializedName("age")
    @Expose
    private String age;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("typo")
    @Expose
    private String typo;

    @SerializedName("loopCount")
    @Expose
    private Integer loopCount;



    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("dod")
    @Expose
    private String dod;

    @SerializedName("die_when_ansent")
    @Expose
    private String die_when_ansent;


    @SerializedName("die_after_six_week_abortion")
    @Expose
    private String die_after_six_week_abortion;

    @SerializedName("die_with_tb_drug")
    @Expose
    private String die_with_tb_drug;

    @SerializedName("is_seleted")
    @Expose
    private boolean is_seleted;


    public boolean is_seleted() {
        return is_seleted;
    }

    public void setIs_seleted(boolean is_seleted) {
        this.is_seleted = is_seleted;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDod() {
        return dod;
    }

    public void setDod(String dod) {
        this.dod = dod;
    }

    public String getDie_when_ansent() {
        return die_when_ansent;
    }

    public void setDie_when_ansent(String die_when_ansent) {
        this.die_when_ansent = die_when_ansent;
    }

    public String getDie_after_six_week_abortion() {
        return die_after_six_week_abortion;
    }

    public void setDie_after_six_week_abortion(String die_after_six_week_abortion) {
        this.die_after_six_week_abortion = die_after_six_week_abortion;
    }

    public String getDie_with_tb_drug() {
        return die_with_tb_drug;
    }

    public void setDie_with_tb_drug(String die_with_tb_drug) {
        this.die_with_tb_drug = die_with_tb_drug;
    }

    public Integer getLoopCount() {
        return loopCount;
    }

    public void setLoopCount(Integer loopCount) {
        this.loopCount = loopCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypo() {
        return typo;
    }

    public void setTypo(String typo) {
        this.typo = typo;
    }
}
