package com.example.zzwordapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zzwordapp.DB.DBManager;
import com.example.zzwordapp.DB.Plan;
public class MainActivity extends Activity implements View.OnClickListener {
    private Plan PlanDB;
    private DBManager dbManager;
    private Button CETWordsButton;		//CET词汇按钮
    private Button GREWordsButton;		//GRE词汇按钮
    private Button TranslateButton;		//翻译
    private Button AttentionButton;		//生词本

    private Button StratStudy;
    private Button ChangePlan;

    private Button CollectionButton;
    private TextView PerdayWord;
    private TextView TotalWord;
    private  long exitTime = 0;
    View myView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.setTitle("zZWordApp");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//使得程序运行时是loading画面，数据准备完毕之后在跳转至主界面
        PlanDB = Plan.getInstance(this);
        LayoutInflater mInflater = LayoutInflater.from(this);
        myView = mInflater.inflate(R.layout.main, null);
        if(PlanDB.selectInsertOrNot("GRE")==0){
            PlanDB.insertWord("GRE",50,PlanDB.getTableCount("book1")+PlanDB.getTableCount("book2")
            +PlanDB.getTableCount("book3"));
        }



        initWidgets();


        setContentView(myView);

    }

    /**
     * 监听物理键盘返回键事件
     * 重写onKeyDown方法
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                exitApp();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     *
     * 退出程序
     */
    private void exitApp() {
        // 判断2次点击事件时间
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            // 退出程序关键代码
            System.exit(0);
        }
    }

    private void initWidgets() {
        // TODO Auto-generated method stub
        this.CETWordsButton=(Button) myView.findViewById(R.id.CETWords);
        CETWordsButton.setOnClickListener(this);
        this.GREWordsButton=(Button) myView.findViewById(R.id.GREWords);
        GREWordsButton.setOnClickListener(this);
        this.TranslateButton=(Button) myView.findViewById(R.id.translate);
        TranslateButton.setOnClickListener(this);
        this.AttentionButton=(Button) myView.findViewById(R.id.attention);
        AttentionButton.setOnClickListener(this);
        this.ChangePlan=(Button) myView.findViewById(R.id.renewplan);
        ChangePlan.setOnClickListener(this);
        this.CollectionButton=(Button) myView.findViewById(R.id.collection);
        CollectionButton.setOnClickListener(this);
        this.PerdayWord=(TextView) myView.findViewById(R.id.todaywordtext);
        String type="PERDAY";
        int perday=PlanDB.selectPERorNUM("GRE",type);
        PerdayWord.setText(String.format("%1$d",perday));
        String type1="PERDAY1";
        int num=PlanDB.selectPERorNUM("GRE",type1);

        int day=num/perday;
        String output="学完共需%1$d天";
        ChangePlan.setText(String.format(output,day));
        //initSpinner();
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v==CETWordsButton){
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, RememberWord.class);
            MainActivity.this.startActivity(intent);
        }

        if (v==GREWordsButton){
            Intent intent = new Intent(MainActivity.this, GREWord.class);
            startActivity(intent);
        }

        if (v==TranslateButton){
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, translateAct.class);
            MainActivity.this.startActivity(intent);
        }

        if (v==this.AttentionButton||v==this.CollectionButton){
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, WordListActivity.class);
            MainActivity.this.startActivity(intent);
        }
        if (v==this.ChangePlan){
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, ChangePlan.class);
            MainActivity.this.startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
