package com.example.kim_dev.Clock;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.kim_dev.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass
 * Use the {@link Clock_Fragment_1#newInstance} factory method to
 * create an instance of this fragment
 */

public class Clock_Fragment_1 extends Fragment {

    TextView Date, Gre;
    UsedAsync asyncTask;
    ProgressHandler handler;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    String time;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAMS1 = "param1";
    private static final String ARG_PARAMS2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Clock_Fragment_1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @Param param1 Parameter 1.
     * @Param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment_1.
     */

    // TODO: Rename and change types and number of parameters
    public static Clock_Fragment_1 newInstance(String param1, String param2) {
        Clock_Fragment_1 fragment = new Clock_Fragment_1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAMS1, param1);
        args.putString(ARG_PARAMS2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAMS1);
            mParam2 = getArguments().getString(ARG_PARAMS2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_clock_f1, container, false);

        Date = (TextView) v.findViewById(R.id.Date);
        Gre = (TextView) v.findViewById(R.id.Gre);

        handler = new ProgressHandler();

        runTime();

        return v;
    }

    public void runTime() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        time = sdf.format(new Date(System.currentTimeMillis()));

                        Message message = handler.obtainMessage();
                        handler.sendMessage(message);

                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {}
                }
            }
        });
        thread.start();

        asyncTask = new UsedAsync();
        asyncTask.execute();
    }

    class ProgressHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Date.setText(time);
        }
    }

    class UsedAsync extends AsyncTask<Integer, Integer, Integer> {
        Calendar cal;
        String timeGre;

        @Override
        protected Integer doInBackground(Integer...params) {
            while (isCancelled() == false) {
                cal = new GregorianCalendar();
                timeGre = String.format("%d/%d/%d %d:%d:%d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1,
                        cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));

                publishProgress();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {

                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            cal = new GregorianCalendar();
            timeGre = String.format("%d/%d/%d %d:%d:%d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1,
                    cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
            Gre.setText(timeGre);

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Gre.setText(timeGre);

            super.onProgressUpdate(values);
        }
    }
}































