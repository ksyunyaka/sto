package com.sto.entity;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: egor
 * Date: 2/27/13
 */
public class STO {

    private long  id;
    private String author;
    private Date date;
    private String shortHistory;
    private String fullHistory;
    private String xFields;
    private String title;
    private String description;
    private String category;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
