package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jifen);
    }

    public void btn_1point(View v) {
        show(1);
    }

    public void btn_2point(View v) {
        show(2);
    }

    public void btn_3point(View v) {
        show(3);
    }

    public void btn_reset(View v) {
        TextView out = (TextView) findViewById(R.id.goal);
        out.setText("0");

    }

    private void show(int i) {
        TextView out = (TextView) findViewById(R.id.goal);
        String oldScore = (String) out.getText();
        String newScore = String.valueOf(Integer.parseInt(oldScore) + i);
        out.setText(newScore);
    }
}
