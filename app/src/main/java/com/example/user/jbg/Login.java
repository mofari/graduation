package com.example.user.jbg;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class Login extends AppCompatActivity {

    String id, pw;
    Button bt_login, bt_signup;
    String phpresult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText et_log_id, et_log_pw;

        et_log_id = (EditText) findViewById(R.id.et_id);
        et_log_pw = (EditText) findViewById(R.id.et_password);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_signup = (Button)findViewById(R.id.bt_signup);

        bt_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){


                if(et_log_id.getText().toString().length()==0 || et_log_pw.getText().toString().length()==0)
                {
                    Toast.makeText(com.example.user.jbg.Login.this, "아이디와 비밀번호를 모두 입력하세요.", Toast.LENGTH_SHORT).show();
                }else{

                    id = et_log_id.getText().toString();
                    pw = et_log_pw.getText().toString();

                    new login().execute(id, pw);
                    Intent intent = new Intent(Login.this, ND_Menu.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


        bt_signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
                finish();
            }
        });

    }//END onCreate


    private class login extends AsyncTask<String, String, String> {

        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("http://graduation.dothome.co.kr/login.php");
            } catch (IOException e) {
                e.printStackTrace();
                return "오류";
            }
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("id", params[0])
                        .appendQueryParameter("pw",params[1]);

                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
            } catch (IOException e) {
                e.printStackTrace();
                return "오류";
            }
            try {
                int response_code = conn.getResponseCode();

                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    String result = new String();

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result += line + '\n';
                    }

                    return (result.toString());
                } else {
                    return "로그인 실패";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "오류";
            } finally {
                conn.disconnect();
            }
        }

        protected void onPostExecute(String result) {
            Toast.makeText(Login.this,"result : "+result,Toast.LENGTH_LONG).show();
            try {
                //JSONObject jsonObject = new JSONObject(result.toString());
                JSONObject obj = new JSONObject(result);

                phpresult = obj.getString("result");

                if (phpresult.contains("true")){
                    Toast.makeText(Login.this, "로그인 성공",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Login.this, "아이디 또는 비밀번호를 다시 입력해 주세요.",Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}