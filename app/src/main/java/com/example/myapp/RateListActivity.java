package com.example.myapp;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RateListActivity extends ListActivity implements Runnable{
    String data[]={"wait..."};
    Handler handler;
    private final String TAG = "RateList";
    private String logDate = "";
    private final String DATE_SP_KEY = "lastRateDateStr";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rate_list);

        super.onCreate(savedInstanceState);
// setContentView(R.layout.activity_rate_list);

        SharedPreferences sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
        logDate = sp.getString(DATE_SP_KEY, "");
        Log.i("List","lastRateDateStr=" + logDate);

        List<String> list1=new ArrayList<String>();
        for(int i=1;i<100;i++){
            list1.add("item"+i);
        }
        ListAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        setListAdapter(adapter);
        Thread t =new Thread(this);
        t.start();

        handler =new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what==7){
                    List<String> list2=(List<String>)msg.obj;
                    ListAdapter adapter=new ArrayAdapter<String>(RateListActivity.this,android.R.layout.simple_list_item_1,list2);
                    setListAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };


    }

    @Override
    public void run() {
        //获取网络数据，放入List带回到主线程中
        List<String> rateList = new ArrayList<String>();
        String curDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        Log.i("run","curDateStr:" + curDateStr + " logDate:" + logDate);

        if(curDateStr.equals(logDate)){
            //如果相等，则不从网络中获取数据
            Log.i("run","日期相等，从数据库中获取数据");
            RateManager manager=new RateManager(this);
            for(RateItem item:manager.listAll()){
                rateList.add(item.getCurName()+"-->"+item.getCurRate());
            }
        }else{
            //从网络中获取数据
            Log.i("run","日期不相等，从网络中获取在线数据");
            Document doc = null;
            try {
                Thread.sleep(2000);
                doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
                Log.i(TAG,"run"+doc.title());
                Elements tables=doc.getElementsByTag("table");
                //int i=1;
           /* for(Element table : tables){
                Log.i(TAG,"run: tr["+i+"]="+table);
                i++;
            }
*/
                Element table1=tables.get(0);
                //Log.i(TAG,"run: tr27="+tr27);//显示tr27的内容
                //获取td中的数据，即汇买价、钞买价、汇卖价、钞卖价、折算价
                Elements tds=table1.getElementsByTag("td");

                List<RateItem> retList = new ArrayList<RateItem>();
                for(int i=0;i<tds.size();i+=6){
                    Element td1=tds.get(i);
                    Element td2=tds.get(i+5);
                    // Log.i(TAG,"run: text="+td1.text());
                    // Log.i(TAG,"run: value="+td2.text());
                    //Log.i(TAG,"run: "+td1.text()+"==>"+td2.text());
                    String str1=td1.text();
                    String val=td2.text();

                    Log.i(TAG,"run: "+str1+"==>"+val);
                    rateList.add(str1+"==>"+val);
                    retList.add(new RateItem(str1,val));

                }
           /* for(Element td : tds){
                Log.i(TAG,"run: td="+td);
                Log.i(TAG,"run: text="+td.text());
                Log.i(TAG,"run: html"+td.html());
            } */

           //把数据写入到数据库中
                RateManager manager=new RateManager(this);
                manager.deleteAll();
                manager.addAll(retList);

                //记录更新日期
                //更新记录日期
                SharedPreferences sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString(DATE_SP_KEY, curDateStr);
                edit.commit();
                Log.i("run","更新日期结束：" + curDateStr);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        Message msg=handler.obtainMessage(7);
        msg.obj=rateList;
        handler.sendMessage(msg);
    }
}
