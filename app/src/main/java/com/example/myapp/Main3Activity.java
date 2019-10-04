package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Main3Activity extends AppCompatActivity implements Runnable{

    private float dollarRate = 0.0f;
    private float euroRate = 0.0f;
    private float wonRate = 0.0f;
    private final String TAG = "Rate";
    Handler handler;
    TextView hello;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rmbtransfer);

        SharedPreferences sharedpreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        dollarRate = sharedpreferences.getFloat("key_dollar", 0.0f);
        euroRate = sharedpreferences.getFloat("key_euro", 0.0f);
        wonRate = sharedpreferences.getFloat("key_won", 0.0f);

        hello=(TextView)findViewById(R.id.hello);

        Thread t = new Thread(this);
        t.start();

        handler =new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what==5){
                    String str=(String)msg.obj;
                    Log.i(TAG,"handleMessage: getMessage msg = " + str);
                    hello.setText(str);
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
        //获取Msg对象，用于返回主线程
        Message msg=handler.obtainMessage(5);
        //msg.what=5
        msg.obj="Hello from run()";
        handler.sendMessage(msg);
        //获取网络数据
        URL url = null;
        try {
            url = new URL("http://www.usd-cny.com/icbc.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();

            String html = inputStream2String(in);
            Log.i(TAG, "run: html=" + html);
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        startActivityForResult(config,1);
    }

        return super.onOptionsItemSelected(item); }
}
