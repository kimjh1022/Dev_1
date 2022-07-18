package com.example.kim_dev.Clock;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.kim_dev.Login.Login;
import com.example.kim_dev.MainActivity;
import com.example.kim_dev.R;
import com.google.android.material.tabs.TabLayout;

public class Clock extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        // ViewPager
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        Clock_PageAdapter adapter = new Clock_PageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        // 자동Tap_Title
        tabLayout.setupWithViewPager(viewPager);

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
