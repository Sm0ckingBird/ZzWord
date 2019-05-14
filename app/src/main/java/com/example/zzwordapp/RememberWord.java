package com.example.zzwordapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zzwordapp.DB.DBManager;
import com.example.zzwordapp.DB.Plan;
import com.example.zzwordapp.bean.GREWord.Word;
import com.example.zzwordapp.bean.Plan.TodayPlan;

import java.util.ArrayList;
import java.util.List;

public class RememberWord extends AppCompatActivity {


    private CardView cardView;
    private TextView remember_word_title;
    private TextView remember_word_meaning;
    private TextView remember_word_pronunciation;
    private TextView hint;
    private FloatingActionButton fab;
    private Button remember_word_unknow;
    private Button remember_word_know;
    private Button remember_word_unsure;
    private DBManager dm;
    private Plan planDB;
    private boolean isReviewing = true;



    private Word thisWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remember_word);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        remember_word_title = (TextView)findViewById(R.id.remember_word_title);
        remember_word_pronunciation = (TextView)findViewById(R.id.remember_word_pronunciation);
        remember_word_meaning = (TextView) findViewById(R.id.remember_word_meaning);
        hint = (TextView) findViewById(R.id.hint);
        fab = (FloatingActionButton) findViewById(R.id.remember_word_fab);
        cardView = (CardView) findViewById(R.id.remember_word_card_view_1);
        remember_word_unknow = (Button) findViewById(R.id.remember_word_unknow);
        remember_word_know = (Button) findViewById(R.id.remember_word_know);
        remember_word_unsure = (Button) findViewById(R.id.remember_word_unsure);
        dm = DBManager.getInstance(this);          //单例模式
        /**
         ** 对TodayPlan进行配置,old_list配置
         **/

        List<Word> old_list = new ArrayList<Word>();

        planDB = Plan.getInstance(this);

        TodayPlan.init(planDB.getIndex("GRE"),planDB.selectPERorNUM("GRE","PERDAY"), old_list);
        TodayPlan.addNewWords(dm.findPartGREWords(TodayPlan.getIndex(),TodayPlan.getNew_number()));
        if(TodayPlan.getOld_words().isEmpty()){
            thisWord = TodayPlan.getNew_words().get(0);
            isReviewing = false;
        }
        else
            thisWord = TodayPlan.getOld_words().get(0);

