package com.example.kim_dev.Translation;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kim_dev.MainActivity;
import com.example.kim_dev.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Translation extends AppCompatActivity {

    TextView e_k_result, e_k;
    EditText e_k_source;
    Button k_e_change, e_k_translate;

    TextView k_e_result, k_e;
    EditText k_e_source;
    Button e_k_change, k_e_translate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        e_k = (TextView) findViewById(R.id.e_k);
        e_k_source = (EditText) findViewById(R.id.e_k_source);
        e_k_result = (TextView) findViewById(R.id.e_k_result);
        e_k_translate = (Button) findViewById(R.id.e_k_translate);
        k_e_change = (Button) findViewById(R.id.k_e_change);

        k_e = (TextView) findViewById(R.id.k_e);
        k_e_source = (EditText) findViewById(R.id.k_e_source);
        k_e_result = (TextView) findViewById(R.id.k_e_result);
        k_e_translate = (Button) findViewById(R.id.k_e_translate);
        e_k_change = (Button) findViewById(R.id.e_k_change);

        e_k.setVisibility(View.INVISIBLE);
        e_k_source.setVisibility(View.INVISIBLE);
        e_k_result.setVisibility(View.INVISIBLE);
        e_k_translate.setVisibility(View.INVISIBLE);
        e_k_change.setVisibility(View.INVISIBLE);

        e_k_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                e_k.setVisibility(View.INVISIBLE);
                e_k_source.setVisibility(View.INVISIBLE);
                e_k_result.setVisibility(View.INVISIBLE);
                e_k_translate.setVisibility(View.INVISIBLE);
                e_k_change.setVisibility(View.INVISIBLE);

                k_e.setVisibility(View.VISIBLE);
                k_e_source.setVisibility(View.VISIBLE);
                k_e_result.setVisibility(View.VISIBLE);
                k_e_translate.setVisibility(View.VISIBLE);
                k_e_change.setVisibility(View.VISIBLE);

                k_e_source.setText("");
                k_e_result.setText("");
            }
        });

        k_e_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                k_e.setVisibility(View.INVISIBLE);
                k_e_source.setVisibility(View.INVISIBLE);
                k_e_result.setVisibility(View.INVISIBLE);
                k_e_change.setVisibility(View.INVISIBLE);
                k_e_translate.setVisibility(View.INVISIBLE);

                e_k.setVisibility(View.VISIBLE);
                e_k_source.setVisibility(View.VISIBLE);
                e_k_result.setVisibility(View.VISIBLE);
                e_k_change.setVisibility(View.VISIBLE);
                e_k_translate.setVisibility(View.VISIBLE);

                e_k_source.setText("");
                e_k_result.setText("");
            }
        });

        //번역 실행 (한영)
        k_e_translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(k_e_source.getText().toString().length() == 0) {
                    Toast.makeText(Translation.this, "번역할 내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                    k_e_source.requestFocus();
                    return;
                }
                NaverTranslateTask1 asyncTask = new NaverTranslateTask1();
                String sText = k_e_source.getText().toString();
                asyncTask.execute(sText);
            }
        });

        //번역 실행 (영한)
        e_k_translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(e_k_source.getText().toString().length() == 0) {
                    Toast.makeText(Translation.this, "번역할 내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                    e_k_source.requestFocus();
                    return;
                }
                NaverTranslateTask2 asyncTask = new NaverTranslateTask2();
                String sText = e_k_source.getText().toString();
                asyncTask.execute(sText);
            }
        });
    }



    //ASYNCTASK (한영)
    public class NaverTranslateTask1 extends AsyncTask<String, Void, String> {

        public String resultText;
        //Naver
        String clientId = "NcM3lzUWbztXgQM1Uz4a";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "7cflibsFye";//애플리케이션 클라이언트 시크릿값";
        //언어선택도 나중에 사용자가 선택할 수 있게 옵션 처리해 주면 된다.
        String sourceLang = "ko";
        String targetLang = "en";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //AsyncTask 메인처리
        @Override
        protected String doInBackground(String... strings) {
            //네이버제공 예제 복사해 넣자.
            //Log.d("AsyncTask:", "1.Background");

            String sourceText = strings[0];

            try {
                //String text = URLEncoder.encode("만나서 반갑습니다.", "UTF-8");
                String text = URLEncoder.encode(sourceText, "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                // post request
                String postParams = "source="+sourceLang+"&target="+targetLang+"&text=" + text;
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(postParams);
                wr.flush();
                wr.close();
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if(responseCode==200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                //System.out.println(response.toString());
                return response.toString();

            } catch (Exception e) {
                //System.out.println(e);
                Log.d("error", e.getMessage());
                return null;
            }
        }

        //번역된 결과를 받아서 처리 (영한)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //최종 결과 처리부
            //Log.d("background result", s.toString()); //네이버에 보내주는 응답결과가 JSON 데이터이다.

            //JSON데이터를 자바객체로 변환해야 한다.
            //Gson을 사용할 것이다.

            Gson gson = new GsonBuilder().create();
            JsonParser parser = new JsonParser();
            JsonElement rootObj = parser.parse(s.toString())
                    //원하는 데이터 까지 찾아 들어간다.
                    .getAsJsonObject().get("message")
                    .getAsJsonObject().get("result");
            //안드로이드 객체에 담기
            TranslatedItem items = gson.fromJson(rootObj.toString(), TranslatedItem.class);
            //Log.d("result", items.getTranslatedText());
            //번역결과를 텍스트뷰에 넣는다.
            k_e_result.setText(items.getTranslatedText());
        }

        //자바용 그릇
        private class TranslatedItem {
            String translatedText;

            public String getTranslatedText() {
                return translatedText;
            }
        }
    }





    //ASYNCTASK (영한)
    public class NaverTranslateTask2 extends AsyncTask<String, Void, String> {

        public String resultText;
        //Naver
        String clientId = "NcM3lzUWbztXgQM1Uz4a";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "7cflibsFye";//애플리케이션 클라이언트 시크릿값";
        //언어선택도 나중에 사용자가 선택할 수 있게 옵션 처리해 주면 된다.
        String sourceLang = "en";
        String targetLang = "ko";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //AsyncTask 메인처리
        @Override
        protected String doInBackground(String... strings) {
            //네이버제공 예제 복사해 넣자.
            //Log.d("AsyncTask:", "1.Background");

            String sourceText = strings[0];

            try {
                //String text = URLEncoder.encode("만나서 반갑습니다.", "UTF-8");
                String text = URLEncoder.encode(sourceText, "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                // post request
                String postParams = "source="+sourceLang+"&target="+targetLang+"&text=" + text;
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(postParams);
                wr.flush();
                wr.close();
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if(responseCode==200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                //System.out.println(response.toString());
                return response.toString();

            } catch (Exception e) {
                //System.out.println(e);
                Log.d("error", e.getMessage());
                return null;
            }
        }

        //번역된 결과를 받아서 처리 (영한)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //최종 결과 처리부
            //Log.d("background result", s.toString()); //네이버에 보내주는 응답결과가 JSON 데이터이다.

            //JSON데이터를 자바객체로 변환해야 한다.
            //Gson을 사용할 것이다.

            Gson gson = new GsonBuilder().create();
            JsonParser parser = new JsonParser();
            JsonElement rootObj = parser.parse(s.toString())
                    //원하는 데이터 까지 찾아 들어간다.
                    .getAsJsonObject().get("message")
                    .getAsJsonObject().get("result");
            //안드로이드 객체에 담기
            TranslatedItem items = gson.fromJson(rootObj.toString(), TranslatedItem.class);
            //Log.d("result", items.getTranslatedText());
            //번역결과를 텍스트뷰에 넣는다.
            e_k_result.setText(items.getTranslatedText());
        }

        //자바용 그릇
        private class TranslatedItem {
            String translatedText;

            public String getTranslatedText() {
                return translatedText;
            }
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

























