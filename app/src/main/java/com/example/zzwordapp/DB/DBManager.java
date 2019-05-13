package com.example.zzwordapp.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.example.zzwordapp.R;
import com.example.zzwordapp.bean.GREWord.Word;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


/**
 * The type Db manager.
 * 用于处理对GRE词汇对应的数据库grewords.db的操作
 */
public class DBManager extends SQLiteOpenHelper {

    public static DBManager instance = null;
    public static Context context = null;

    private static final String DB_NAME = "grewords.db";
    private static final int DB_VERSION = 1;

    private final int BUFFER_SIZE = 999999;
    public static final String GREDB_NAME = "grewords.db"; //数据库名字
    public static final String PACKAGE_NAME = "com.example.zzwordapp";//包名
    public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() +
            "/" + PACKAGE_NAME;   //数据库的绝对路径( /data/data/com.*.*(package name))

    private SQLiteDatabase db;

    public DBManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        this.db = openGREDataBase(DB_PATH + "/" + GREDB_NAME);

    }

    public static synchronized DBManager getInstance(Context context) {
        if (instance == null) {
            instance = new DBManager(context);
        }
        return instance;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_1 = "CREATE TABLE IF NOT EXISTS book1 ( ID text not null, SPELLING text not null ," +
                " MEANNING text not null, PHONETIC_ALPHABET text, LIST text not null, COLLECTED BOOLEAN DEFAULT 0,\n" +
                "    FAMILIARITY       TEXT," +
                "    TIME              DATE," +
                "    SENTENCE          TEXT)";
        String sql_2 = "CREATE TABLE IF NOT EXISTS book2 ( ID text not null, SPELLING text not null ," +
                " MEANNING text not null, PHONETIC_ALPHABET text, LIST text not null, COLLECTED boolean DEFAULT 0,\n" +
                "    FAMILIARITY       TEXT," +
                "    TIME              DATE," +
                "    SENTENCE          TEXT)";
        String sql_3 = "CREATE TABLE IF NOT EXISTS book3 ( ID text not null, SPELLING text not null ," +
                " MEANNING text not null, PHONETIC_ALPHABET text, LIST text not null, COLLECTED BOOLEAN DEFAULT 0,\n" +
                "    FAMILIARITY       TEXT," +
                "    TIME              DATE," +
                "    SENTENCE          TEXT)";
        db.execSQL(sql_1);
        db.execSQL(sql_2);
        db.execSQL(sql_3);

    }

    // 本地打开数据方法
    private SQLiteDatabase openGREDataBase(String filePath) {       //使用R.raw.grewords资源写入数据库
        try {
            File file = new File(filePath);
            if (!file.exists()) { //判断文件是否存在
                //通过输入流和输出流，把数据库拷贝到"filePath"下
                InputStream is = context.getResources().openRawResource(R.raw.grewords);//获取输入流，使用R.raw.grewords资源
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[BUFFER_SIZE];
                int readCount;
                while((readCount = is.read(buffer))>0){
                    fos.write(buffer,0,readCount);
                }
                fos.close();
                is.close();
            }
        //打开数据库
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(filePath,null);
            System.out.println(db.getPath());
            return db;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //关闭数据库
    public  void closeDataBase()
    {
        if(this.db!=null)db.close();
    }


    /**
     * Find all words list.
     *
     * @return allGREWords
     */
    public List<Word> findAllGREWords(){
        ArrayList<Word> GREWords = new ArrayList<Word>();
        //Cursor cursor = db.query("book1", null, null, null, null, null, null);
        //按字母顺序返回GRE三本书中的所有词汇
        String sql = "SELECT * FROM (SELECT * FROM book1 UNION SELECT * FROM book2 UNION SELECT * FROM book3) " +
                " group by SPELLING";
        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            Word word = new Word();
            word.setID(cursor.getInt(cursor.getColumnIndex("ID")));
            word.setMEANNING(cursor.getString(cursor.getColumnIndex("MEANNING")));
            word.setSPELLING(cursor.getString(cursor.getColumnIndex("SPELLING")));
            word.setPHONETIC_ALPHABET(cursor.getString(cursor.getColumnIndex("PHONETIC_ALPHABET")));
            word.setLIST(cursor.getString(cursor.getColumnIndex("LIST")));
            word.setCOLLECTED(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("COLLECTED"))));
            word.setFAMILIARITY(cursor.getString(cursor.getColumnIndex("FAMILIARITY")));
            word.setTIME(Date.valueOf(cursor.getString(cursor.getColumnIndex("TIME"))));
            word.setSENTENCE(cursor.getString(cursor.getColumnIndex("SENTENCE")));
            GREWords.add(word);
        }
        cursor.close();
        return GREWords;
    }

    public List<Word> findUndoGREWords(){
        ArrayList<Word> GREWords = new ArrayList<Word>();
        //Cursor cursor = db.query("book1", null, null, null, null, null, null);
        //按字母顺序返回GRE三本书中的所有词汇
        String sql = "SELECT * FROM (SELECT * FROM book1 UNION SELECT * FROM book2 UNION SELECT * FROM book3) " +
                " where FAMILIARITY = \"0\" " + "limit 100";
        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            Word word = new Word();
            word.setID(cursor.getInt(cursor.getColumnIndex("ID")));
            word.setMEANNING(cursor.getString(cursor.getColumnIndex("MEANNING")));
            word.setSPELLING(cursor.getString(cursor.getColumnIndex("SPELLING")));
            word.setPHONETIC_ALPHABET(cursor.getString(cursor.getColumnIndex("PHONETIC_ALPHABET")));
            word.setLIST(cursor.getString(cursor.getColumnIndex("LIST")));
            word.setCOLLECTED(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("COLLECTED"))));
            word.setFAMILIARITY(cursor.getString(cursor.getColumnIndex("FAMILIARITY")));
            word.setSENTENCE(cursor.getString(cursor.getColumnIndex("SENTENCE")));
            GREWords.add(word);
        }
        cursor.close();
        return GREWords;
    }

    public List<Word> findDoneGREWords(){
        ArrayList<Word> GREWords = new ArrayList<Word>();
        //Cursor cursor = db.query("book1", null, null, null, null, null, null);
        //按字母顺序返回GRE三本书中的所有词汇
        String sql = "SELECT * FROM (SELECT * FROM book1 UNION SELECT * FROM book2 UNION SELECT * FROM book3) " +
                " where FAMILIARITY != '0' " + "limit 100";
        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            Word word = new Word();
            word.setID(cursor.getInt(cursor.getColumnIndex("ID")));
            word.setMEANNING(cursor.getString(cursor.getColumnIndex("MEANNING")));
            word.setSPELLING(cursor.getString(cursor.getColumnIndex("SPELLING")));
            word.setPHONETIC_ALPHABET(cursor.getString(cursor.getColumnIndex("PHONETIC_ALPHABET")));
            word.setLIST(cursor.getString(cursor.getColumnIndex("LIST")));
            word.setCOLLECTED(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("COLLECTED"))));
            word.setFAMILIARITY(cursor.getString(cursor.getColumnIndex("FAMILIARITY")));
            word.setSENTENCE(cursor.getString(cursor.getColumnIndex("SENTENCE")));
            GREWords.add(word);
        }
        cursor.close();
        return GREWords;
    }

    public List<Word> findCollectedGREWords(){
        ArrayList<Word> GREWords = new ArrayList<Word>();
        //Cursor cursor = db.query("book1", null, null, null, null, null, null);
        //按字母顺序返回GRE三本书中的所有词汇
        String sql = "SELECT * FROM (SELECT * FROM book1 UNION SELECT * FROM book2 UNION SELECT * FROM book3) " +
                " where COLLECTED = 'true' " + "limit 100";
        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            Word word = new Word();
            word.setID(cursor.getInt(cursor.getColumnIndex("ID")));
            word.setMEANNING(cursor.getString(cursor.getColumnIndex("MEANNING")));
            word.setSPELLING(cursor.getString(cursor.getColumnIndex("SPELLING")));
            word.setPHONETIC_ALPHABET(cursor.getString(cursor.getColumnIndex("PHONETIC_ALPHABET")));
            word.setLIST(cursor.getString(cursor.getColumnIndex("LIST")));
            word.setCOLLECTED(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("COLLECTED"))));
            word.setFAMILIARITY(cursor.getString(cursor.getColumnIndex("FAMILIARITY")));
            word.setSENTENCE(cursor.getString(cursor.getColumnIndex("SENTENCE")));
            GREWords.add(word);
        }
        cursor.close();
        return GREWords;
    }


    /**
     * @param index 计划进度
     * @param num   今日背单词数量
     * @return 今日要背的单词的list
     */
    public List<Word> findPartGREWords(int index, int num){
        ArrayList<Word> GREWords = new ArrayList<Word>();
        //Cursor cursor = db.query("book1", null, null, null, null, null, null);
        //按字母顺序返回GRE三本书中的所有词汇
        String sql = "SELECT * FROM (SELECT * FROM book1 UNION SELECT * FROM book2 UNION SELECT * FROM book3) " +
                " LIMIT "+ index+ ", " + num;
        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            Word word = new Word();
            word.setID(cursor.getInt(cursor.getColumnIndex("ID")));
            word.setMEANNING(cursor.getString(cursor.getColumnIndex("MEANNING")));
            word.setSPELLING(cursor.getString(cursor.getColumnIndex("SPELLING")));
            word.setPHONETIC_ALPHABET(cursor.getString(cursor.getColumnIndex("PHONETIC_ALPHABET")));
            word.setLIST(cursor.getString(cursor.getColumnIndex("LIST")));
            word.setFAMILIARITY(cursor.getString(cursor.getColumnIndex("FAMILIARITY")));
            word.setCOLLECTED(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("COLLECTED"))));
            java.util.Date utilDate = new java.util.Date();
            word.setTIME(new Date(utilDate.getTime()));
            word.setSENTENCE(cursor.getString(cursor.getColumnIndex("SENTENCE")));
            GREWords.add(word);
        }
        cursor.close();
        return GREWords;
    }


    /**
     * Find one word word.
     *
     * @param wordID   word在数据库中的ID
     * @param tableName 数据库中的表名
     * @return Word
     */
    public Word findOneGREWordByID(int wordID,String tableName){
        Word word = new Word();
        String sql = "SELECT * FROM " + tableName + " WHERE ID=" + String.valueOf(wordID);
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.moveToFirst()){
            word.setID(cursor.getInt(cursor.getColumnIndex("ID")));
            word.setMEANNING(cursor.getString(cursor.getColumnIndex("MEANNING")));
            word.setSPELLING(cursor.getString(cursor.getColumnIndex("SPELLING")));
            word.setPHONETIC_ALPHABET(cursor.getString(cursor.getColumnIndex("PHONETIC_ALPHABET")));
            word.setLIST(cursor.getString(cursor.getColumnIndex("LIST")));
            word.setFAMILIARITY(cursor.getString(cursor.getColumnIndex("FAMILIARITY")));
            word.setCOLLECTED(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("COLLECTED"))));
            word.setTIME(Date.valueOf(cursor.getString(cursor.getColumnIndex("TIME"))));
            word.setSENTENCE(cursor.getString(cursor.getColumnIndex("SENTENCE")));
        }
        cursor.close();
        return word;
    }


    public Word findOneGREWordByName(String wordName){
        Word word = new Word();
        String sql = "SELECT * FROM (SELECT * FROM book1 UNION SELECT * FROM book2 UNION SELECT * FROM book3)" +
                " WHERE SPELLING = " + "\'" + wordName + "\'";
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.moveToFirst()){
            word.setID(cursor.getInt(cursor.getColumnIndex("ID")));
            word.setMEANNING(cursor.getString(cursor.getColumnIndex("MEANNING")));
            word.setSPELLING(cursor.getString(cursor.getColumnIndex("SPELLING")));
            word.setPHONETIC_ALPHABET(cursor.getString(cursor.getColumnIndex("PHONETIC_ALPHABET")));
            word.setLIST(cursor.getString(cursor.getColumnIndex("LIST")));
            word.setFAMILIARITY(cursor.getString(cursor.getColumnIndex("FAMILIARITY")));
            word.setCOLLECTED(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("COLLECTED"))));
            word.setTIME(Date.valueOf(cursor.getString(cursor.getColumnIndex("TIME"))));
            word.setSENTENCE(cursor.getString(cursor.getColumnIndex("SENTENCE")));
        }
        cursor.close();
        return word;
    }

    /**
     * Get table count int.
     *
     * @param tableName 数据库中的表名
     * @return the int  该表的项数
     */
    public int getTableCount(String tableName){
        String sql = "SELECT ID FROM " + tableName;
        Cursor cursor = db.rawQuery(sql,null);
        cursor.close();
        return cursor.getCount();
    }

    /**
     * @param word          需要修改的单词的拼写
     * @param Familiarity   更新以后的熟悉程度
     */
    public void updateFamiliarity(String word, String Familiarity){
        String sql_1 = "UPDATE book1 SET FAMILIARITY = '"
                + Familiarity + "' WHERE SPELLING = '" + word + "'";
        String sql_2 = "UPDATE book2 SET FAMILIARITY = '"
                + Familiarity + "' WHERE SPELLING = '" + word + "'";
        String sql_3 = "UPDATE book3 SET FAMILIARITY = '"
                + Familiarity + "' WHERE SPELLING = '" + word + "'";
        db.execSQL(sql_1);
        db.execSQL(sql_2);
        db.execSQL(sql_3);
        Log.d("Update", "updateFamiliarity: " + word + " :" + Familiarity);
    }

    /**
     * @param word          需要修改的单词的拼写
     * @param Collected     更新以后的收藏状态
     */
    public void updateCollected(String word, boolean Collected){
        String sql_1 = "UPDATE book1 SET COLLECTED = '"
                + Boolean.toString(Collected) + "' WHERE SPELLING = '" + word + "'";
        String sql_2 = "UPDATE book2 SET COLLECTED = '"
                + Boolean.toString(Collected) + "' WHERE SPELLING = '" + word + "'";
        String sql_3 = "UPDATE book3 SET COLLECTED = '"
                + Boolean.toString(Collected) + "' WHERE SPELLING = '" + word + "'";
        db.execSQL(sql_1);
        db.execSQL(sql_2);
        db.execSQL(sql_3);
        Log.d("Update", "updateCollected: " + word + " :" + Collected);
    }

    public void getOldLists(){
        List<Word> oldLists = new ArrayList<Word>();


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