//        //读取主页传来的数据
//        thisWord = (Word) getIntent().getSerializableExtra("nextWord");
//        Log.d("chuan",thisWord.getMEANNING());
        remember_word_title.setText(thisWord.getSPELLING());
        remember_word_meaning.setText(thisWord.getMEANNING());
        remember_word_pronunciation.setText(thisWord.getPHONETIC_ALPHABET());
        hint.setText("需新学: "+ (TodayPlan.getNew_number()-1) + "   需复习: "+old_list.size());
        if(!thisWord.getCOLLECTED())
            fab.setImageResource(R.drawable.ic_bookmark_border_white_48dp);
        else
            fab.setImageResource(R.drawable.ic_bookmark);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(thisWord.getCOLLECTED()){
                    fab.setImageResource(R.drawable.ic_bookmark_border_white_48dp);
                    thisWord.setCOLLECTED(false);
                    dm.updateCollected(thisWord.getSPELLING(),false);
                } else{
                    fab.setImageResource(R.drawable.ic_bookmark);
                    thisWord.setCOLLECTED(true);
                    dm.updateCollected(thisWord.getSPELLING(),true);
                }

            }
        });

        remember_word_unknow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //数据库操作
                dm.updateFamiliarity(thisWord.getSPELLING(),"1");
                //如果现在是新学阶段
                if(isReviewing == false){
                    //old_Words更新
                    TodayPlan.addStrangeWord(thisWord);
                    int process = TodayPlan.getProcess();
                    int new_number = TodayPlan.getNew_number();
                    //每10个进入一次复习阶段,且有单词复习的情况下
                    if((process % 10 == 9 || process == new_number-1) && !TodayPlan.getOld_words().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "进入复习阶段",
                                Toast.LENGTH_SHORT).show();
                        isReviewing = true;
                        thisWord = TodayPlan.getOld_words().get(0);
                    }
                    else if(process == new_number-1 && TodayPlan.getOld_words().isEmpty()){
                        Toast.makeText(getApplicationContext(), "完成今日计划！",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RememberWord.this, MainActivity.class);
                        RememberWord.this.startActivity(intent);
                        planDB.updateIndex("GRE",planDB.getIndex("GRE")+planDB.selectPERorNUM("GRE","PERDAY"));
                    }
                    //否则还是处于新学单词阶段
                    else
                        thisWord = TodayPlan.getNew_words().get(process + 1);
                    TodayPlan.setProcess(process+1);
                }
                //如果现在是复习阶段
                else{
                    int reviewProcess = TodayPlan.getReviewProcess();
                    if(TodayPlan.getReviewProcess() == TodayPlan.getOld_words().size()-1) {
                        Toast.makeText(getApplicationContext(), "进入新学阶段",
                                Toast.LENGTH_SHORT).show();
                        isReviewing = false;
                        TodayPlan.clearOldWords();
                        int process = TodayPlan.getProcess();
                        int new_number = TodayPlan.getNew_number();
                        if(process != new_number-1) {
                            Toast.makeText(getApplicationContext(), "进入新学阶段",
                                    Toast.LENGTH_SHORT).show();
                            thisWord = TodayPlan.getNew_words().get(TodayPlan.getProcess());
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "完成今日计划！",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RememberWord.this, MainActivity.class);
                            RememberWord.this.startActivity(intent);
                            planDB.updateIndex("GRE",planDB.getIndex("GRE")+planDB.selectPERorNUM("GRE","PERDAY"));
                        }
                    }
                    else {
                        thisWord = TodayPlan.getOld_words().get(reviewProcess + 1);
                        TodayPlan.setReviewProcess(reviewProcess + 1);
                    }
                }
                //更新视图
                refresh();
            }
        });
        remember_word_know.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //数据库操作
                dm.updateFamiliarity(thisWord.getSPELLING(),"3");
                //如果现在是新学阶段
                if(isReviewing == false){
                    int process = TodayPlan.getProcess();
                    int new_number = TodayPlan.getNew_number();
                    //每10个进入一次复习阶段,且有单词复习的情况下
                    if((process % 10 == 9 || process == new_number-1) && !TodayPlan.getOld_words().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "进入复习阶段",
                                Toast.LENGTH_SHORT).show();
                        isReviewing = true;
                        thisWord = TodayPlan.getOld_words().get(0);
                    }
                    else if(process == new_number-1 && TodayPlan.getOld_words().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "完成今日计划！",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RememberWord.this, MainActivity.class);
                        RememberWord.this.startActivity(intent);
                        planDB.updateIndex("GRE",planDB.getIndex("GRE")+planDB.selectPERorNUM("GRE","PERDAY"));
                    }
                    //否则还是处于新学单词阶段
                    else
                        thisWord = TodayPlan.getNew_words().get(process + 1);
                    TodayPlan.setProcess(process+1);
                }
                //如果现在是复习阶段
                else{
                    int reviewProcess = TodayPlan.getReviewProcess();
                    if(TodayPlan.getReviewProcess() == TodayPlan.getOld_words().size()-1) {
                        isReviewing = false;
                        TodayPlan.clearOldWords();
                        int process = TodayPlan.getProcess();
                        int new_number = TodayPlan.getNew_number();
                        if(process != new_number-1) {
                            Toast.makeText(getApplicationContext(), "进入新学阶段",
                                    Toast.LENGTH_SHORT).show();
                            thisWord = TodayPlan.getNew_words().get(TodayPlan.getProcess());
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "完成今日计划！",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RememberWord.this, MainActivity.class);
                            RememberWord.this.startActivity(intent);
                            planDB.updateIndex("GRE",planDB.getIndex("GRE")+planDB.selectPERorNUM("GRE","PERDAY"));
                        }
                    }
                    else {
                        thisWord = TodayPlan.getOld_words().get(reviewProcess + 1);
                        TodayPlan.setReviewProcess(reviewProcess + 1);
                    }
                }

                //更新视图
                refresh();
            }
        });
        remember_word_unsure.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //数据库操作
                dm.updateFamiliarity(thisWord.getSPELLING(),"2");
                //如果现在是新学阶段
                if(isReviewing == false){
                    //old_Words更新
                    TodayPlan.addAmbiguousWord(thisWord);
                    int process = TodayPlan.getProcess();
                    int new_number = TodayPlan.getNew_number();
                    //每10个进入一次复习阶段,且有单词复习的情况下
                    if((process % 10 == 9 || process == new_number-1) && !TodayPlan.getOld_words().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "进入复习阶段",
                                Toast.LENGTH_SHORT).show();
                        isReviewing = true;
                        thisWord = TodayPlan.getOld_words().get(0);
                    }
                    else if(process == new_number-1 && TodayPlan.getOld_words().isEmpty()){
                        Toast.makeText(getApplicationContext(), "完成今日计划！",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RememberWord.this, MainActivity.class);
                        RememberWord.this.startActivity(intent);
                        planDB.updateIndex("GRE",planDB.getIndex("GRE")+planDB.selectPERorNUM("GRE","PERDAY"));
                    }
                    //否则还是处于新学单词阶段
                    else
                        thisWord = TodayPlan.getNew_words().get(process + 1);
                    TodayPlan.setProcess(process+1);
                }
                //如果现在是复习阶段
                else{
                    int reviewProcess = TodayPlan.getReviewProcess();
                    if(TodayPlan.getReviewProcess() == TodayPlan.getOld_words().size()-1) {
                        isReviewing = false;
                        TodayPlan.clearOldWords();
                        int process = TodayPlan.getProcess();
                        int new_number = TodayPlan.getNew_number();
                        if(process != new_number-1) {
                            Toast.makeText(getApplicationContext(), "进入新学阶段",
                                    Toast.LENGTH_SHORT).show();
                            thisWord = TodayPlan.getNew_words().get(TodayPlan.getProcess());
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "完成今日计划！",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RememberWord.this, MainActivity.class);
                            RememberWord.this.startActivity(intent);
                            planDB.updateIndex("GRE",planDB.getIndex("GRE")+planDB.selectPERorNUM("GRE","PERDAY"));
                        }
                    }
                    else {
                        thisWord = TodayPlan.getOld_words().get(reviewProcess + 1);
                        TodayPlan.setReviewProcess(reviewProcess + 1);
                    }
                }
                //更新视图
                refresh();
            }
        });



        cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(remember_word_meaning.getVisibility()==View.INVISIBLE)
                    remember_word_meaning.setVisibility(View.VISIBLE);
                else
                    remember_word_meaning.setVisibility(View.INVISIBLE);

            }
        });
    }

    private void refresh(){
        int anchor = TodayPlan.getReviewPartLength();
        int review_process = TodayPlan.getReviewProcess();
        remember_word_title.setText(thisWord.getSPELLING());
        remember_word_meaning.setText(thisWord.getMEANNING());
        remember_word_pronunciation.setText(thisWord.getPHONETIC_ALPHABET());
        hint.setText("需新学: "+ (TodayPlan.getNew_number()-TodayPlan.getProcess()-1) +
                "   需复习: " +
                (review_process < anchor? TodayPlan.getOld_words().size() - anchor:TodayPlan.getOld_words().size()-review_process));
        if(!thisWord.getCOLLECTED())
            fab.setImageResource(R.drawable.ic_bookmark_border_white_48dp);
        else
            fab.setImageResource(R.drawable.ic_bookmark);
    }

}
