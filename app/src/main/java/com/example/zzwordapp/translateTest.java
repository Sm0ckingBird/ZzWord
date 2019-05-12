package com.example.zzwordapp;

import com.example.zzwordapp.bean.translate.TransApi;
import com.example.zzwordapp.bean.translate.TransRtnAll;
import com.google.gson.Gson;


public  class translateTest {
    // 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
    private static final String APP_ID = "20190408000285859";
    private static final String SECURITY_KEY = "9fk_WjSu8SioIOwV9mFJ";

    public static void main(String[] args) {
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        Gson gson = new Gson();
        String query = "如果你听不见我就讲到你听见为止";
        String transResult = api.getTransResult(query, "auto", "auto");
        //生成返回的Json数据格式对应的实体类TransRtnAll
        TransRtnAll transRtnAll = gson.fromJson(transResult,TransRtnAll.class);
        //输出对样例的第一个翻译结果进行测试
        System.out.println(transRtnAll.getTrans_result().get(0).getDst());
        //输出请求所返回的Json格式数据
        System.out.println(transResult);

    }
}
