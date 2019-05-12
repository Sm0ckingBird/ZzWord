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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zzwordapp.DB.AttWordDBHelper;
import com.example.zzwordapp.DB.DBManager;
import com.example.zzwordapp.bean.GREWord.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明：GRE词汇actvity
 * 作者：李耀鹏
 * 时间：2019/4/14 11:57
 */
public class GREWord extends Activity implements View.OnClickListener {
    
    private DBManager dm;                   //用于处理对GRE词汇对应的数据库grewords.db的操作
    private AttWordDBHelper attWordDBHelp;    //用于处理对生词本对应的数据库AttentionWords.db的操作
    //private ListView lv;
    private TextView WordSpelling;
    private TextView WordInfo;
    private Button button_GRE_return;       //返回主界面按钮
    private Button button_next_word;        //下一个单词按钮
    private Button button_add_word;         //加入生词本按钮

    private EditText Edittext_GRE_search;   //单词搜索栏
    private Button button_GRE_search;       //单词搜索按钮

    private List<Word> GREWordsList = new ArrayList<>();
    private  Word word;

    private String[] tablesName = {"book1","book1","book3"};    //GRE中的三本书名 book1、book2、book3
    private int [] bookWordsCount = new int [3] ;        //GRE book1、book2、book3各自的词汇数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("GRE词汇");
        this.setContentView(R.layout.activity_gre_word);
        initData();
        initWidgets();
        showOneGREWord();
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

    //初始化数据
    private void initData(){

        dm =DBManager.getInstance(this);          //单例模式
        GREWordsList = dm.findAllGREWords();
        bookWordsCount[0] = dm.getTableCount("book1");
        bookWordsCount[1] = dm.getTableCount("book2");
        bookWordsCount[2] = dm.getTableCount("book3");
        attWordDBHelp = AttWordDBHelper.getInstance(this);        //单例模式

    }

    //初始化控件
    private void initWidgets() {
        //this.lv = (ListView) findViewById(R.id.lv_GREWords);
        this.button_next_word = (Button) findViewById(R.id.button_next_GREword);
        this.button_GRE_return = findViewById(R.id.button_GRE_return);
        this.button_add_word = findViewById(R.id.button_add_GREWord);
        this.WordSpelling = findViewById(R.id.WordSpelling);
        this.WordInfo = findViewById(R.id.WordInfo);
        this.Edittext_GRE_search = findViewById(R.id.Edittext_GRE_search);
        this.button_GRE_search = findViewById(R.id.button_GRE_search);

        button_next_word.setOnClickListener(this);
        button_GRE_return.setOnClickListener(this);
        button_add_word.setOnClickListener(this);
        button_GRE_search.setOnClickListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
       // dm.closeDataBase();
       // attWordDBHelp.closeDB();
    }


    @Override
    public void onClick(View v) {

        if (v==button_GRE_search){
            String searchWord = this.Edittext_GRE_search.getText().toString();
            boolean flag = false;
            for(Word wordtemp : GREWordsList){
                if(searchWord.equals(wordtemp.getSPELLING())){
                    flag = true;
                    break;
                }
            }
            if(flag == true){
                this.Edittext_GRE_search.setText("");       //清空搜索栏
                Intent intent = new Intent();
                intent.setClass(GREWord.this, NotAttWordInfo.class);
                //从activity_GRE_word界面传递搜索词的名字至NotAttWordInfo界面
                intent.putExtra("wordName",searchWord);
                startActivity(intent);
                //finish();
            }
            else{
                Dialog dialog = new AlertDialog.Builder(this)
                        .setTitle("查询失败")
                        .setMessage("GRE词库中找不到"+searchWord)
                        .setNegativeButton("懂了哥", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                GREWord.this.Edittext_GRE_search.setText("");//清空搜索栏
                            }
                        }).create();
                dialog.show();
            }
        }

        if (v==button_next_word){
            showOneGREWord();
        }

        if(v==button_add_word){
            addAttentionWord(this.word);
        }

        if (v==button_GRE_return){
            Intent intent = new Intent();
            intent.setClass(GREWord.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    /**
     * Show one gre word.
     * 随机显示一个GRE词汇
     */
    public void showOneGREWord(){
        int i = (int)(Math.random()*3);     //随机取0/1/2
        String tableName = tablesName[i];
        int tableCount = dm.getTableCount(tableName);    //该tableName对应的表的行数
        int wordID = (int)(Math.random()* tableCount + 1);
        this.word = dm.findOneGREWordByID(wordID,tableName);      //随机从某一本书取出某一个词
        WordSpelling.setText(word.getSPELLING());
        WordInfo.setText(word.getMEANNING() + "  " + word.getPHONETIC_ALPHABET());
    }

    /**
     * Add attention word.
     *把word加入生词本数据库
     * @param word the word
     */
    public void addAttentionWord(Word word){
        List<Word> AttWordList = new ArrayList<>();
        AttWordList = attWordDBHelp.findAllAttWords();
        boolean flag = true;    //设置一个flag判断生词本中是否已经存在该词
        if(AttWordList.size()!=0){      //生词本里有单词的情况下进行判断
              for(Word wordtemp : AttWordList){
                 if(word.getMEANNING().equals(wordtemp.getMEANNING())){
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
                    .setMessage("该单词已经在单词本中")
                    .setNegativeButton("懂了哥", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    }).create();
            dialog.show();
        }
    }

/*    public void queryGREWords() {
    ArrayList<Map<String,String>> list = new ArrayList<>();
    for (Word word : GREWordsList) {
        HashMap<String, String> map = new HashMap<>();
        map.put("SPELLING", word.getSPELLING());
        map.put("INFO", word.getMEANNING() + "  " + word.getPHONETIC_ALPHABET());
        list.add(map);
    }
    SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_list_item_2,
            new String[]{"SPELLING", "INFO"}, new int[]{android.R.id.text1, android.R.id.text2});
    lv.setAdapter(adapter);
    if (list.isEmpty()) {
        Toast.makeText(this, "列表里莫子单词", Toast.LENGTH_SHORT).show();
    }
}*/
}
