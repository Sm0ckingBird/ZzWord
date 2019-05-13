package com.example.zzwordapp.myAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zzwordapp.DB.DBManager;
import com.example.zzwordapp.R;
import com.example.zzwordapp.bean.GREWord.Word;

import java.util.ArrayList;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {
    private static final String TAG = "WordAdapter";
    public enum ListType {UNDO, DONE, COLLECTED};
    private ListType mListType = ListType.UNDO;
    private List<Word> mWordList;
    private Context mContext;
    private DBManager dbManager = DBManager.getInstance(mContext);
    public WordAdapter() {
        updateWordList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mContext == null) {
            mContext = viewGroup.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.word_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        final Word word = mWordList.get(i);
        final int index = i;
        holder.collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(word.getCOLLECTED()){
                    holder.collect.setImageResource(R.drawable.ic_bookmark_border_white_48dp);
                    dbManager.updateCollected(word.getSPELLING(),false);
                    word.setCOLLECTED(false);
                    notifyItemChanged(index);
                } else{
                    holder.collect.setImageResource(R.drawable.ic_bookmark);
                    dbManager.updateCollected(word.getSPELLING(),true);
                    word.setCOLLECTED(true);
                    notifyItemChanged(index);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Word word = mWordList.get(i);
        viewHolder.spelling.setText(word.getSPELLING());
        if(word.getCOLLECTED()){
            viewHolder.collect.setImageResource(R.drawable.ic_bookmark);
        } else{
            viewHolder.collect.setImageResource(R.drawable.ic_bookmark_border_white_48dp);
        }
    }

    @Override
    public int getItemCount() {
        return mWordList.size();
    }

    private void updateWordList() {
        switch (mListType){
            case UNDO:
                mWordList = dbManager.findUndoGREWords();
                this.notifyDataSetChanged();
                break;
            case DONE:
                mWordList = dbManager.findDoneGREWords();
                this.notifyDataSetChanged();
                break;
            case COLLECTED:
                mWordList = dbManager.findCollectedGREWords();
                this.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    public void changeWordList(ListType listType){
        this.mListType = listType;
        updateWordList();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView spelling;
        FloatingActionButton collect;
        private ViewHolder(View view) {
            super(view);
            spelling = view.findViewById(R.id.SPELLING);
            collect = view.findViewById(R.id.collect_button);
        }
    }
}
