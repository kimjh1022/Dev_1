package com.example.kim_dev.License;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kim_dev.R;

public class License_delete extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_update);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}
