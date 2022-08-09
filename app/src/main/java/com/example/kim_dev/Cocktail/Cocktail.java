package com.example.kim_dev.Cocktail;

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
import com.example.kim_dev.image.image;
import com.example.kim_dev.image.image_data;
import com.example.kim_dev.image.image_insert;
import com.example.kim_dev.image.image_update;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Cocktail extends AppCompatActivity {

    // 파이어베이스 데이터베이스 연동
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    TextView u_id;
    Button c_insert;

    private List<String> cocktail_List = new ArrayList<>(); //게시물 key

    private ArrayList<Cocktail_data> arrayList;
    private RecyclerView.Adapter MyAppAdapter;
    private RecyclerView c_recyclerview;
    private RecyclerView.LayoutManager layoutManager;

    String Key = "";

    // 새로고침
    SwipeRefreshLayout refresh_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktail);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // (회원) 상단 아이디
        u_id = (TextView) findViewById(R.id.u_id);
        SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);
        String inputText = sharedPreferences.getString("user_id","");
        u_id.setText(inputText);

        c_insert = (Button) findViewById(R.id.c_insert);
        c_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Cocktail_insert.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        c_recyclerview = (RecyclerView) findViewById(R.id.c_recyclerview);
        c_recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        c_recyclerview.setLayoutManager(layoutManager);
        arrayList = new ArrayList<Cocktail_data>();

        // 리스트 역순 출력
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        c_recyclerview.setLayoutManager(mLayoutManager);

        database = FirebaseDatabase.getInstance();

        // 리스트 정렬
        String date = "_c_name";
        Query databaseReference = FirebaseDatabase.getInstance().getReference().child("Cocktail").orderByChild(date);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Cocktail_data pro = snapshot.getValue(Cocktail_data.class);

                    Key = snapshot.getKey();
                    pro.set_c_key(Key);

                    arrayList.add(pro);
                }
                MyAppAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Cocktail.this, "오류", Toast.LENGTH_SHORT).show();
            }
        });
        MyAppAdapter = new Cocktail.MyAppAdapter(arrayList, this);
        c_recyclerview.setAdapter(MyAppAdapter);

        // 새로 고침
        refresh_layout = findViewById(R.id.refresh_layout);
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                database = FirebaseDatabase.getInstance();
                //databaseReference = database.getReference("License");

                // 리스트 정렬
                String date = "_c_name";
                Query databaseReference = FirebaseDatabase.getInstance().getReference().child("Cocktail").orderByChild(date);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        arrayList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Cocktail_data pro = snapshot.getValue(Cocktail_data.class);

                            Key = snapshot.getKey();
                            pro.set_c_key(Key);

                            arrayList.add(pro);
                        }
                        MyAppAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Cocktail.this, "오류", Toast.LENGTH_SHORT).show();
                    }
                });
                c_recyclerview.setAdapter(MyAppAdapter);
                refresh_layout.setRefreshing(false);
            }
        });
    }


    // 리사이클러뷰 어댑터
    public class MyAppAdapter extends RecyclerView.Adapter<Cocktail.MyAppAdapter.CocktailViewHolder> {
        private List<Cocktail_data> values;
        public Context context;

        public class CocktailViewHolder extends RecyclerView.ViewHolder {

            TextView c_name;
            TextView c_key;
            TextView image_name;


            public View layout;

            public CocktailViewHolder(View v)
            {
                super(v);
                layout = v;
                this.c_name = itemView.findViewById(R.id.c_name);
                this.c_key = itemView.findViewById(R.id.c_key);
                this.image_name = itemView.findViewById(R.id.image_name);
            }
        }

        public MyAppAdapter(List<Cocktail_data> myDataset, Context context) {
            values = myDataset;
            this.context = context;
        }

        @Override
        public Cocktail.MyAppAdapter.CocktailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.activity_cocktail_item, parent, false);
            Cocktail.MyAppAdapter.CocktailViewHolder vh = new Cocktail.MyAppAdapter.CocktailViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull Cocktail.MyAppAdapter.CocktailViewHolder holder, final int position) {

            final Cocktail_data classListItems = arrayList.get(position);

            holder.c_name.setText(arrayList.get(position).get_c_name());
            holder.c_key.setText(arrayList.get(position).get_c_key());
            holder.image_name.setText(arrayList.get(position).get_image_name());

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), Cocktail_update.class);
                    intent.putExtra("c_day", classListItems.get_c_day());
                    intent.putExtra("c_name", classListItems.get_c_name());
                    intent.putExtra("image_name", classListItems.get_image_name());
                    intent.putExtra("c_content", classListItems.get_c_content());
                    intent.putExtra("c_key", classListItems.get_c_key());
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