package com.example.kim_dev.Drawing_Board;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.kim_dev.License.License_data;
import com.example.kim_dev.License.License_insert;
import com.example.kim_dev.MainActivity;
import com.example.kim_dev.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Drawing_board extends AppCompatActivity {

    // 파이어베이스 데이터베이스 연동
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    TextView u_id,l_key;
    Button drawing_insert;
//    private List<String> l_List = new ArrayList<>(); //게시물 key
//
//    private ArrayList<License_data> arrayList;
//    private RecyclerView.Adapter MyAppAdapter;
//    private RecyclerView l_recyclerview;
//    private RecyclerView.LayoutManager layoutManager;
//
//    String Key = "";

    // 새로고침
//    SwipeRefreshLayout refresh_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_board);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // (회원) 상단 아이디
        u_id = (TextView) findViewById(R.id.u_id);
        SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);
        String inputText = sharedPreferences.getString("user_id","");
        u_id.setText(inputText);

        drawing_insert = (Button) findViewById(R.id.drawing_insert);
        drawing_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Drawing_board_insert.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

    }

    // 뒤로가기
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        finish();
        super.onBackPressed();
    }
}

