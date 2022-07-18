package com.example.kim_dev.Clock;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.kim_dev.R;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Clock_Fragment_2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Clock_Fragment_2 extends Fragment {

    TextView textView, recordView;
    Button start, pause, reset, record;

    ScrollView scroll;

    long MillisecondTime = 0L;  // 스탑워치 시작 버튼을 누르고 흐른 시간
    long StartTime = 0L;        // 스탑워치 시작 버튼 누르고 난 이후 부터의 시간
    long TimeBuff = 0L;         // 스탑워치 일시정지 버튼 눌렀을 때의 총 시간
    long UpdateTime = 0L;       // 스탑워치 일시정지 버튼 눌렀을 때의 총 시간 + 시작 버튼 누르고 난 이후 부터의 시간 = 총 시간

    Handler handler;

    int Seconds, Minutes, MilliSeconds;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Clock_Fragment_2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment_1.
     */
    // TODO: Rename and change types and number of parameters
    public static Clock_Fragment_2 newInstance(String param1, String param2) {
        Clock_Fragment_2 fragment = new Clock_Fragment_2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_clock_f2, container, false);

        textView = (TextView) v.findViewById(R.id.textView);
        recordView = (TextView) v.findViewById(R.id.recordView);
        start = (Button) v.findViewById(R.id.start);
        pause = (Button) v.findViewById(R.id.pause);
        reset = (Button) v.findViewById(R.id.reset);
        record = (Button) v.findViewById(R.id.record);
        scroll = (ScrollView) v.findViewById(R.id.scroll);

        handler = new Handler();

        pause.setEnabled(false);

        // 스탑워치 시작 버튼
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // SystemClock.uptimeMillis()는 디바이스를 부팅한후 부터 쉰 시간을 제외한 밀치초를 반환
                StartTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);

                // 시작 버튼 클릭 시 리셋 버튼 비활성화
                pause.setEnabled(true);
                reset.setEnabled(false);
                start.setEnabled(false);
            }
        });

        // 스탑워치 일시정지 버튼
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 스탑워치 일시정지 버튼 눌렀을 때의 총 시간
                TimeBuff += MillisecondTime;

                // Runnable 객체 제거
                handler.removeCallbacks(runnable);

                // 일시정지 버튼 클릭 시 리셋 버튼을 활성화
                start.setEnabled(true);
                reset.setEnabled(true);
            }
        });

        // 스탑워치 리셋 버튼
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 측정 시간을 모두 0으로 리셋시켜준다.
                MillisecondTime = 0L;
                StartTime = 0L;
                TimeBuff = 0;
                UpdateTime = 0L;
                Seconds = 0;
                Minutes = 0;
                MilliSeconds = 0;

                // 초를 나타내는 TextView 를 0초로 갱신
                textView.setText("00:00:00");
                recordView.setText("");

                pause.setEnabled(false);
            }
        });

        // 스탑워치 기록 버튼
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordView.setText(recordView.getText() + textView.getText().toString() + "\n");

                // 스크롤 꽉차면 아래로 스크롤
                scroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        return v;
    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // 디바이스를 부팅한 후 부터 현재까지 시간 - 시작 버튼을 누른 시간
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            // 스탑워치 일시정지 버튼 눌렀을 때의 총 시간 + 시작 버튼을 누르고 난 이후 부터의 시간 = 총 시간
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;
            MilliSeconds = (int) (UpdateTime % 1000);

            // TextView 에 UpdateTime 을 갱신
            textView.setText("" + Minutes + ":" + String.format("%02d", Seconds) + ":" + String.format("%03d", MilliSeconds));
            handler.postDelayed(this, 0);
        }
    };
}















