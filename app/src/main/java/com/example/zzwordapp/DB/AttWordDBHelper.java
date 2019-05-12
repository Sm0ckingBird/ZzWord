package com.example.zzwordapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.zzwordapp.bean.GREWord.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：created by 李耀鹏 on 2019/4/14
 * 用于处理对GRE词汇对应的数据库AttentionWords.db的操作
 */
public class AttWordDBHelper extends SQLiteOpenHelper {

    public static AttWordDBHelper instance = null;
    public static Context context = null;

    private static final String DB_NAME = "AttentionWords.db";
    private static final int DB_VERSION = 1;

    private static String AttentionWordsTable = "AttentionWordsTale";
    private static String wordID = "wordID";
    private static String wordName = "wordName";
    private static String wordMeaning = "wordMeaning";
    private static String wordPronunciation = "wordPronunciation";

    private  SQLiteDatabase db;

    public AttWordDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.db = this.getWritableDatabase();
        this.context = context;
    }

    public static synchronized AttWordDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new AttWordDBHelper(context);
        }
        return instance;

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + AttentionWordsTable + " ("
                + wordID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + wordName + " VARCHAR,"
               // + wordType + " VARCHAR,"
                + wordPronunciation + " VARCHAR,"
                + wordMeaning + " VARCHAR)");
    }

    /**
     * 把word加入生词本数据库表AttentionWordsTable中
     * @param word the word
     */
    public void insertWord(Word word){
        ContentValues values = new ContentValues();
        values.put(this.wordName,word.getSPELLING());
        values.put(this.wordPronunciation ,word.getPHONETIC_ALPHABET());
        values.put(this.wordMeaning ,word.getMEANNING());
        db.insert(this.AttentionWordsTable, null, values);
    }

    /**
     * Find all att words list.
     *查询生词本中的所有单词
     * @return the list
     */
    public List<Word> findAllAttWords(){
        ArrayList<Word> GREWords = new ArrayList<Word>();
        String sql = "SELECT * FROM " + AttentionWordsTable;
        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()){
            Word word = new Word();
            word.setID(cursor.getInt(cursor.getColumnIndex(wordID)));
            word.setMEANNING(cursor.getString(cursor.getColumnIndex(wordMeaning)));
            word.setSPELLING(cursor.getString(cursor.getColumnIndex(wordName)));
            word.setPHONETIC_ALPHABET(cursor.getString(cursor.getColumnIndex(wordPronunciation)));
            GREWords.add(word);
        }
        cursor.close();
        return GREWords;
    }

    /**
     * Find one att word by name word.
     *根据单词名字查询生词本中该词
     * @param wordName the word name
     * @return the word
     */
    public Word findOneAttWordByName(String wordName){
        Word word = new Word();
        String sql = "SELECT * FROM " + AttentionWordsTable + " WHERE "+ this.wordName + " = " + "\'" + wordName + "\'";
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.moveToFirst()){
            word.setID(cursor.getInt(cursor.getColumnIndex(wordID)));
            word.setMEANNING(cursor.getString(cursor.getColumnIndex("wordMeaning")));
            word.setSPELLING(cursor.getString(cursor.getColumnIndex("wordName")));
            word.setPHONETIC_ALPHABET(cursor.getString(cursor.getColumnIndex("wordPronunciation")));
        }
        cursor.close();
        return word;
    }

    /**
     * Get table count int.
     *返回AttentionWordsTable表的行数
     * @return the int
     */
    public int getAttentionWordsTableCount(){
        String sql = "SELECT wordID FROM " + AttentionWordsTable;
        Cursor cursor = db.rawQuery(sql,null);
        return cursor.getCount();
    }

    /**
     * 删除AttentionWordsTable表中的所有数据
     */
    public void deleteAllWords() {
        String sql = "delete  from " +  AttentionWordsTable + " where 1 = 1";
        db.execSQL(sql);
    }

    /**
     * Delete one word.
     *根据指定的wordName从AttentionWordsTable表中删除相应的Word
     * @param wordName the word name
     */
    public void deleteOneWord(String wordName){
        String sql = "delete  from " +  AttentionWordsTable + " where "+ this.wordName + " = " + "\'" + wordName + "\'";
        db.execSQL(sql);
    }



    public void closeDB(){
        if(this.db!=null)this.db.close();
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //
    }
}
