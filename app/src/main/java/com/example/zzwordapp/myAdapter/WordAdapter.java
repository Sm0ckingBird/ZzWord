package com.example.zzwordapp.myAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zzwordapp.R;
import com.example.zzwordapp.bean.GREWord.Word;

import java.util.ArrayList;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {
    private static final String TAG = "WordAdapter";
    private List<Word> mWordList = new ArrayList<>();
    private Context mContext;

    public WordAdapter(List<Word> wordList) {
        mWordList = wordList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mContext == null) {
            mContext = viewGroup.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.word_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        // TODO: 添加按钮的响应时间，like holder.spelling.setOnClickListener();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Word word = mWordList.get(i);
        viewHolder.spelling.setText(word.getSPELLING());
    }

    @Override
    public int getItemCount() {
        return mWordList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView spelling;
        private ViewHolder(View view) {
            super(view);
            spelling = view.findViewById(R.id.SPELLING);
        }
    }
}
