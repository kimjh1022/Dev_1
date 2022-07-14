package com.example.kim_dev;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    Button login_btn, join_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        login_btn = findViewById(R.id.login_btn);
        join_btn = findViewById(R.id.join_btn);

        // 메인 페이지로 이동
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // 회원가입 페이지로 이동
        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Join.class);
                startActivity(intent);
            }
        });
    }
}
