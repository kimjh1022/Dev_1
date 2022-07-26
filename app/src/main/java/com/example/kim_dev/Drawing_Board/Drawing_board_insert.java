package com.example.kim_dev.Drawing_Board;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kim_dev.License.License;
import com.example.kim_dev.R;

public class Drawing_board_insert extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_board_insert);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    // 뒤로가기
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Drawing_board.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        finish();
        super.onBackPressed();
    }
}

