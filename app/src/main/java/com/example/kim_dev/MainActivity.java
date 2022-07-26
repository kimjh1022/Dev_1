package com.example.kim_dev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kim_dev.Calculator.Calculator;
import com.example.kim_dev.Calendar.Calendar;
import com.example.kim_dev.Clock.Clock;
import com.example.kim_dev.Dev_Project.Dev_Project;
import com.example.kim_dev.Drawing_Board.Drawing_board;
import com.example.kim_dev.License.License;
import com.example.kim_dev.Login.Login;
import com.example.kim_dev.Lotto.Lotto;
import com.example.kim_dev.Map.Map;
import com.example.kim_dev.Memo.Memo;
import com.example.kim_dev.Profile.Profile;
import com.example.kim_dev.Translation.Translation;
import com.example.kim_dev.Weather.Weather;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private View drawerView;

    //ViewPager
    Activity activity;
    Context context;
    MainActivity_ViewPager_Adapter adapter;
    ViewPager viewPager;
    CircleIndicator indicator;

    TextView u_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // (회원) 상단 아이디
        u_id = (TextView) findViewById(R.id.u_id);
        SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);
        String inputText = sharedPreferences.getString("user_id","");
        u_id.setText(inputText);

        //ViewPager
        activity = new Activity();
        context =getApplicationContext();
        viewPager = findViewById(R.id.pager);
        adapter = new MainActivity_ViewPager_Adapter(context);
        viewPager.setAdapter(adapter);
        indicator = findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        // Navigation_Menu
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawer);

        // 로그아웃
        Button logout_btn = (Button) findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which){
                                signOut();
                                //finishAffinity();
                                Toast.makeText(MainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which){

                            }
                        })
                        .show();
            }
        });

        Button btn_open = (Button) findViewById(R.id.btn_open);
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        Button btn_close = (Button) findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
            }
        });

        // 개발 프로젝트 페이지
        Button license = (Button) findViewById(R.id.license);
        license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), License.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        // 개발 프로젝트 페이지
        Button dev_project = (Button) findViewById(R.id.dev_project);
        dev_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Dev_Project.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        // 시계 페이지
        Button clock_btn = (Button) findViewById(R.id.clock_btn);
        clock_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Clock.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        // 계산기 페이지
        Button calculator_btn = (Button) findViewById(R.id.calculator_btn);
        calculator_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Calculator.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        // 그림판 페이지
        Button drawing_btn = (Button) findViewById(R.id.drawing_btn);
        drawing_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Drawing_board.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        // 달력 페이지
        Button calendar_btn = (Button) findViewById(R.id.calendar_btn);
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Calendar.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        // 메모 페이지
        Button memo_btn = (Button) findViewById(R.id.memo_btn);
        memo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Memo.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        // 지도 페이지
        Button map_btn = (Button) findViewById(R.id.map_btn);
        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Map.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        // 날씨 페이지
        Button weather_btn = (Button) findViewById(R.id.weather_btn);
        weather_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Weather.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        // 번역기 페이지
        Button translation_btn = (Button) findViewById(R.id.translation_btn);
        translation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Translation.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        // 로또번호 조회 페이지
        Button lotto_btn = (Button) findViewById(R.id.lotto_btn);
        lotto_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Lotto.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {}

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {}

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {}

        @Override
        public void onDrawerStateChanged(int newState) {}
    };

    // 로그아웃
    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    // 첫 화면에서 뒤로가기 두번으로 앱 종료시키기 (onBackPressed)
    private long time = 0;
    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis() - time >= 2000) {
            time = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "한번더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
        else if(System.currentTimeMillis() - time < 2000) {
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }
    }


}




