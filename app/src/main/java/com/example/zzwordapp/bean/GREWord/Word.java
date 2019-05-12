package com.example.zzwordapp.bean.GREWord;

import java.io.Serializable;
import java.sql.Date;

public class Word implements Serializable {
    private int     ID;                 //单词ID
    private String  SPELLING;           //单词本身
    private String  MEANNING;           //单词释义
    private String  PHONETIC_ALPHABET;  //单词音标
    private String  LIST;               //单词表号
    private Boolean COLLECTED;          //单词是否被收藏
    private String  FAMILIARITY;        //单词熟悉程度(0:未学习 1:不熟悉 2:模糊 3:已熟悉)
    private Date    TIME;               //单词学习时间
    private String  SENTENCE;           //单词例句

    public int getID() { return ID; }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getSPELLING() {
        return SPELLING;
    }

    public void setSPELLING(String SPELLING) {
        this.SPELLING = SPELLING;
    }

    public String getMEANNING() {
        return MEANNING;
    }

    public void setMEANNING(String MEANNING) {
        this.MEANNING = MEANNING;
    }

    public String getPHONETIC_ALPHABET() {
        return PHONETIC_ALPHABET;
    }

    public void setPHONETIC_ALPHABET(String PHONETIC_ALPHABET) {
        this.PHONETIC_ALPHABET = PHONETIC_ALPHABET;
    }

    public String getLIST() {
        return LIST;
    }

    public void setLIST(String LIST) {
        this.LIST = LIST;
    }

    public Boolean getCOLLECTED() {
        return COLLECTED;
    }

    public void setCOLLECTED(Boolean COLLECTED) {
        this.COLLECTED = COLLECTED;
    }

    public String getFAMILIARITY() {
        return FAMILIARITY;
    }

    public void setFAMILIARITY(String FAMILIARITY) {
        this.FAMILIARITY = FAMILIARITY;
    }

    public Date getTIME() {
        return TIME;
    }

    public void setTIME(Date TIME) {
        this.TIME = TIME;
    }

    public String getSENTENCE() {
        return SENTENCE;
    }

    public void setSENTENCE(String SENTENCE) {
        this.SENTENCE = SENTENCE;
    }
}
