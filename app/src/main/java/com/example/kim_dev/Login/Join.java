package com.example.kim_dev.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kim_dev.MainActivity;
import com.example.kim_dev.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Join extends AppCompatActivity {

    private static final String TAG = "Join";
    EditText u_name, id, pw, pw_ck;
    Button join_btn;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        u_name = (EditText) findViewById(R.id.u_name);
        id = (EditText) findViewById(R.id.id);
        pw = (EditText) findViewById(R.id.pw);
        pw_ck = (EditText) findViewById(R.id.pw_ck);
        join_btn = (Button) findViewById(R.id.join_btn);

        //파이어베이스 접근 설정
        // user = firebaseAuth.getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();
        //firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        join_btn.setEnabled(false);
        join_btn.setTextColor(Color.parseColor("#CDCDCD"));
        u_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(u_name.length() > 0 && id.length() > 0 && pw.length() > 0 && pw_ck.length() > 0){
                    join_btn.setTextColor(Color.parseColor("#000000"));
                    join_btn.setEnabled(true);
                } else {
                    join_btn.setTextColor(Color.parseColor("#CDCDCD"));
                    join_btn.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(u_name.length() > 0 && id.length() > 0 && pw.length() > 0 && pw_ck.length() > 0){
                    join_btn.setTextColor(Color.parseColor("#000000"));
                    join_btn.setEnabled(true);
                } else {
                    join_btn.setTextColor(Color.parseColor("#CDCDCD"));
                    join_btn.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(u_name.length() > 0 && id.length() > 0 && pw.length() > 0 && pw_ck.length() > 0){
                    join_btn.setTextColor(Color.parseColor("#000000"));
                    join_btn.setEnabled(true);
                } else {
                    join_btn.setTextColor(Color.parseColor("#CDCDCD"));
                    join_btn.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        pw_ck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(u_name.length() > 0 && id.length() > 0 && pw.length() > 0 && pw_ck.length() > 0){
                    join_btn.setTextColor(Color.parseColor("#000000"));
                    join_btn.setEnabled(true);
                } else {
                    join_btn.setTextColor(Color.parseColor("#CDCDCD"));
                    join_btn.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //파이어베이스 user 로 접근
        //가입버튼 클릭리스너   -->  firebase에 데이터를 저장한다.
        join_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //가입 정보 가져오기
                final String email = id.getText().toString().trim();
                String pwd = pw.getText().toString().trim();
                String pwdcheck = pw_ck.getText().toString().trim();

                if (pwd.equals(pwdcheck)) {
                    Log.d(TAG, "등록 버튼 " + email + " , " + pwd);
                    final ProgressDialog mDialog = new ProgressDialog(Join.this);
                    mDialog.setMessage("가입중입니다...");
                    mDialog.show();

                    //파이어베이스에 신규계정 등록하기
                    firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(Join.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //가입 성공시
                            if (task.isSuccessful()) {
                                mDialog.dismiss();

                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                String email = user.getEmail();
                                String uid = user.getUid();
                                String name = u_name.getText().toString().trim();

                                //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                                HashMap<Object, String> hashMap = new HashMap<>();

                                hashMap.put("uid", uid);
                                hashMap.put("email", email);
                                hashMap.put("name", name);

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("Users");
                                reference.child(uid).setValue(hashMap);

                                //가입이 이루어져을시 가입 화면을 빠져나감.
                                Intent intent = new Intent(Join.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                                finish();
                                Toast.makeText(Join.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                            } else {
                                mDialog.dismiss();
                                Toast.makeText(Join.this, "이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                                return;  //해당 메소드 진행을 멈추고 빠져나감.

                            }
                        }
                    });
                    //비밀번호 오류시
                } else {
                    Toast.makeText(Join.this, "비밀번호가 틀렸습니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    // 뒤로가기
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        finish();
        super.onBackPressed();
    }
}

























