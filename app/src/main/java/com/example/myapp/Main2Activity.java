package com.example.myapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jifen);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String scorea=((TextView) findViewById(R.id.goal)).getText().toString();
        String scoreb=((TextView) findViewById(R.id.goal2)).getText().toString();
        outState.putString("teama_score",scorea);
        outState.putString("teamb_score",scoreb);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String scorea = savedInstanceState.getString("teama_score");
        String scoreb = savedInstanceState.getString("teamb_score");
        ((TextView) findViewById(R.id.goal)).setText(scorea);
        ((TextView) findViewById(R.id.goal2)).setText(scoreb);
    }

    public void btn_1point(View v) {
        show1(1);
    }

    public void btn_2point(View v) {
        show1(2);
    }

    public void btn_3point(View v) {
        show1(3);
    }
    public void btn_1point2(View v) {
        show2(1);
    }

    public void btn_2point2(View v) {
        show2(2);
    }

    public void btn_3point2(View v) {
        show2(3);
    }
    public void btn_reset(View v) {
        TextView out = (TextView) findViewById(R.id.goal);
        TextView out2 = (TextView) findViewById(R.id.goal2);
        out.setText("0");
        out2.setText("0");

    }

    private void show1(int i) {
        TextView out = (TextView) findViewById(R.id.goal);
        String oldScore = (String) out.getText();
        String newScore = String.valueOf(Integer.parseInt(oldScore) + i);
        out.setText(newScore);
    }
    private void show2(int i) {
        TextView out = (TextView) findViewById(R.id.goal2);
        String oldScore = (String) out.getText();
        String newScore = String.valueOf(Integer.parseInt(oldScore) + i);
        out.setText(newScore);
    }
}
