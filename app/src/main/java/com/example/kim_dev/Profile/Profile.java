package com.example.kim_dev.Profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kim_dev.MainActivity;
import com.example.kim_dev.R;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

    }

    // 뒤로가기
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        finish();
        super.onBackPressed();
    }
}
