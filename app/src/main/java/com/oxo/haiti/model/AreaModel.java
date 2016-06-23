package com.oxo.haiti.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaswinderwadali on 10/06/16.
 */
public class AreaModel implements Serializable {

    @SerializedName("sit")
    @Expose
    private String sit = "";

    @SerializedName("HH")
    @Expose
    private String HH = "";

    @SerializedName("block")
    @Expose
    private String block = "";

    @SerializedName("gps")
    @Expose
    private String gps = "";

    @SerializedName("_id")
    @Expose
    private String _id = "";

    @SerializedName("lat")
    @Expose
    private String lat = "";

    @SerializedName("_long")
    @Expose
    private String _long = "";

    @SerializedName("name")
    @Expose
    private String name = "";

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String get_long() {
        return _long;
    }

    public void set_long(String _long) {
        this._long = _long;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @SerializedName("desc")
    @Expose
    private String desc;


    @SerializedName("members")
    @Expose
    private List<RtfModel> memberRtfModels = new ArrayList<>();

    public void setMemberRtfModels(RtfModel memberRtfModels) {
        this.memberRtfModels.add(memberRtfModels);
    }

    public void setMemberRtfModels(List<RtfModel> memberRtfModels) {
        this.memberRtfModels = memberRtfModels;
    }

    public List<RtfModel> getMemberRtfModels() {
        return memberRtfModels;
    }

    public String getSit() {
        return sit;
    }

    public void setSit(String sit) {
        this.sit = sit;
    }


    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }


    public String getHH() {
        return HH;
    }

    public void setHH(String HH) {
        this.HH = HH;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }


}
