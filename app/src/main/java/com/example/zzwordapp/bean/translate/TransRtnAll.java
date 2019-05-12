
package com.example.zzwordapp.bean.translate;
import java.util.List;

public class TransRtnAll {

    private String from;
    private String to;
    private List<Trans_result> trans_result;        //可能返回多个翻译结果
    public void setFrom(String from) {
         this.from = from;
     }
     public String getFrom() {
         return from;
     }

    public void setTo(String to) {
         this.to = to;
     }
     public String getTo() {
         return to;
     }

    public void setTrans_result(List<Trans_result> trans_result) {
         this.trans_result = trans_result;
     }
     public List<Trans_result> getTrans_result() {
         return trans_result;
     }

}