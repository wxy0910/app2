package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class configActivity extends AppCompatActivity {
    EditText input1,input2,input3;
    Float newDollar,newEuro,newWon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);
        Intent config=getIntent();
        Bundle bdl;
         input1 = findViewById(R.id.dollar_rate_key);
         input2 = findViewById(R.id.euro_rate_key);
         input3 = findViewById(R.id.won_rate_key);
         bdl=config.getExtras();
         if(bdl!=null){
             newDollar=bdl.getFloat("key_dollar");
             newEuro=bdl.getFloat("key_euro");
             newWon=bdl.getFloat("key_won");
             input1.setText(String.valueOf(newDollar));
             input2.setText(String.valueOf(newEuro));
             input3.setText(String.valueOf(newWon));
         }


    }


    public void btnsave(View v){
        String str1 = input1.getText().toString();
        String str2 = input2.getText().toString();
        String str3 = input3.getText().toString();
        newDollar = Float.parseFloat(str1);
        newEuro = Float.parseFloat(str2);
        newWon = Float.parseFloat(str3);
        Intent intent=getIntent();
        Bundle bdl=new Bundle();
        bdl.putFloat("key_dollar",newDollar);
        bdl.putFloat("key_euro",newEuro);
        bdl.putFloat("key_won",newWon);
        intent.putExtras(bdl);
        SharedPreferences sp=getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putFloat("key_dollar",newDollar);
        editor.putFloat("key_euro",newEuro);
        editor.putFloat("key_won",newWon);
        editor.apply();
        setResult(2,intent);
        finish();

    }
}
