package com.example.food_account;

public class Information{
    private String nickname;
    private Integer food_expense;
    private String uid; // uid

    public Information(String nickname,Integer food_expense,String uid){
        this.nickname = nickname;
        this.food_expense = food_expense;
        this.uid = uid;
    }

    public String getNickname(){ return nickname; }
    public void setNickname(String nickname){ this.nickname=nickname; }
    public Integer getFood_expense(){ return food_expense; }
    public void setFood_expense(Integer food_expense){ this.food_expense = food_expense; }
    public String getUid(){return uid;}
    public void setUid(String uid) {this.uid = uid;}
}
