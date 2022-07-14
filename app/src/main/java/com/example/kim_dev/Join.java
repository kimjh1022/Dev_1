package com.example.kim_dev;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Join extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}
