package com.example.zzwordapp.bean.Plan;

import com.example.zzwordapp.bean.GREWord.Word;

import java.util.ArrayList;
import java.util.List;

public class TodayPlan {
    private static int process = 0;    //背单词进度
    private static int reviewProcess = 0; //复习进度
    private static int reviewPartLength = 0;
    private static int index = 2;      //目前背到了第几个单词
    private static int new_number = 50; //新学单词数
    private static int old_number; //复习单词数
    private static List<Word> new_words = new ArrayList<Word>();      //新学单词表
    private static List<Word> old_words = new ArrayList<Word>();;     //复习单词表


    /*
    还需要更改
     */
    public static void init(int Index, int number,List<Word> Old_words){
        process = 0;
        reviewProcess = 0;
        reviewPartLength = 0;
        clearAll();
        index = Index;
        new_number = number;
        old_words = Old_words;
    }

    public static int getReviewPartLength() {
        return reviewPartLength;
    }

    public static void setReviewPartLength(int reviewPartLength) {
        TodayPlan.reviewPartLength = reviewPartLength;
    }

    public static int getReviewProcess() {
        return reviewProcess;
    }

    public static void setReviewProcess(int reviewProcess) {
        TodayPlan.reviewProcess = reviewProcess;
    }

    public static int getProcess() {
        return process;
    }

    public static void setProcess(int process) {
        TodayPlan.process = process;
    }

    public static int getIndex() {
        return index;
    }

    public static void setIndex(int index) {
        TodayPlan.index = index;
    }

    public static int getNew_number() {
        return new_number;
    }

    public static void setNew_number(int new_number) {
        TodayPlan.new_number = new_number;
    }

    public static int getOld_number() {
        return old_number;
    }

    public static void setOld_number(int old_number) {
        TodayPlan.old_number = old_number;
    }

    public static List<Word> getNew_words() {
        return new_words;
    }

    public static List<Word> getOld_words(){
        return old_words;
    }

    public static void addNewWord(Word word){
        new_words.add(word);
    }

    public static void addNewWords(List<Word> words){
        new_words.addAll(words);
    }

    public static void clearNewWords(){
        new_words.clear();
    }

    public static void addStrangeWord(Word word){
        old_words.add(reviewPartLength,word);
        reviewPartLength++;
        old_words.add(word);
    }

    public static void addAmbiguousWord(Word word){
        old_words.add(old_words.size()-reviewPartLength,word);
    }

    public static void clearOldWords(){
        old_words.clear();
        reviewPartLength = 0;
        reviewProcess = 0;
    }

    public static void clearAll(){
        clearNewWords();
        clearOldWords();
    }
}
