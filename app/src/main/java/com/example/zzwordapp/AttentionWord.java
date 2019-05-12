package com.example.zzwordapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zzwordapp.DB.AttWordDBHelper;
import com.example.zzwordapp.bean.GREWord.Word;
import com.example.zzwordapp.myAdapter.CallBackInterface;
import com.example.zzwordapp.myAdapter.attWordAdapter;

import java.util.ArrayList;
import java.util.List;

/**
  *说明：生词本attentionWord对应的activity
  *作者：李耀鹏
  *时间：2019/4/14 15:39
*/
public class AttentionWord extends Activity implements View.OnClickListener, CallBackInterface{

    private AttWordDBHelper attWordDBHelp;    //用于处理对生词本对应的数据库AttentionWords.db的操作
    private attWordAdapter myAdapter;       //listview绑定的适配器

    private Button buttonDelWords;
    private Button button_attention_rtn;
    private ListView lv_attention_words;

    private EditText Edittext_attword_search;   //单词搜索栏
    private Button button_attword_search;       //单词搜索按钮

    private  List<Word> AttWordList = new ArrayList<>();

   // ArrayList<Map<String,String>> attWordMapList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("生词本");
        setContentView(R.layout.activity_attention_word);

        attWordDBHelp = AttWordDBHelper.getInstance(this);        //单例模式
        AttWordList = attWordDBHelp.findAllAttWords();
       // attWordDBHelp = new AttWordDBHelper(this);
        initWidgets();
        initData();
        //queryAttWordList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    private void initData(){

        myAdapter = new attWordAdapter(this,AttWordList,this);
        lv_attention_words.setAdapter(myAdapter); //listView关联适配器
    }

    private void initWidgets() {
        this.button_attention_rtn = findViewById(R.id.button_attention_return);
        this.buttonDelWords = findViewById(R.id.button_delete_attention_words);
        this.lv_attention_words = findViewById(R.id.lv_attention_words);
        this.Edittext_attword_search = findViewById(R.id.Edittext_attword_search);
        this.button_attword_search = findViewById(R.id.button_attword_search);

        button_attention_rtn.setOnClickListener(this);
        buttonDelWords.setOnClickListener(this);
        button_attword_search.setOnClickListener(this);

        //listview中每一个item的点击事件（调到该词的详情面）
        lv_attention_words.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = AttWordList.get(position);
                Intent intent = new Intent();
                intent.setClass(AttentionWord.this, AttWordInfo.class);
                //从AttentionWord界面传递搜索词的名字至AttWordInfo界面
                intent.putExtra("wordName",word.getSPELLING());
                startActivity(intent);
            }
        });

    }


    private  void updateAdapter(){
        AttWordList = attWordDBHelp.findAllAttWords();
        myAdapter = new attWordAdapter(this,AttWordList,this);
    }

   /* private void autoFlash(){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AttWordList = attWordDBHelp.findAllAttWords();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myAdapter.notifyDataSetChanged();
                        }
                    });

                }
            }
        });
        thread.start();
    }*/


    @Override
    public void onClick(View v) {

        if (v==button_attword_search){
            String searchWord = this.Edittext_attword_search.getText().toString();
            boolean flag = false;
            for(Word wordtemp : AttWordList){
                if(searchWord.equals(wordtemp.getSPELLING())){
                    flag = true;
                    break;
                }
            }
            if(flag == true){
                this.Edittext_attword_search.setText("");       //清空搜索栏
                Intent intent = new Intent();
                intent.setClass(AttentionWord.this, AttWordInfo.class);
                //从AttentionWord界面传递搜索词的名字至AttWordInfo界面
                intent.putExtra("wordName",searchWord);
                startActivity(intent);
                //finish();
            }
            else{
                Dialog dialog = new AlertDialog.Builder(this)
                        .setTitle("查询失败")
                        .setMessage("生词本中找不到"+searchWord)
                        .setNegativeButton("懂了哥", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                AttentionWord.this.Edittext_attword_search.setText("");//清空搜索栏
                            }
                        }).create();
                dialog.show();
            }
        }

        if(v==buttonDelWords){
            if(!AttWordList.isEmpty())
                deleteAllAttWords();
            else{
                Toast.makeText(this, "生词本比脸还干净来着", Toast.LENGTH_SHORT).show();
            }
        }

        if (v==button_attention_rtn){
            Intent intent = new Intent();
            intent.setClass(AttentionWord.this, MainActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 清空单词本
     */
    public void deleteAllAttWords(){

        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("清空单词本")
                .setMessage("确定清空生词本吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        attWordDBHelp.deleteAllWords();
                        lv_attention_words.setAdapter(null);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).create();
        dialog.show();
    }

    /**
     * Query att word list.
     * 把生词本中的所有单词显示在一个Listview中
     */
/*
    public void queryAttWordList() {
        ArrayList<Map<String,String>> attWordMapList = new ArrayList<>();
        for (Word word : AttWordList) {
            HashMap<String, String> map = new HashMap<>();
            map.put("SPELLING", word.getSPELLING());
            map.put("INFO", word.getMEANNING() + "  " + word.getPHONETIC_ALPHABET());
            attWordMapList.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, attWordMapList, android.R.layout.simple_list_item_2,
                new String[]{"SPELLING", "INFO"}, new int[]{android.R.id.text1, android.R.id.text2});
        lv_attention_words.setAdapter(adapter);
        if (attWordMapList.isEmpty()) {
            Toast.makeText(this, "列表里莫子单词", Toast.LENGTH_SHORT).show();
        }
    }
*/

    protected void onResume() {
        // TODO Auto-generated method stub
        //this.queryAttWordList();
        super.onResume();
    }



    @Override
    public void callBackClick(int position) {
        Word word = AttWordList.get(position);
        final String wordName = word.getSPELLING();
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("移除该词")
                .setMessage("确定从生词本中移除该词吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        attWordDBHelp.deleteOneWord(wordName);
                        updateAdapter();
                        lv_attention_words.setAdapter(myAdapter);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).create();
        dialog.show();
    }
}
