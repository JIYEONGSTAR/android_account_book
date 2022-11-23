package com.example.food_account;

import java.util.ArrayList;
import java.util.Date;

public class PostInfo {

    private String title;//이름
    private Integer price;//가격
//    private ArrayList<String> img;
    private String date;//날짜
    private String id;
    private String keyword; // 집밥,외식

    public PostInfo(String title, Integer price, String date, String id, String keyword) {
        this.title = title;
//        this.img = img;
        this.price = price;
        this.date = date;
        this.id = id;
        this.keyword = keyword;
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


}
