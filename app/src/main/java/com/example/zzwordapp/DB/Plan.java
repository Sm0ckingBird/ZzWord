package com.example.zzwordapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Plan extends SQLiteOpenHelper {
    public static Plan instance = null;
    public static Context context = null;

    private static final String DB_NAME = "Plan.db";
    private static final int DB_VERSION = 1;

    private static String PlanTable = "PlanTable";
    private static String ID = "ID";
    private static String INDEXPlan = "INDEXPlan";
    private static String PERDAY = "PERDAY";
    private static String NUMBER = "NUMBER";

    private SQLiteDatabase db;



    public Plan(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.db = this.getWritableDatabase();
        this.context = context;
    }
    public static synchronized Plan getInstance(Context context) {
        if (instance == null) {
            instance = new Plan(context);
        }
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + PlanTable + " ("
                + ID + " VARCHAR PRIMARY KEY "+","
                + INDEXPlan + " INT,"
                // + wordType + " VARCHAR,"
                + PERDAY + " INT "+","
                + NUMBER + " INT "+")");
    }

    public void insertWord(String id,int perday,int number){
        ContentValues values = new ContentValues();
        values.put(this.ID,id);
        values.put(this.PERDAY ,perday);
       /* values.put(this.INDEXPlan ,0);*/
        values.put(this.NUMBER ,number);
        try{
            db.insert(this.PlanTable, null, values);
        }catch(Exception ex){
            System.out.println("gangnangan");
            ex.printStackTrace();
        }
    }
    public void updatePlan(String ID,int perday){
        ContentValues values = new ContentValues();
        values.put(this.PERDAY ,perday);
        String[] args={String.valueOf(ID)};
        db.update(PlanTable,values,"ID=?",args);
    }
    public int selectInsertOrNot(String ID){
        String sql = "SELECT * FROM " + PlanTable + " WHERE "+this.ID + " = " + "\'" + ID + "\'";
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.getCount()>0){
            return 1;
        }else{
            return 0;
        }
    }
    public int selectPERorNUM(String ID,String type){
        String sql = "SELECT * FROM " + this.PlanTable + " WHERE "+this.ID + " = " + "\'" + ID + "\'";
        Cursor cursor = db.rawQuery(sql,null);
        System.out.println(cursor.getCount());
        int result=0;
        if(type.equals("PERDAY")){
            if (cursor.moveToFirst()){
                result=cursor.getInt(cursor.getColumnIndex(PERDAY));
            }
            cursor.close();
        }else{
            if (cursor.moveToFirst()){
                result=cursor.getInt(cursor.getColumnIndex(NUMBER));
            }
            cursor.close();
        }
        return result;
    }
    public void closeDB(){
        if(this.db!=null)this.db.close();
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //
    }

}
