package com.example.zzwordapp.bean.Plan;

public class Plan {
    private String ID;                           //计划名称
    private int index;                           //计划完成度
    private int perDay;                          //每天计划单词数
    private int number;                          //单词库总量


    public void setID(String ID){
        this.ID = ID;
    }

    public String getID(){
        return ID;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public int getIndex(){
        return index;
    }

    public void setPerDay(int perDay){
        this.perDay = perDay;
    }

    public int getPerDay(){
        return perDay;
    }

    public void setNumber(int number){
        this.number = number;
    }

    public int getNumber(){
        return number;
    }
}
