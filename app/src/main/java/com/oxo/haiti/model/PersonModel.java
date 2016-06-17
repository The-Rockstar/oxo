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
