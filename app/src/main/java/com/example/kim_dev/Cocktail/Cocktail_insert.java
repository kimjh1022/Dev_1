package com.example.kim_dev.Cocktail;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import com.example.kim_dev.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Cocktail_insert extends AppCompatActivity {

    // 파이어베이스 데이터베이스 연동
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    private static final String TAG = "Cock";

    private TextView u_id;
    private TextView c_day;
    private TextView image_name;
    private EditText c_name;
    private EditText c_content;
    private Button c_choose;
    private Button c_upload;
    private ImageView c_image;

    private Uri filePath;

    public static String format_yyyyMMdd = "yyyy/MM/dd";
    Date currentTime = Calendar.getInstance().getTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktail_insert);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        c_day = (TextView) findViewById(R.id.c_day);
        image_name = (TextView) findViewById(R.id.image_name);
        c_name = (EditText) findViewById(R.id.c_name);
        c_content = (EditText) findViewById(R.id.c_content);
        c_choose = (Button) findViewById(R.id.c_choose);
        c_upload = (Button) findViewById(R.id.c_upload);
        c_image = (ImageView) findViewById(R.id.c_image);


        // (회원) 상단 아이디
        u_id = (TextView) findViewById(R.id.u_id);
        SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);
        String inputText = sharedPreferences.getString("user_id","");
        u_id.setText(inputText);

        SimpleDateFormat format = new SimpleDateFormat(format_yyyyMMdd, Locale.getDefault());
        String current = format.format(currentTime);
        c_day.setText(current);

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


        // 등록
        c_upload.setEnabled(false);
        c_upload.setTextColor(Color.parseColor("#CDCDCD"));
        c_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (c_name.length() > 0 && c_content.length() > 0 && image_name.length() > 0) {
                    c_upload.setTextColor(Color.parseColor("#000000"));
                    c_upload.setEnabled(true);
                } else {
                    c_upload.setTextColor(Color.parseColor("#CDCDCD"));
                    c_upload.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        c_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (c_name.length() > 0 && c_content.length() > 0 && image_name.length() > 0) {
                    c_upload.setTextColor(Color.parseColor("#000000"));
                    c_upload.setEnabled(true);
                } else {
                    c_upload.setTextColor(Color.parseColor("#CDCDCD"));
                    c_upload.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        image_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (c_name.length() > 0 && c_content.length() > 0 && image_name.length() > 0) {
                    c_upload.setTextColor(Color.parseColor("#000000"));
                    c_upload.setEnabled(true);
                } else {
                    c_upload.setTextColor(Color.parseColor("#CDCDCD"));
                    c_upload.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        c_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Cocktail_insert.this)
                        .setMessage("등록 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText(getApplicationContext(), "업로드중...", Toast.LENGTH_SHORT).show();
                                //업로드
                                uploadFile();

                                Intent intent = new Intent(getApplicationContext(), Cocktail.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
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

    //upload the file
    private void uploadFile() {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();

            //Unique한 파일명을 만들자.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            String filename = formatter.format(now) + ".png";
            image_name.setText(filename);

            SimpleDateFormat format = new SimpleDateFormat(format_yyyyMMdd, Locale.getDefault());
            String current = format.format(currentTime);
            c_day.setText(current);

            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://kim-dev-d7e55.appspot.com").child("Cocktail/" + filename);
            //올라가거라...
            storageRef.putFile(filePath)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            add(u_id.getText().toString(), c_day.getText().toString(), c_name.getText().toString(), image_name.getText().toString(), c_content.getText().toString());
                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                            double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }

    // 값을 파이어베이스 Realtime database로 넘김
    public void add(String id, String c_day, String c_name, String image_name, String c_content) {
        //Profile_data.java 에서 선언했던 함수
        Cocktail_data Cocktail_data = new Cocktail_data(id, c_day, c_name, image_name, c_content);

        //child 는 해당 키 위치로 이동하는 함수입니다.
        //키가 없는데 값을 지정한 경우 자동으로 생성합니다.
        databaseReference.child("Cocktail").push().setValue(Cocktail_data);
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


