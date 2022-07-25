package com.example.kim_dev.License;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kim_dev.MainActivity;
import com.example.kim_dev.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.Executor;

public class License_update extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    // 파이어베이스 데이터베이스 연동
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    TextView u_id;
    TextView l_key, l_date;
    EditText l_name, l_num, l_org;

    String sKey;

    Button update, delete, date_btn;

    Spinner org_spinner;
    String[] spinner_item;

    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_update);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        l_key = (TextView) findViewById(R.id.l_key);
        l_name = (EditText) findViewById(R.id.l_name);
        l_num = (EditText) findViewById(R.id.l_num);
        l_date = (TextView) findViewById(R.id.l_date);
        l_org = (EditText) findViewById(R.id.l_org);
        date_btn = (Button) findViewById(R.id.date_btn);
        org_spinner = (Spinner) findViewById(R.id.org_spinner);

        // (회원) 상단 아이디
        u_id = (TextView) findViewById(R.id.u_id);
        SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);
        String inputText = sharedPreferences.getString("user_id","");
        u_id.setText(inputText);

        // (품목) 불러오기
        Intent intent = getIntent();
        l_key.setText(intent.getStringExtra("l_key"));
        l_name.setText(intent.getStringExtra("l_name"));
        l_num.setText(intent.getStringExtra("l_num"));
        l_date.setText(intent.getStringExtra("l_date"));
        l_org.setText(intent.getStringExtra("l_org"));
        sKey = getIntent().getStringExtra("l_key");

        // 일자 선택
        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 오늘 날짜(년, 월, 일) 변수에 담기
                Calendar calendar = Calendar.getInstance();
                int l_Year =  calendar.get(Calendar.YEAR);
                int l_Month =  calendar.get(Calendar.MONTH);
                int l_Day =  calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(License_update.this, new DatePickerDialog.OnDateSetListener() {
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
        spinner_item = new String[]{l_org.getText().toString(),"한국산업인력공단","국사편찬위원회","한국정보통신자격협회"};
        ArrayAdapter<String> Spinner_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,spinner_item);
        Spinner_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        org_spinner.setAdapter(Spinner_Adapter);

        update = (Button) findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // 변경값
                String u_l_name = l_name.getText().toString();
                String u_l_num = l_num.getText().toString();
                String u_l_date = l_date.getText().toString();
                String u_l_org = l_org.getText().toString();

                // 파라미터 셋팅
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("_l_name", u_l_name);
                hashMap.put("_l_num", u_l_num);
                hashMap.put("_l_date", u_l_date);
                hashMap.put("_l_org", u_l_org);

                new AlertDialog.Builder(License_update.this)
                        .setMessage("수정 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which){

                                update(sKey, hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Intent intent = new Intent(getApplicationContext(), License.class);
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

        delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(License_update.this)
                        .setMessage("삭제 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which){

                                delete(sKey).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Intent intent = new Intent(getApplicationContext(), License.class);
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

    // 업데이트
    public Task<Void> update(String key, HashMap<String, Object> hashMap) {

        return databaseReference.child("License").child(key).updateChildren(hashMap);
        //return databaseReference.child(key).updateChildren(hashMap);
    }

    // 삭제
    public Task<Void> delete(String key) {

        return databaseReference.child("License").child(key).removeValue();
        //return databaseReference.child(key).updateChildren(hashMap);
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
