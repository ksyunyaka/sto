package com.sto.entity;

import com.sto.utils.StoUtils;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: egor
 * Date: 2/27/13
 */
public class STO {

    private long id;
    private String shortHistory;
    private String telephone;
    private float longitude;
    private float latitude;
    private String time;
    private String site;
    private String washType;
    private String title;
    private String description;
    private List<Category> category;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShortHistory() {
        return shortHistory;
    }

    public void setShortHistory(String shortHistory) {
        this.shortHistory = shortHistory;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getWashType() {
        return washType;
    }

    public void setWashType(String washType) {
        this.washType = washType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }
}
