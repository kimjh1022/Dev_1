package com.example.kim_dev.License;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kim_dev.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class License_insert extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // 파이어베이스 데이터베이스 연동
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    TextView u_id, l_date;
    EditText l_name, l_num, l_org;
    Button insert, date_btn;

    Spinner org_spinner;
    String[] spinner_item;

    DatePickerDialog datePickerDialog;


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
        l_date = (TextView) findViewById(R.id.l_date);
        l_org = (EditText) findViewById(R.id.l_org);
        insert = (Button) findViewById(R.id.insert);
        date_btn = (Button) findViewById(R.id.date_btn);
        org_spinner = (Spinner) findViewById(R.id.org_spinner);

        // 일자 선택
        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 오늘 날짜(년, 월, 일) 변수에 담기
                Calendar calendar = Calendar.getInstance();
                int l_Year =  calendar.get(Calendar.YEAR);
                int l_Month =  calendar.get(Calendar.MONTH);
                int l_Day =  calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(License_insert.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // 1월은 0부터 시작하기 때문에 +1을 해준다.
                        month = month + 1;
                        String date = year + "/" + month + "/" + day;
                        l_date.setText(date);
                    }
                }, l_Year, l_Month, l_Day);
                datePickerDialog.show();
            }
        });

        // 기관 선택 Spinner
        org_spinner.setOnItemSelectedListener(this);
        spinner_item = new String[]{"","한국산업인력공단","국사편찬위원회","한국정보통신자격협회"};
        ArrayAdapter<String> Spinner_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,spinner_item);
        Spinner_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        org_spinner.setAdapter(Spinner_Adapter);

        // 등록
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
//                                l_name.setText("");
//                                l_num.setText("");
//                                l_date.setText("");
//                                l_org.setText("");

                                Intent intent = new Intent(getApplicationContext(), License.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);

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

    // 기관 선택 Spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        l_org.setText(spinner_item[i]);
        if (l_org.getText().toString().equals("선택하세요")) {
            l_org.setText("");
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView){
        l_org.setText("");
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
