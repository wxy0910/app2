    package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

    public class MainActivity extends AppCompatActivity {
     TextView out;
        @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week2);
        out =findViewById(R.id.textView);

        EditText input=findViewById(R.id.editText);
        String str=input.getText().toString();
        Button btn=findViewById(R.id.button);
    }
    public void onClick(View v)
    {
        Log.i("main","clicked");
        EditText input =findViewById(R.id.editText);
        String str =input.getText().toString();
        out.setText("Hello," +str);
    }
}
