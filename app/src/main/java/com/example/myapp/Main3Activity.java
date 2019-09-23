package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rmbtransfer);
    }

    public void btndollar(View v) {
        transfer(0.1445f);
    }

    public void btneuro(View v){
        transfer(0.1227f);

    }

    public void btnwon(View v){
        transfer(168.0538f);


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
            show.setText(String.format("%2f", r));
        }
    }
}
