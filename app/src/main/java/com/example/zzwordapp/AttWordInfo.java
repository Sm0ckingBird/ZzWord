package com.example.zzwordapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zzwordapp.DB.AttWordDBHelper;
import com.example.zzwordapp.bean.GREWord.Word;


public class AttWordInfo extends Activity implements View.OnClickListener {

    private AttWordDBHelper attWordDBHelp;

    private Word word;
    private String wordName;
    private TextView WordSpelling;
    private TextView WordInfo;
    private Button button_att_search_return;       //返回上一界面按钮
    private Button button_del_attWord_bySearch;        //移出生词本按钮
    private Button button_att_search_return_main;        //返回主页按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_att_word_info);

        initData();
        initWidgets();
        showThisWord();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
       // attWordDBHelp.closeDB();
    }

    //监听物理键盘的返回按钮
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


    private void initData() {
        Intent intent = getIntent();
        this.wordName = intent.getStringExtra("wordName");
        attWordDBHelp = AttWordDBHelper.getInstance(this);        //单例模式
        word = attWordDBHelp.findOneAttWordByName(this.wordName);
    }

    private void initWidgets() {
        this.WordSpelling = findViewById(R.id.search_AttWordSpelling);
        this.WordInfo = findViewById(R.id.search_AttWordInfo);
        this.button_att_search_return = findViewById(R.id.button_att_search_return);
        this.button_del_attWord_bySearch = findViewById(R.id.button_del_attWord_bySearch);
        this.button_att_search_return_main = findViewById(R.id.button_att_search_return_main);

        this.button_att_search_return.setOnClickListener(this);
        this.button_del_attWord_bySearch.setOnClickListener(this);
        this.button_att_search_return_main.setOnClickListener(this);
    }

    /**
     * Show this word.
     * 显示搜索的词
     */
    public void showThisWord(){
        WordSpelling.setText(word.getSPELLING());
        WordInfo.setText(word.getMEANNING() + "  " + word.getPHONETIC_ALPHABET());
    }

    @Override
    public void onClick(View v) {
        if(v == button_del_attWord_bySearch){
            removeWordFromAtt(this.wordName);
        }

        if (v==button_att_search_return){
            Intent intent = new Intent();
            intent.setClass(AttWordInfo.this, AttentionWord.class);
            startActivity(intent);
            finish();
        }
        if(v==this.button_att_search_return_main){
            Intent intent = new Intent();
            intent.setClass(AttWordInfo.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void removeWordFromAtt(String wordName){
        final Intent intent = new Intent();
        intent.setClass(AttWordInfo.this, AttentionWord.class);
        final String tempName = wordName;
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("移除该词")
                .setMessage("确定从生词本中移除该词吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        attWordDBHelp.deleteOneWord(tempName);
                        startActivity(intent);      //从生词本移出该词后返回生词本页面
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).create();
        dialog.show();
    }
}
