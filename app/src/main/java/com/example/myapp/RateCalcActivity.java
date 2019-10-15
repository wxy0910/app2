package com.example.myapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RateCalcActivity extends AppCompatActivity {

    String TAG="rateCalc";
    float rate=0f;
    EditText inp2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_calc);
        String title=getIntent().getStringExtra("title");
        rate=getIntent().getFloatExtra("rate",0f);

        Log.i(TAG,"onCreate: title="+title);
        Log.i(TAG,"onCreate: rate="+rate);
        ((TextView)findViewById(R.id.title2)).setText(title);
        inp2 = (EditText)findViewById(R.id.inp2);
        inp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                TextView show = (TextView) RateCalcActivity.this.findViewById(R.id.show2);
                if(editable.length()>0){
                    float val = Float.parseFloat(editable.toString());
                    show.setText(val + "RMB==> " + (100/rate*val));
                }else{
                    show.setText("");
                }

            }
        });
    }
}
