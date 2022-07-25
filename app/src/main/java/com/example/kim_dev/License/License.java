package com.example.kim_dev.License;

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

import com.example.kim_dev.MainActivity;
import com.example.kim_dev.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class License extends AppCompatActivity {

    // 파이어베이스 데이터베이스 연동
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    TextView u_id,l_key;
    Button profile_insert;
    private List<String> l_List = new ArrayList<>(); //게시물 key

    private ArrayList<License_data> arrayList;
    private RecyclerView.Adapter MyAppAdapter;
    private RecyclerView l_recyclerview;
    private RecyclerView.LayoutManager layoutManager;

    String Key = "";

    // 새로고침
    SwipeRefreshLayout refresh_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        l_key = (TextView) findViewById(R.id.l_key);

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

        l_recyclerview = (RecyclerView) findViewById(R.id.l_recyclerview);
        l_recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        l_recyclerview.setLayoutManager(layoutManager);
        arrayList = new ArrayList<License_data>();

        // 리스트 역순 출력
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        l_recyclerview.setLayoutManager(mLayoutManager);


        database = FirebaseDatabase.getInstance();
        //databaseReference = database.getReference("License");

        // 리스트 정렬
        String date = "_l_date";
        Query databaseReference = FirebaseDatabase.getInstance().getReference().child("License").orderByChild(date);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    License_data pro = snapshot.getValue(License_data.class);

                    Key = snapshot.getKey();
                    pro.set_l_key(Key);

                    arrayList.add(pro);
                }
                MyAppAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(License.this, "오류", Toast.LENGTH_SHORT).show();
            }
        });
        MyAppAdapter = new MyAppAdapter(arrayList, this);
        l_recyclerview.setAdapter(MyAppAdapter);

        // 새로 고침
        refresh_layout = findViewById(R.id.refresh_layout);
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                database = FirebaseDatabase.getInstance();
                //databaseReference = database.getReference("License");

                // 리스트 정렬
                String date = "_l_date";
                Query databaseReference = FirebaseDatabase.getInstance().getReference().child("License").orderByChild(date);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        arrayList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            License_data pro = snapshot.getValue(License_data.class);

                            Key = snapshot.getKey();
                            pro.set_l_key(Key);

                            arrayList.add(pro);
                        }
                        MyAppAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(License.this, "오류", Toast.LENGTH_SHORT).show();
                    }
                });
                l_recyclerview.setAdapter(MyAppAdapter);
                refresh_layout.setRefreshing(false);
            }
        });
    }

    // 리사이클러뷰 어댑터
    public class MyAppAdapter extends RecyclerView.Adapter<MyAppAdapter.LicenseViewHolder> {
        private List<License_data> values;
        public Context context;

        public class LicenseViewHolder extends RecyclerView.ViewHolder {
            TextView l_name;
            TextView l_num;
            TextView l_date;
            TextView l_org;
            TextView l_key;

            public View layout;

            public LicenseViewHolder(View v)
            {
                super(v);
                layout = v;
                this.l_name = itemView.findViewById(R.id.l_name);
                this.l_num = itemView.findViewById(R.id.l_num);
                this.l_date = itemView.findViewById(R.id.l_date);
                this.l_org = itemView.findViewById(R.id.l_org);
                this.l_key = itemView.findViewById(R.id.l_key);
            }
        }

        public MyAppAdapter(List<License_data> myDataset, Context context) {
            values = myDataset;
            this.context = context;
        }

        @Override
        public MyAppAdapter.LicenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.activity_license_item, parent, false);
            MyAppAdapter.LicenseViewHolder vh = new MyAppAdapter.LicenseViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull MyAppAdapter.LicenseViewHolder holder, final int position) {

            final License_data classListItems = arrayList.get(position);

            holder.l_name.setText(arrayList.get(position).get_l_name());
            holder.l_num.setText(arrayList.get(position).get_l_num());
            holder.l_date.setText(arrayList.get(position).get_l_date());
            holder.l_org.setText(arrayList.get(position).get_l_org());
            holder.l_key.setText(arrayList.get(position).get_l_key());

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), License_update.class);
                    intent.putExtra("l_key", classListItems.get_l_key());
                    intent.putExtra("l_name", classListItems.get_l_name());
                    intent.putExtra("l_num", classListItems.get_l_num());
                    intent.putExtra("l_date", classListItems.get_l_date());
                    intent.putExtra("l_org", classListItems.get_l_org());
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

