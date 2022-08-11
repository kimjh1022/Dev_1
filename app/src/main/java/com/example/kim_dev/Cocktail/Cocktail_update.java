package com.example.kim_dev.Cocktail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.kim_dev.Memo.Memo;
import com.example.kim_dev.Memo.Memo_update;
import com.example.kim_dev.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Cocktail_update extends AppCompatActivity {

    private static final String TAG = "Cock";

    // 파이어베이스 데이터베이스 연동
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    TextView u_id, c_day, image_name, c_key;
    EditText c_name, c_content;
    Button c_choose, c_update, c_delete;
    private ImageView c_image;

    String sKey;

    private Uri filePath;

    public static String format_yyyyMMdd = "yyyy/MM/dd";
    Date currentTime = Calendar.getInstance().getTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktail_update);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        c_day = (TextView) findViewById(R.id.c_day);
        image_name = (TextView) findViewById(R.id.image_name);
        c_key = (TextView) findViewById(R.id.c_key);
        c_name = (EditText) findViewById(R.id.c_name);
        c_content = (EditText) findViewById(R.id.c_content);
        c_image = (ImageView) findViewById(R.id.c_image);
        c_choose = (Button) findViewById(R.id.c_choose);
        c_update = (Button) findViewById(R.id.c_update);
        c_delete = (Button) findViewById(R.id.c_delete);

        // (회원) 상단 아이디
        u_id = (TextView) findViewById(R.id.u_id);
        SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);
        String inputText = sharedPreferences.getString("user_id","");
        u_id.setText(inputText);

        // (품목) 불러오기
        Intent intent = getIntent();
        c_day.setText(intent.getStringExtra("c_day"));
        image_name.setText(intent.getStringExtra("image_name"));
        c_name.setText(intent.getStringExtra("c_name"));
        c_content.setText(intent.getStringExtra("c_content"));
        c_key.setText(intent.getStringExtra("c_key"));
        sKey = getIntent().getStringExtra("c_key");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference pathReference = storageReference.child("Cocktail");
        if (pathReference == null) {
            Toast.makeText(Cocktail_update.this, "저장소에 사진이 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            StorageReference submitProfile = storageReference.child("Cocktail/" + image_name.getText());
            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(Cocktail_update.this).load(uri).into(c_image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }

        //버튼 클릭 이벤트
        c_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //이미지를 선택
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
            }
        });

        c_update.setEnabled(false);
        c_update.setTextColor(Color.parseColor("#CDCDCD"));
        c_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(c_name.length() > 0 && c_content.length() > 0){
                    c_update.setTextColor(Color.parseColor("#000000"));
                    c_update.setEnabled(true);
                } else {
                    c_update.setTextColor(Color.parseColor("#CDCDCD"));
                    c_update.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        c_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(c_name.length() > 0 && c_content.length() > 0){
                    c_update.setTextColor(Color.parseColor("#000000"));
                    c_update.setEnabled(true);
                } else {
                    c_update.setTextColor(Color.parseColor("#CDCDCD"));
                    c_update.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        c_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat format = new SimpleDateFormat(format_yyyyMMdd, Locale.getDefault());
                String current = format.format(currentTime);
                c_day.setText(current);

                // 변경값
                String u_c_day = c_day.getText().toString();
                String u_c_name = c_name.getText().toString();
                String u_c_content = c_content.getText().toString();

                // 파라미터 셋팅
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("_c_day", u_c_day);
                hashMap.put("_c_name", u_c_name);
                hashMap.put("_c_content", u_c_content);

                new AlertDialog.Builder(Cocktail_update.this)
                        .setMessage("수정 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which){

                                update(sKey, hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Intent intent = new Intent(getApplicationContext(), Cocktail.class);
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


        // 삭제
        c_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(Cocktail_update.this)
                        .setMessage("삭제 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which){

                                StorageReference storageRef = storage.getReferenceFromUrl("gs://kim-dev-d7e55.appspot.com").child("Cocktail/" + image_name.getText());
                                storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        delete(sKey).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Intent intent = new Intent(getApplicationContext(), Cocktail.class);
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
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "삭제 실패2", Toast.LENGTH_SHORT).show();
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

    //결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            Log.d(TAG, "uri:" + String.valueOf(filePath));
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                c_image.setImageBitmap(bitmap);
                image_name.setText(" ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 업데이트
    public Task<Void> update(String key, HashMap<String, Object> hashMap) {
        return databaseReference.child("Cocktail").child(key).updateChildren(hashMap);
        //return databaseReference.child(key).updateChildren(hashMap);
    }

    // 삭제
    public Task<Void> delete(String key) {
        return databaseReference.child("Cocktail").child(key).removeValue();
        //return databaseReference.child(key).updateChildren(hashMap);
    }



    // 뒤로가기
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Cocktail.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        finish();
        super.onBackPressed();
    }
}

