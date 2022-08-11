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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kim_dev.License.License;
import com.example.kim_dev.License.License_update;
import com.example.kim_dev.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Memo_update extends AppCompatActivity {

    // 파이어베이스 데이터베이스 연동
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    TextView u_id, m_day, m_key;
    EditText m_title, m_content;

    String sKey;

    Button m_update_btn, m_delete_btn;

    public static String format_yyyyMMdd = "yyyy/MM/dd";
    Date currentTime = Calendar.getInstance().getTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_update);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        m_key = (TextView) findViewById(R.id.m_key);
        m_day = (TextView) findViewById(R.id.m_day);
        m_title = (EditText) findViewById(R.id.m_title);
        m_content = (EditText) findViewById(R.id.m_content);
        m_update_btn = (Button) findViewById(R.id.m_update_btn);
        m_delete_btn = (Button) findViewById(R.id.m_delete_btn);

        // (회원) 상단 아이디
        u_id = (TextView) findViewById(R.id.u_id);
        SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);
        String inputText = sharedPreferences.getString("user_id","");
        u_id.setText(inputText);

        // (품목) 불러오기
        Intent intent = getIntent();
        m_key.setText(intent.getStringExtra("m_key"));
        m_day.setText(intent.getStringExtra("m_day"));
        m_title.setText(intent.getStringExtra("m_title"));
        m_content.setText(intent.getStringExtra("m_content"));
        sKey = getIntent().getStringExtra("m_key");

        m_update_btn.setEnabled(false);
        m_update_btn.setTextColor(Color.parseColor("#CDCDCD"));
        m_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(m_title.length() > 0 && m_content.length() > 0){
                    m_update_btn.setTextColor(Color.parseColor("#000000"));
                    m_update_btn.setEnabled(true);
                } else {
                    m_update_btn.setTextColor(Color.parseColor("#CDCDCD"));
                    m_update_btn.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        m_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(m_title.length() > 0 && m_content.length() > 0){
                    m_update_btn.setTextColor(Color.parseColor("#000000"));
                    m_update_btn.setEnabled(true);
                } else {
                    m_update_btn.setTextColor(Color.parseColor("#CDCDCD"));
                    m_update_btn.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        m_update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat format = new SimpleDateFormat(format_yyyyMMdd, Locale.getDefault());
                String current = format.format(currentTime);
                m_day.setText(current);

                // 변경값
                String u_m_day = m_day.getText().toString();
                String u_m_title = m_title.getText().toString();
                String u_m_content = m_content.getText().toString();

                // 파라미터 셋팅
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("_m_day", u_m_day);
                hashMap.put("_m_title", u_m_title);
                hashMap.put("_m_content", u_m_content);

                new AlertDialog.Builder(Memo_update.this)
                        .setMessage("수정 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which){

                                update(sKey, hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Intent intent = new Intent(getApplicationContext(), Memo.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);

                                        Toast.makeText(getApplicationContext(), "업데이트 성공", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "업데이트 실패", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which){

                            }
                        })
                        .show();
            }
        });


        m_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(Memo_update.this)
                        .setMessage("삭제 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which){

                                delete(sKey).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Intent intent = new Intent(getApplicationContext(), Memo.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);

                                        Toast.makeText(getApplicationContext(), "삭제 성공", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "삭제 실패", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which){

                            }
                        })
                        .show();
            }
        });
    }

    // 업데이트
    public Task<Void> update(String key, HashMap<String, Object> hashMap) {
        return databaseReference.child("Memo").child(key).updateChildren(hashMap);
        //return databaseReference.child(key).updateChildren(hashMap);
    }

    // 삭제
    public Task<Void> delete(String key) {
        return databaseReference.child("Memo").child(key).removeValue();
        //return databaseReference.child(key).updateChildren(hashMap);
    }


    // 뒤로가기
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Memo.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        finish();
        super.onBackPressed();
    }
}

