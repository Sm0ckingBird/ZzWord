package com.example.zzwordapp.bean.httputils;


import com.example.zzwordapp.bean.translate.ChatMessage;
import com.example.zzwordapp.bean.translate.TransApi;
import com.example.zzwordapp.bean.translate.TransRtnAll;
import com.example.zzwordapp.bean.translate.ChatMessage.Type;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.TimeZone;


/**
  *说明：请求百度翻译API，返回相应的翻译内容
  *作者：李耀鹏
  *时间：2019/4/15 10:44
*/
public class HttpUtils {
    private static final String APP_ID = "20190408000285859";
    private static final String SECURITY_KEY = "9fk_WjSu8SioIOwV9mFJ";

    public static ChatMessage sendMessage(String queryMsg){
        ChatMessage chatMessage = new ChatMessage();
        Gson gson = new Gson();
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        String transResult = api.getTransResult(queryMsg, "auto", "auto");
        //生成返回的Json数据格式对应的实体类TransRtnAll
        TransRtnAll transRtnAll = gson.fromJson(transResult,TransRtnAll.class);

        //设置正确的时区
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        chatMessage.setDate(now.getTime());
        //参数为调用API返回的第一个翻译的内容
        chatMessage.setMsg(transRtnAll.getTrans_result().get(0).getDst());
        chatMessage.setType(Type.INCOMING);
        return chatMessage;
    }



}
