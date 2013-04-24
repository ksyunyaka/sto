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
    private String author;
    private Date date;
    private String shortHistory;
    private String fullHistory;
    private String xFields;
    private String title;
    private String description;
    private List<STOCategory> category;
    private float coordinateX;
    private float coordinateY;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getShortHistory() {
        return shortHistory;
    }

    public void setShortHistory(String shortHistory) {
        this.shortHistory = shortHistory;
    }

    public String getFullHistory() {
        return fullHistory;
    }

    public void setFullHistory(String fullHistory) {
        this.fullHistory = fullHistory;
    }

    public String getxFields() {
        return xFields;
    }

    public void setxFields(String xFields) {
        this.xFields = xFields;
        parseCoordinate(xFields);
    }

    private void parseCoordinate(String coordinate) {
        float[] coordinates = StoUtils.parseCoordinates(coordinate);
        coordinateX = coordinates[0];
        coordinateY = coordinates[1];
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

    public List<STOCategory> getCategory() {
        return category;
    }

    public void setCategory(List<STOCategory> category) {
        this.category = category;
    }

    public float getCoordinateX() {
        return coordinateX;
    }

    public float getCoordinateY() {
        return coordinateY;
    }
}
