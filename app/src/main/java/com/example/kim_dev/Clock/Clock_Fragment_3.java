package com.example.kim_dev.Clock;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.kim_dev.R;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Clock_Fragment_3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Clock_Fragment_3 extends Fragment implements View.OnClickListener {

    private long timeCountMilliSeconds = 1 * 60000;

    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    private TimerStatus timerStatus = TimerStatus.STOPPED;
    private ProgressBar progressBarCircle;
    private EditText editTextMinute;
    private TextView textViewTime;
    private ImageView imageViewReset;
    private ImageView imageViewStartStop;
    private CountDownTimer countDownTimer;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Clock_Fragment_3() {
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
    public static Clock_Fragment_3 newInstance(String param1, String param2) {
        Clock_Fragment_3 fragment = new Clock_Fragment_3();
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

        View v = inflater.inflate(R.layout.activity_clock_f3, container, false);

        progressBarCircle = v.findViewById(R.id.progressBarCircle);
        editTextMinute = v.findViewById(R.id.editTextMinute);
        textViewTime = v.findViewById(R.id.textViewTime);
        imageViewReset = v.findViewById(R.id.imageViewReset);
        imageViewStartStop = v.findViewById(R.id.imageViewStartStop);

        // 리스너 초기화
        initListeners();

        return v;
    }

    /**
     * 리스너 초기화
     */
    private void initListeners() {
        imageViewReset.setOnClickListener(this);
        imageViewStartStop.setOnClickListener(this);
    }

    /**
     * 클릭 이벤트 설정
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewReset:
                reset();
                break;
            case R.id.imageViewStartStop:
                startStop();
                break;
        }
    }

    /**
     * 카운트 다운 시간을 리셋하고 재시작하는 기능
     */
    private void reset() {
        stopCountDownTimer();
        startCountDownTimer();
    }

    /**
     * 타이머가 시작하고 멈추는 기능
     */
    private void startStop() {
        if (timerStatus == TimerStatus.STOPPED) {
            setTimerValues();
            setProgressBarValues();

            imageViewReset.setVisibility(View.VISIBLE);
            imageViewStartStop.setImageResource(R.drawable.ic_baseline_stop_circle_24);
            editTextMinute.setEnabled(false);

            timerStatus = TimerStatus.STARTED;
            startCountDownTimer();
        } else {
            imageViewReset.setVisibility(View.GONE);
            imageViewStartStop.setImageResource(R.drawable.ic_baseline_play_circle_24);
            editTextMinute.setEnabled(true);

            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();
        }
    }

    /**
     * 타이머에 시간이 설정 되어있는지 체크
     * - 있는 경우 : 타이머에 시간 세팅
     * - 없는 경우 : 시간을 설정해달라는 안내 토스트 메세지
     */
    private void setTimerValues() {
        int time = 0;

        if (!editTextMinute.getText().toString().isEmpty()) {
            time = Integer.parseInt(editTextMinute.getText().toString().trim());
        } else {
            Toast.makeText(getActivity(), "시간을 설정해주세요.", Toast.LENGTH_LONG).show();
        }
        timeCountMilliSeconds = time * 60 * 1000;
    }

    /**
     * 카운트다운 시작 기능
     */
    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(timeCountMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));
                progressBarCircle.setProgress((int) (millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                textViewTime.setText(hmsTimeFormatter(timeCountMilliSeconds));
                setProgressBarValues();
                imageViewReset.setVisibility(View.GONE);
                imageViewStartStop.setImageResource(R.drawable.ic_baseline_play_circle_24);
                editTextMinute.setEnabled(true);
                timerStatus = TimerStatus.STOPPED;
            }
        }.start();
        countDownTimer.start();
    }

    /**
     * 카운트 다운 정지 및 초기화
     */
    private void stopCountDownTimer() {
        countDownTimer.cancel();
    }

    /**
     * 원형 프로그레스 바에 값 세팅
     */
    private void setProgressBarValues() {
        progressBarCircle.setMax((int) timeCountMilliSeconds/1000);
        progressBarCircle.setProgress((int) timeCountMilliSeconds/1000);
    }

    /**
     * 밀리언 초를 시간으로 포맷해주는 기능
     *
     * @param milliSeconds
     * @return HH:mm:ss 시간 포맷
     */
    private String hmsTimeFormatter(long milliSeconds) {

        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));
    }
}




















