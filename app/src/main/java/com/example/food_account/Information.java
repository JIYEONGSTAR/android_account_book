package com.example.food_account;

public class Information{
    private String nickname;
    private Integer food_expense;

    public Information(String nickname,Integer food_expense){
        this.nickname = nickname;
        this.food_expense = food_expense;
    }

    public String getNickname(){ return nickname; }
    public void setNickname(String nickname){ this.nickname=nickname; }
    public Integer getFood_expense(){ return food_expense; }
    public void setFood_expense(Integer food_expense){ this.food_expense = food_expense; }

}
