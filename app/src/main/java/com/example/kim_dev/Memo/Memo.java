package com.example.kim_dev.Memo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.kim_dev.License.License;
import com.example.kim_dev.License.License_data;
import com.example.kim_dev.License.License_insert;
import com.example.kim_dev.License.License_update;
import com.example.kim_dev.MainActivity;
import com.example.kim_dev.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Memo extends AppCompatActivity {

    // 파이어베이스 데이터베이스 연동
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    TextView u_id;
    Button m_insert;

    private List<String> m_List = new ArrayList<>(); //게시물 key

    private ArrayList<Memo_data> arrayList;
    private RecyclerView.Adapter MyAppAdapter;
    private RecyclerView m_recyclerview;
    private RecyclerView.LayoutManager layoutManager;

    String Key = "";

    // 새로고침
    SwipeRefreshLayout refresh_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // (회원) 상단 아이디
        u_id = (TextView) findViewById(R.id.u_id);
        SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);
        String inputText = sharedPreferences.getString("user_id","");
        u_id.setText(inputText);

        m_insert = (Button) findViewById(R.id.m_insert);
        m_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Memo_insert.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        m_recyclerview = (RecyclerView) findViewById(R.id.m_recyclerview);
        m_recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        m_recyclerview.setLayoutManager(layoutManager);
        arrayList = new ArrayList<Memo_data>();

        // 리스트 역순 출력
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        m_recyclerview.setLayoutManager(mLayoutManager);

        database = FirebaseDatabase.getInstance();

        // 리스트 정렬
        String date = "_m_day";
        Query databaseReference = FirebaseDatabase.getInstance().getReference().child("Memo").orderByChild(date);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Memo_data pro = snapshot.getValue(Memo_data.class);

                    Key = snapshot.getKey();
                    pro.set_m_key(Key);

                    arrayList.add(pro);
                }
                MyAppAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Memo.this, "오류", Toast.LENGTH_SHORT).show();
            }
        });
        MyAppAdapter = new Memo.MyAppAdapter(arrayList, this);
        m_recyclerview.setAdapter(MyAppAdapter);

        // 새로 고침
        refresh_layout = findViewById(R.id.refresh_layout);
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                database = FirebaseDatabase.getInstance();
                //databaseReference = database.getReference("License");

                // 리스트 정렬
                String date = "_m_day";
                Query databaseReference = FirebaseDatabase.getInstance().getReference().child("Memo").orderByChild(date);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        arrayList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Memo_data pro = snapshot.getValue(Memo_data.class);

                            Key = snapshot.getKey();
                            pro.set_m_key(Key);

                            arrayList.add(pro);
                        }
                        MyAppAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Memo.this, "오류", Toast.LENGTH_SHORT).show();
                    }
                });
                m_recyclerview.setAdapter(MyAppAdapter);
                refresh_layout.setRefreshing(false);
            }
        });
    }

    // 리사이클러뷰 어댑터
    public class MyAppAdapter extends RecyclerView.Adapter<Memo.MyAppAdapter.MemoViewHolder> {
        private List<Memo_data> values;
        public Context context;

        public class MemoViewHolder extends RecyclerView.ViewHolder {
            TextView m_day;
            TextView m_title;
            TextView m_key;

            public View layout;

            public MemoViewHolder(View v)
            {
                super(v);
                layout = v;
                this.m_day = itemView.findViewById(R.id.m_day);
                this.m_title = itemView.findViewById(R.id.m_title);
                this.m_key = itemView.findViewById(R.id.m_key);
            }
        }

        public MyAppAdapter(List<Memo_data> myDataset, Context context) {
            values = myDataset;
            this.context = context;
        }

        @Override
        public Memo.MyAppAdapter.MemoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.activity_memo_item, parent, false);
            Memo.MyAppAdapter.MemoViewHolder vh = new Memo.MyAppAdapter.MemoViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull Memo.MyAppAdapter.MemoViewHolder holder, final int position) {

            final Memo_data classListItems = arrayList.get(position);

            holder.m_day.setText(arrayList.get(position).get_m_day());
            holder.m_title.setText(arrayList.get(position).get_m_title());
            //holder.m_content.setText(arrayList.get(position).get_m_content());
            holder.m_key.setText(arrayList.get(position).get_m_key());

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), Memo_update.class);
                    intent.putExtra("m_key", classListItems.get_m_key());
                    intent.putExtra("m_day", classListItems.get_m_day());
                    intent.putExtra("m_title", classListItems.get_m_title());
                    intent.putExtra("m_content", classListItems.get_m_content());
                    context.startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                }
            });
        }

        @Override
        public int getItemCount() {
            return values.size();
        }
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
