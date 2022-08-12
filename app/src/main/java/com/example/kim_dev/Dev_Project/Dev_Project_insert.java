package com.example.kim_dev.Dev_Project;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kim_dev.MainActivity;
import com.example.kim_dev.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Dev_Project_insert extends AppCompatActivity {

    private TextView u_id;
    private ImageView p_image;
    private Button p_upload;
    private ProgressBar progress_View;
    private final DatabaseReference root = FirebaseDatabase.getInstance().getReference("Git");
    private final StorageReference reference = FirebaseStorage.getInstance().getReference();

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_project_insert);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // (회원) 상단 아이디
        u_id = (TextView) findViewById(R.id.u_id);
        SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);
        String inputText = sharedPreferences.getString("user_id","");
        u_id.setText(inputText);

        // 컴포넌트 객체에 담기
        p_upload = findViewById(R.id.p_upload);
        progress_View = findViewById(R.id.progress_View);
        p_image = findViewById(R.id.p_image);

        // 프로그래스바 숨기기
        progress_View.setVisibility(View.INVISIBLE);

        // 이미지 클릭 이벤트
        p_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                activityResult.launch(galleryIntent);

            }
        });

        p_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 선택한 이미지가 있다면
                if (imageUri != null) {
                    uploadToFirebase(imageUri);
                } else {
                    Toast.makeText(Dev_Project_insert.this, "사진을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 사진 가져오기
    ActivityResultLauncher<Intent> activityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        p_image.setImageURI(imageUri);
                    }
                }
            });

    // 파이어베이스 이미지 업로드드
    private void uploadToFirebase(Uri uri) {
        StorageReference fileRef = reference.child("Git/" + System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // 이미지 담기
                        Dev_Project_data Dev_Project_data = new Dev_Project_data(uri.toString());

                        // 키로 아이디 생성
                        String Dev_Project_data_id = root.push().getKey();

                        // 데이터 넣기
                        root.child(Dev_Project_data_id).setValue(Dev_Project_data);

                        // 프로그래스바 숨김
                        progress_View.setVisibility(View.INVISIBLE);
                        Toast.makeText(Dev_Project_insert.this, "업로드 성공", Toast.LENGTH_SHORT).show();
                        p_image.setImageResource(R.drawable.ic_baseline_add_a_photo_24);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                // 프로그래스바 보여주기
                progress_View.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // 프로그래스바 숨김
                progress_View.setVisibility(View.INVISIBLE);
                Toast.makeText(Dev_Project_insert.this, "업로드 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 파일타입 가져오기
    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    // 뒤로가기
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Dev_Project.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        finish();
        super.onBackPressed();
    }
}


















