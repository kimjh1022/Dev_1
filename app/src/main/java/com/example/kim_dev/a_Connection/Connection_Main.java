package com.example.kim_dev.a_Connection;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kim_dev.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Connection_Main extends AppCompatActivity {

    // Connection
    Connection con = null;
    Statement stmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        new SQL_1().execute("");
        SQL_2();
    }

    // insert, update, delete, ...
    public class SQL_1 extends AsyncTask<String, String , String> {
        String z = "";
        Boolean isSuccess = false;
        @Override
        protected void onPreExecute() { }
        @Override
        protected void onPostExecute(String s) { }
        @Override
        protected String doInBackground(String... strings) {
            try{
                con = connectionClass(ConnectionClass.un.toString(),ConnectionClass.pass.toString(),ConnectionClass.db.toString(),ConnectionClass.ip.toString());
                if(con == null){
                    z = "인터넷 연결을 확인해주세요.";
                }
                else{
                    stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    String sql = "";
                    stmt.executeUpdate(sql);
                }
            }catch (Exception e){
                isSuccess = false;
                z = e.getMessage();
            }
            return z;
        }
    }

    // select, ...
    public void SQL_2() {
        con = connectionClass(ConnectionClass.un.toString(),ConnectionClass.pass.toString(),ConnectionClass.db.toString(),ConnectionClass.ip.toString());

        if(con != null){
            Statement statement = null;
            try
            {
                Statement stmt = con.createStatement();

                ResultSet resultSet = stmt.executeQuery("");
                if(resultSet.next()){
                    //A.setText(resultSet.getString(1));
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        } else {

        }
    }

    // DB 연동
    @SuppressLint("NewApi")
    public Connection connectionClass(String user, String password, String database, String server){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String connectionURL = null;
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectionURL = "jdbc:jtds:sqlserver://" + server+"/" + database + ";user=" + user + ";password=" + password + ";";
            connection = DriverManager.getConnection(connectionURL);
        }catch (Exception e){
            Log.e("SQL Connection Error : ", e.getMessage());
        }
        return connection;
    }
}

