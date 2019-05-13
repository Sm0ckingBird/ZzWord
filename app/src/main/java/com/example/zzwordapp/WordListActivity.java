package com.example.zzwordapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.zzwordapp.DB.DBManager;
import com.example.zzwordapp.bean.GREWord.Word;
import com.example.zzwordapp.myAdapter.WordAdapter;

import java.util.ArrayList;
import java.util.List;

public class WordListActivity extends AppCompatActivity implements View.OnClickListener {
    Button undoButton;
    Button doneButton;
    Button collectedButton;
    private RecyclerView mRecyclerView;
    private WordAdapter mWordAdapter;
    private DBManager dbManager = DBManager.getInstance(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        mRecyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(layoutManager);
        mWordAdapter = new WordAdapter();
        mRecyclerView.setAdapter(mWordAdapter);
        undoButton = findViewById(R.id.undo_button);
        doneButton = findViewById(R.id.done_button);
        collectedButton = findViewById(R.id.collected_button);
        undoButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);
        collectedButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.undo_button:
                mWordAdapter.changeWordList(WordAdapter.ListType.UNDO);
                break;
            case R.id.done_button:
                mWordAdapter.changeWordList(WordAdapter.ListType.DONE);
                break;
            case R.id.collected_button:
                mWordAdapter.changeWordList(WordAdapter.ListType.COLLECTED);
                break;
            default:
                break;
        }
    }
}
