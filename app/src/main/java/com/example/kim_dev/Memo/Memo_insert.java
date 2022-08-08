package com.example.kim_dev.Memo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kim_dev.License.License;
import com.example.kim_dev.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Memo_insert extends AppCompatActivity {

    // 파이어베이스 데이터베이스 연동
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    TextView u_id, m_day;
    EditText m_title, m_content;
    Button m_insert_btn;

    public static String format_yyyyMMdd = "yyyy/MM/dd";
    Date currentTime = Calendar.getInstance().getTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_insert);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        m_day = (TextView) findViewById(R.id.m_day);
        m_title = (EditText) findViewById(R.id.m_title);
        m_content = (EditText) findViewById(R.id.m_content);
        m_insert_btn = (Button) findViewById(R.id.m_insert_btn);

        // (회원) 상단 아이디
        u_id = (TextView) findViewById(R.id.u_id);
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String inputText = sharedPreferences.getString("user_id", "");
        u_id.setText(inputText);

        SimpleDateFormat format = new SimpleDateFormat(format_yyyyMMdd, Locale.getDefault());
        String current = format.format(currentTime);
        m_day.setText(current);

        // 등록
        m_insert_btn.setEnabled(false);
        m_insert_btn.setTextColor(Color.parseColor("#CDCDCD"));
        m_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (m_title.length() > 0 && m_content.length() > 0) {
                    m_insert_btn.setTextColor(Color.parseColor("#000000"));
                    m_insert_btn.setEnabled(true);
                } else {
                    m_insert_btn.setTextColor(Color.parseColor("#CDCDCD"));
                    m_insert_btn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        m_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (m_title.length() > 0 && m_content.length() > 0) {
                    m_insert_btn.setTextColor(Color.parseColor("#000000"));
                    m_insert_btn.setEnabled(true);
                } else {
                    m_insert_btn.setTextColor(Color.parseColor("#CDCDCD"));
                    m_insert_btn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        m_insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Memo_insert.this)
                        .setMessage("등록 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                SimpleDateFormat format = new SimpleDateFormat(format_yyyyMMdd, Locale.getDefault());
                                String current = format.format(currentTime);
                                m_day.setText(current);

                                add(u_id.getText().toString(), m_day.getText().toString(), m_title.getText().toString(), m_content.getText().toString());

                                Intent intent = new Intent(getApplicationContext(), Memo.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);

                                Toast.makeText(Memo_insert.this, "등록되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
    }

    // 값을 파이어베이스 Realtime database로 넘김
    public void add(String id, String m_day, String m_title, String m_content) {
        //Profile_data.java 에서 선언했던 함수
        Memo_data Memo_data = new Memo_data(id, m_day, m_title, m_content);

        //child 는 해당 키 위치로 이동하는 함수입니다.
        //키가 없는데 값을 지정한 경우 자동으로 생성합니다.
        databaseReference.child("Memo").push().setValue(Memo_data);
    }

    // 뒤로가기
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), License.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        finish();
        super.onBackPressed();
    }
}
