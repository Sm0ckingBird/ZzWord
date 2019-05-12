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
import android.widget.Toast;

import com.example.zzwordapp.DB.AttWordDBHelper;
import com.example.zzwordapp.DB.DBManager;
import com.example.zzwordapp.bean.GREWord.Word;

import java.util.ArrayList;
import java.util.List;

public class NotAttWordInfo extends Activity implements View.OnClickListener{

    private AttWordDBHelper attWordDBHelp;
    private DBManager dm;

    private Word word;
    private String wordName;
    private TextView WordSpelling;
    private TextView WordInfo;
    private Button button_search_return;       //返回上一界面按钮
    private Button button_add_Word_bySearch;        //加入生词本按钮
    private Button button_search_return_main;        //返回主页按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notattword_info);

        initData();
        initWidgets();
        showThisWord();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //dm.closeDataBase();
        //attWordDBHelp.closeDB();
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
        dm = DBManager.getInstance(this);
        word = dm.findOneGREWordByName(this.wordName);
    }

    private void initWidgets() {
        this.WordSpelling = findViewById(R.id.search_WordSpelling);
        this.WordInfo = findViewById(R.id.search_WordInfo);
        this.button_add_Word_bySearch = findViewById(R.id.button_add_Word_bySearch);
        this.button_search_return = findViewById(R.id.button_search_return);
        this.button_search_return_main = findViewById(R.id.button_search_return_main);

        this.button_add_Word_bySearch.setOnClickListener(this);
        this.button_search_return.setOnClickListener(this);
        this.button_search_return_main.setOnClickListener(this);
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

        if(v == button_add_Word_bySearch){
            addAttentionWord(this.wordName);
        }

        if (v==button_search_return){
            Intent intent = new Intent();
            intent.setClass(NotAttWordInfo.this, GREWord.class);
            startActivity(intent);
            finish();
        }
        if(v==this.button_search_return_main){
            Intent intent = new Intent();
            intent.setClass(NotAttWordInfo.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }


    /**
     * Add attention word.
     *把word加入生词本数据库
     * @param wordName the word name
     */
    public void addAttentionWord(String wordName){
        List<Word> AttWordList = new ArrayList<>();
        AttWordList = attWordDBHelp.findAllAttWords();
        boolean flag = true;    //设置一个flag判断生词本中是否已经存在该词
        if(AttWordList.size()!=0){      //生词本里有单词的情况下进行判断
            for(Word wordtemp : AttWordList){
                if(wordName.equals(wordtemp.getSPELLING())){
                    flag = false;
                    break;
                }
            }
        }
        if(flag == true){
            int beforeInsert = attWordDBHelp.getAttentionWordsTableCount();
            attWordDBHelp.insertWord(this.word);
            int afterInsert = attWordDBHelp.getAttentionWordsTableCount();
            if(afterInsert > beforeInsert) {        //根据表中数据条数判断是否插入成功
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Dialog dialog = new AlertDialog.Builder(this)
                    .setTitle("添加失败")
                    .setMessage(wordName + "已经在单词本中")
                    .setNegativeButton("懂了哥", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    }).create();
            dialog.show();
        }
    }
}
