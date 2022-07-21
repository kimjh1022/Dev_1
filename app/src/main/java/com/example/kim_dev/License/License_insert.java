package com.example.kim_dev.License;

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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kim_dev.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class License_insert extends AppCompatActivity {

    // 파이어베이스 데이터베이스 연동
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    TextView u_id;
    EditText l_name, l_num, l_date, l_org;
    Button insert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_insert);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // (회원) 상단 아이디
        u_id = (TextView) findViewById(R.id.u_id);
        SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);
        String inputText = sharedPreferences.getString("user_id","");
        u_id.setText(inputText);

        l_name = (EditText) findViewById(R.id.l_name);
        l_num = (EditText) findViewById(R.id.l_num);
        l_date = (EditText) findViewById(R.id.l_date);
        l_org = (EditText) findViewById(R.id.l_org);
        insert = (Button) findViewById(R.id.insert);

        insert.setEnabled(false);
        insert.setTextColor(Color.parseColor("#CDCDCD"));
        l_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(l_name.length() > 0 && l_num.length() > 0 && l_date.length() > 0 && l_org.length() > 0){
                    insert.setTextColor(Color.parseColor("#000000"));
                    insert.setEnabled(true);
                } else {
                    insert.setTextColor(Color.parseColor("#CDCDCD"));
                    insert.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        l_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(l_name.length() > 0 && l_num.length() > 0 && l_date.length() > 0 && l_org.length() > 0){
                    insert.setTextColor(Color.parseColor("#000000"));
                    insert.setEnabled(true);
                } else {
                    insert.setTextColor(Color.parseColor("#CDCDCD"));
                    insert.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        l_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(l_name.length() > 0 && l_num.length() > 0 && l_date.length() > 0 && l_org.length() > 0){
                    insert.setTextColor(Color.parseColor("#000000"));
                    insert.setEnabled(true);
                } else {
                    insert.setTextColor(Color.parseColor("#CDCDCD"));
                    insert.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        l_org.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(l_name.length() > 0 && l_num.length() > 0 && l_date.length() > 0 && l_org.length() > 0){
                    insert.setTextColor(Color.parseColor("#000000"));
                    insert.setEnabled(true);
                } else {
                    insert.setTextColor(Color.parseColor("#CDCDCD"));
                    insert.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(License_insert.this)
                        .setMessage("입력 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which){
                                add(u_id.getText().toString(), l_name.getText().toString(), l_num.getText().toString(), l_date.getText().toString(), l_org.getText().toString());
                                l_name.setText("");
                                l_num.setText("");
                                l_date.setText("");
                                l_org.setText("");
                                Toast.makeText(License_insert.this, "등록되었습니다.", Toast.LENGTH_SHORT).show();
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

    // 값을 파이어베이스 Realtime database로 넘김
    public void add(String id, String l_name, String l_num, String l_date, String l_org) {
        //Profile_data.java 에서 선언했던 함수
        License_data License_data = new License_data(id, l_name, l_num, l_date, l_org);

        //child 는 해당 키 위치로 이동하는 함수입니다.
        //키가 없는데 값을 지정한 경우 자동으로 생성합니다.
        databaseReference.child("License").push().setValue(License_data);
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
