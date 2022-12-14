package com.example.food_account;

import java.util.ArrayList;
import java.util.Date;

public class PostInfo {

    private String title;//이름
    private Integer price;//가격
    private String date;//날짜
    private String id; //사용자 id
    private String keyword; // 집밥,외식
    private String documentId;
    private String monthAndYear;

    public PostInfo(String title, Integer price, String date, String id, String keyword, String monthAndYear, String documentId) {
        this.title = title;
        this.price = price;
        this.date = date;
        this.id = id;
        this.keyword = keyword;
        this.monthAndYear = monthAndYear;
        this.documentId = documentId;
    }

    public PostInfo(String title, Integer price, String date, String id, String keyword,String monthAndYear) {
        this.title = title;
        this.price = price;
        this.date = date;
        this.id = id;
        this.keyword = keyword;
        this.monthAndYear = monthAndYear;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getMonthAndYear() {
        return monthAndYear;
    }

    public void setMonthAndYear(String monthAndYear) {
        this.monthAndYear = monthAndYear;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String id) {
        this.documentId = documentId;
    }


}
