package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rmbtransfer);
    }
    //float dollarRate=0.1445f;
    //float euroRate=0.1227f;
    //float wonRate=168.0538f;
    float dollarRate,euroRate,wonRate;
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
       // Bundle bdl=new Bundle();
        //bdl.putFloat("key_dollar",dollarRate);
        //bdl.putFloat("key_euro",euroRate);
        //bdl.putFloat("key_won",wonRate);
        //config.putExtras(bdl);
        //startActivityForResult(config,1);

        SharedPreferences sp=getSharedPreferences("myrate",Main3Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putFloat("key_dollar",dollarRate);
        editor.putFloat("key_euro",euroRate);
        editor.putFloat("key_won",wonRate);
        editor.apply();
        startActivityForResult(config,1);
    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode==1&&resultCode==2){
           // Bundle bundle=data.getExtras();
            //dollarRate=bundle.getFloat("key_dollar",0.1f);
            //euroRate=bundle.getFloat("key_euro",0.1f);
            //wonRate=bundle.getFloat("key_won",0.1f);

            SharedPreferences sharedpreferences=getSharedPreferences("myrate",configActivity.MODE_PRIVATE);
            dollarRate=sharedpreferences.getFloat("key_dollar",0.0f);
            euroRate=sharedpreferences.getFloat("key_euro",0.0f);
            wonRate=sharedpreferences.getFloat("key_won",0.0f);

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
        //Bundle bdl=new Bundle();
        //bdl.putFloat("key_dollar",dollarRate);
        //bdl.putFloat("key_euro",euroRate);
        //bdl.putFloat("key_won",wonRate);
        //config.putExtras(bdl);
        //startActivityForResult(config,1);
        SharedPreferences sp=getSharedPreferences("myrate",Main3Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putFloat("key_dollar",dollarRate);
        editor.putFloat("key_euro",euroRate);
        editor.putFloat("key_won",wonRate);
        editor.apply();
        startActivityForResult(config,1);
    }

        return super.onOptionsItemSelected(item); }
}
