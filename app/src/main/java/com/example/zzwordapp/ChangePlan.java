package com.example.zzwordapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zzwordapp.DB.Plan;
import com.example.zzwordapp.myAdapter.CallBackInterface;

public class ChangePlan extends AppCompatActivity {
    private Plan PlanDB;
    private Button SaveButton;
    private Button ExitButton;
    private Button SelectButton;
    private Button CETButton;
    private Button GREButton;
    private Button ShowButton;
    private EditText PerNum;
    private TextView DayNum;
    private String ID;
    View myView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeplan);//使得程序运行时是loading画面，数据准备完毕之后在跳转至主界面
        PlanDB = Plan.getInstance(this);
        initWidgets();
        //queryAttWordList();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //attWordDBHelp.closeDB();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        //按下的如果是BACK，同时没有重复
        {
            this.finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void initWidgets() {
        this.SaveButton = (Button)findViewById(R.id.save);
        this.ExitButton = findViewById(R.id.exit);
        this.SelectButton = findViewById(R.id.select);
        this.CETButton = (Button)findViewById(R.id.CET);
        this.GREButton = findViewById(R.id.GRE);
        this.ShowButton=findViewById(R.id.show);
        this.PerNum=findViewById(R.id.pernum);
        this.DayNum=findViewById(R.id.daynum);
        SaveButton.setOnClickListener(SaveClickListener);
        ExitButton.setOnClickListener(SaveClickListener);
        SelectButton.setOnClickListener(SaveClickListener);
        CETButton.setOnClickListener(SaveClickListener);
        GREButton.setOnClickListener(SaveClickListener);
    }

    private final View.OnClickListener SaveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == SaveButton) {
                int pernum=Integer.parseInt(PerNum.getText().toString());
                int daynum=Integer.parseInt(DayNum.getText().toString());
                if(PlanDB.selectInsertOrNot(ID)==0){
                    PlanDB.insertWord(ID,pernum,6000);
                }else{
                    PlanDB.updatePlan(ID,pernum);
                }
                    Intent intent = new Intent();
                    intent.setClass(ChangePlan.this, MainActivity.class);
                    startActivity(intent);
                    finish();
            }
            if(view==ExitButton){
                Intent intent = new Intent();
                intent.setClass(ChangePlan.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            if (view == SelectButton) {
                int pernum=Integer.parseInt(PerNum.getText().toString());
                int daynum=(int)PlanDB.selectPERorNUM(ID,"ggg")/pernum;
                DayNum.setText(String.format("%1$d",daynum));
            }
            if(view == CETButton){
                CETButton.setBackgroundResource(R.drawable.refesh);
                GREButton.setBackgroundResource(R.drawable.queding);
                ID="CET";
                ShowButton.setText("CET");
            }
            if(view == GREButton){
                GREButton.setBackgroundResource(R.drawable.refesh);
                CETButton.setBackgroundResource(R.drawable.queding);
                ID="GRE";
                ShowButton.setText("GRE");
            }
        }
    };
}
