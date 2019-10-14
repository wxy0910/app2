package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main3Activity extends AppCompatActivity implements Runnable{

    private float dollarRate = 0.1f;
    private float euroRate = 0.2f;
    private float wonRate = 0.3f;
    private final String TAG = "Rate";
    private String updateDate="";
    Handler handler;
    TextView hello;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rmbtransfer);
        hello=(TextView)findViewById(R.id.hello);
        SharedPreferences sharedpreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        dollarRate = sharedpreferences.getFloat("key_dollar", 0.0f);
        euroRate = sharedpreferences.getFloat("key_euro", 0.0f);
        wonRate = sharedpreferences.getFloat("key_won", 0.0f);
        updateDate=sharedpreferences.getString("update_date","");
        //获取当前系统时间
        Date today=Calendar.getInstance().getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        final String todayStr=sdf.format(today);

        Log.i(TAG,"onCreate: sp dollarRate="+dollarRate);
        Log.i(TAG,"onCreate: sp euroRate="+euroRate);
        Log.i(TAG,"onCreate: sp wonRate="+wonRate);
        Log.i(TAG,"onCreate: sp updateDate="+updateDate);
        Log.i(TAG,"onCreate: sp todayStr="+todayStr);

        //判断时间
        if(!todayStr.equals(updateDate)){
            Log.i(TAG,"onCreate: 需要更新");

            //开启子线程
            Thread t = new Thread(this);
            t.start();
        }else{
            Log.i(TAG,"onCreate: 不需要更新");
        }



        handler =new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what==5){
                    Bundle bdl =(Bundle) msg.obj;
                    dollarRate=bdl.getFloat("key_dollar");
                    euroRate=bdl.getFloat("key_euro");
                    wonRate=bdl.getFloat("key_won");

                    Log.i(TAG,"handleMessage:dollar:"+dollarRate);
                    Log.i(TAG,"handleMessage:euro:"+euroRate);
                    Log.i(TAG,"handleMessage:won:"+wonRate);

                    //保存更新的日期
                    SharedPreferences sharedpreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedpreferences.edit();
                    editor.putFloat("key_dollar",dollarRate);
                    editor.putFloat("key_euro",euroRate);
                    editor.putFloat("key_won",wonRate);
                    editor.putString("update_date",todayStr);
                    editor.apply();

                    Toast.makeText(Main3Activity.this,"汇率已更新",Toast.LENGTH_SHORT).show();
                }

                super.handleMessage(msg);
            }
        };

    }
    //float dollarRate=0.1445f;
    //float euroRate=0.1227f;
    //float wonRate=168.0538f;
    //float dollarRate,euroRate,wonRate;


    public void run() { //在子线程里返回消息
        Log.i(TAG, "run: run()......");
        for (int i=1;i<3;i++){
            Log.i(TAG, "run: i=" + i);
            try {
                Thread.sleep(2000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        //用于保存获取的汇率
        Bundle bundle;


        //获取网络数据
        /*URL url = null;
        try {
            url = new URL("http://www.usd-cny.com/bankofchina.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();

            String html = inputStream2String(in);
            Log.i(TAG, "run: html=" + html);
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } */
       bundle= getFromBOC();
        //获取Msg对象，用于返回主线程
        Message msg=handler.obtainMessage(5);
        //msg.what=5
        //msg.obj="Hello from run()";
        msg.obj=bundle;
        handler.sendMessage(msg);

    }  //bundle中保存所获取的汇率
/*
     从bankofchina获取数据

 */
    private Bundle getFromBOC() {
        Bundle bundle=new Bundle();
        Document doc = null;
        try {
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
           for(int i=0;i<tds.size();i+=6){
               Element td1=tds.get(i);
               Element td2=tds.get(i+5);
              // Log.i(TAG,"run: text="+td1.text());
              // Log.i(TAG,"run: value="+td2.text());
               //Log.i(TAG,"run: "+td1.text()+"==>"+td2.text());
                String str1=td1.text();
                String val=td2.text();
               if("美元".equals(str1)){
                   bundle.putFloat("key_dollar",100f/Float.parseFloat(val));
               }else if("欧元".equals(str1)){
                   bundle.putFloat("key_euro",100f/Float.parseFloat(val));
               }else if("韩元".equals(str1)){
                   bundle.putFloat("key_won",100f/Float.parseFloat(val));
               }

           }
           /* for(Element td : tds){
                Log.i(TAG,"run: td="+td);
                Log.i(TAG,"run: text="+td.text());
                Log.i(TAG,"run: html"+td.html());
            } */
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bundle;
    }

    //将输入流InputStream转换为String
    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        while (true) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

    public void btndollar(View v) {
        transfer(dollarRate);
    }

    public void btneuro(View v){
        transfer(euroRate);

    }

    public void btnwon(View v){
        transfer(wonRate);


    }

    public void transfer(float i) {
        EditText input = findViewById(R.id.inputRMB);
        String str = input.getText().toString();
        TextView show = findViewById(R.id.inputRMB);
        if (str==null||str.length()==0){
            show.setText("input RMB!!!");
        }
        else {
            float s = Float.parseFloat(str);
            float r = s * i;
            show.setText(String.format("%.2f", r));
        }
    }
    public void btnconfig(View v){
        Intent config = new Intent(this,configActivity.class);
        Bundle bdl=new Bundle();
        bdl.putFloat("key_dollar",dollarRate);
        bdl.putFloat("key_euro",euroRate);
        bdl.putFloat("key_won",wonRate);
        config.putExtras(bdl);
        startActivityForResult(config,1);

    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode==1&&resultCode==2){
            Bundle bundle=data.getExtras();
            dollarRate=bundle.getFloat("key_dollar",0.1f);
            euroRate=bundle.getFloat("key_euro",0.1f);
            wonRate=bundle.getFloat("key_won",0.1f);


        }
        super.onActivityResult(requestCode,resultCode,data);
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {     getMenuInflater().inflate(R.menu.menu1,menu);
          return true;
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {     if(item.getItemId()==R.id.menu_set){

        // 事件处理代码
        Intent config = new Intent(this,configActivity.class);
        Bundle bdl=new Bundle();
        bdl.putFloat("key_dollar",dollarRate);
        bdl.putFloat("key_euro",euroRate);
        bdl.putFloat("key_won",wonRate);
        config.putExtras(bdl);
        startActivityForResult(config,1);

    }else if(item.getItemId()==R.id.open_list){
        //打开列表窗口
        Intent list=new Intent(this,MyList2Activity.class);
        startActivity(list);
    }

        return super.onOptionsItemSelected(item); }
}
