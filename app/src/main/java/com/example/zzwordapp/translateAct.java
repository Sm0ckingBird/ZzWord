package com.example.zzwordapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zzwordapp.bean.httputils.HttpUtils;
import com.example.zzwordapp.bean.translate.ChatMessage;
import com.example.zzwordapp.bean.translate.ChatMessage.Type;
import com.example.zzwordapp.myAdapter.ChatMessageAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
  *说明：translate对应的activity（布局引用聊天框形式）
  *作者：李耀鹏
  *时间：2019/4/15 11:08
*/
public class translateAct extends Activity {

        private ListView mMsgs;
        private ChatMessageAdapter mAdapter;
        private List<ChatMessage> mDatas;
        private Button button_translate_rtn;

        private EditText mInputMsg;
        private Button mSendMsg;

        private Handler mHandler = new Handler()
        {
            public void handleMessage(android.os.Message msg)
            {
                // 等待接收，子线程完成数据的返回
                ChatMessage fromMessge = (ChatMessage) msg.obj;
                mDatas.add(fromMessge);
                mAdapter.notifyDataSetChanged();
                mMsgs.setSelection(mDatas.size()-1);
            };

        };

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_translate);

            initView();
            initDatas();
            // 初始化事件
            initListener();
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

    private void initDatas()
    {
        //设置正确的时区
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        mDatas = new ArrayList<ChatMessage>();
        mDatas.add(new ChatMessage("性感巴拉巴拉，在线为您翻译", Type.INCOMING, now.getTime()));
        mAdapter = new ChatMessageAdapter(this, mDatas);
        mMsgs.setAdapter(mAdapter);
    }

    private void initView()
    {
        mMsgs = findViewById(R.id.id_listview_msgs);
        mInputMsg = findViewById(R.id.id_input_msg);
        mSendMsg = findViewById(R.id.id_send_msg);
        //button_translate_rtn = findViewById(R.id.button_translate_rtn);
    }

    private void initListener() {
        mSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String toMsg = mInputMsg.getText().toString();
                if (TextUtils.isEmpty(toMsg)) {
                    Toast.makeText(translateAct.this, "发送消息不能为空！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                ChatMessage toMessage = new ChatMessage();
                toMessage.setDate(new Date());
                toMessage.setMsg(toMsg);
                toMessage.setType(Type.OUTCOMING);
                mDatas.add(toMessage);
                mAdapter.notifyDataSetChanged();
                mMsgs.setSelection(mDatas.size() - 1);

                mInputMsg.setText("");      //清空输入框

                new Thread() {
                    public void run() {
                        ChatMessage fromMessage = HttpUtils.sendMessage(toMsg);
                        Message m = Message.obtain();
                        m.obj = fromMessage;
                        mHandler.sendMessage(m);
                    }

                    ;
                }.start();
            }
        });

       /* button_translate_rtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(translateAct.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });*/

    }

}
