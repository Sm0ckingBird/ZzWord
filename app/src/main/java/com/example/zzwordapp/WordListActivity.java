package com.example.zzwordapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.zzwordapp.bean.GREWord.Word;
import com.example.zzwordapp.myAdapter.WordAdapter;

import java.util.ArrayList;
import java.util.List;

public class WordListActivity extends AppCompatActivity implements View.OnClickListener {
    private List<Word> wordList = new ArrayList<>();
    private List<Word> undoList = new ArrayList<>();
    private List<Word> doneList = new ArrayList<>();
    private List<Word> collectedList = new ArrayList<>();
    Button undoButton;
    Button doneButton;
    Button collectedButton;
    private RecyclerView mRecyclerView;
    private WordAdapter mWordAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        mRecyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(layoutManager);
        initData();
        wordList.addAll(undoList);
        mWordAdapter = new WordAdapter(wordList);
        mRecyclerView.setAdapter(mWordAdapter);
        undoButton = findViewById(R.id.undo_button);
        doneButton = findViewById(R.id.done_button);
        collectedButton = findViewById(R.id.collected_button);
        undoButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);
        collectedButton.setOnClickListener(this);
    }

    //TODO: 从数据库中初始化三个列表
    private void initData(){
        for(int i=0; i<20; ++i){
            Word word1 = new Word();
            word1.setSPELLING("undo" + i);
            Word word2 = new Word();
            word2.setSPELLING("done" + i);
            Word word3 = new Word();
            word3.setSPELLING("selected" + i);
            undoList.add(word1);
            doneList.add(word2);
            collectedList.add(word3);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.undo_button:
                wordList.clear();
                wordList.addAll(undoList);
                mWordAdapter.notifyDataSetChanged();
                break;
            case R.id.done_button:
                wordList.clear();
                wordList.addAll(doneList);
                mWordAdapter.notifyDataSetChanged();
                break;
            case R.id.collected_button:
                wordList.clear();
                wordList.addAll(collectedList);
                mWordAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }
}
