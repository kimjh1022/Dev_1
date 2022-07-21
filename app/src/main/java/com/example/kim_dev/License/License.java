package com.example.kim_dev.License;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.kim_dev.MainActivity;
import com.example.kim_dev.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class License extends AppCompatActivity {

    // 파이어베이스 데이터베이스 연동
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    TextView u_id;
    Button profile_insert;

    private RecyclerView l_recyclerview;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<License_data> arrayList;

    // 새로고침
    SwipeRefreshLayout refresh_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // (회원) 상단 아이디
        u_id = (TextView) findViewById(R.id.u_id);
        SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);
        String inputText = sharedPreferences.getString("user_id","");
        u_id.setText(inputText);

        profile_insert = (Button) findViewById(R.id.profile_insert);
        profile_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), License_insert.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        l_recyclerview = findViewById(R.id.l_recyclerview);
        l_recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        l_recyclerview.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("License");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    License_data pro = snapshot.getValue(License_data.class);
                    arrayList.add(pro);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(License.this, "오류", Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new License_Adapter(arrayList, this);
        l_recyclerview.setAdapter(adapter);

        // 새로 고침
        refresh_layout = findViewById(R.id.refresh_layout);
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                database = FirebaseDatabase.getInstance();
                databaseReference = database.getReference("License");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        arrayList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            License_data pro = snapshot.getValue(License_data.class);
                            arrayList.add(pro);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(License.this, "오류", Toast.LENGTH_SHORT).show();
                    }
                });
                l_recyclerview.setAdapter(adapter);
                refresh_layout.setRefreshing(false);
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

