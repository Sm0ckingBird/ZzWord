package com.example.zzwordapp.myAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.zzwordapp.R;
import com.example.zzwordapp.bean.GREWord.Word;

import java.util.List;

/**
 * 作者：created by 李耀鹏 on 2019/4/14
 */
public class attWordAdapter extends BaseAdapter {

    private List<Word> attWordList;
    private  Context context;
    private CallBackInterface callBackInterface;


    /**
     * Instantiates a new Att word adapter.
     *含参数构造函数
     * @param context           the context
     * @param attWordList       the att word list
     * @param callBackInterface the call back interface
     */
    public attWordAdapter(Context context, List<Word> attWordList, CallBackInterface callBackInterface){
        this.context = context;
        this.attWordList = attWordList;
        this.callBackInterface = callBackInterface;
    }

    @Override
    public int getCount() {
        return attWordList.size();
    }

    @Override
    public Object getItem(int position) {
        return attWordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        Word word = attWordList.get(position);

        //convertView用于将加载好的布局进行缓存，先判断是否为空，可优化listView
        if (convertView == null){
            viewHolder = new ViewHolder();

            //1、引用layout布局
            convertView = LayoutInflater.from(context).inflate(R.layout.attword_item, parent, false);
            //2、填充当前项的数据
            viewHolder.textView = convertView.findViewById(R.id.text_attword_item);
            viewHolder.button = convertView.findViewById(R.id.button_del_attword_item);

            convertView.setTag(viewHolder); //3、在view上保存所需数据
        }else {
            viewHolder =  (ViewHolder) convertView.getTag();
        }

        //设置数据
        String text = word.getSPELLING() + "   " + word.getPHONETIC_ALPHABET() + "   " + word.getMEANNING();
        viewHolder.textView.setText(text);
        //word.setmCurrentPos(position);

        //listview中的button点击事件
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackInterface.callBackClick((int)getItemId(position));
            }
        });
        return convertView;
    }

    /**
     * ViewHolder通常出现在适配器里，为的是listview滚动的时候快速设置值，而不必每次都重新创建很多对象，从而提升性能
     */
    private static class ViewHolder{
        TextView textView;
        Button button;
    }
}
