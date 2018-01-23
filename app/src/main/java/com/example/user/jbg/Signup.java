package com.example.user.jbg;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.ArrayList;

/**
 * Created by user on 2018-01-19.
 */

public class Signup extends AppCompatActivity {
    EditText et_id,et_password,et_name,et_phone_middle,et_phone_end;
    String id,pw,name,phone_number,major,phone_middle,phone_end;
    public static String phone_front="";
    public static String major1="";
    ArrayList arraylist,arraylist2;
    Button bt_complete,bt_idcheck;
    String phpresult = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        bt_complete = (Button)findViewById(R.id.bt_complete);
        bt_idcheck = (Button)findViewById(R.id.bt_idcheck);
        et_id = (EditText)findViewById(R.id.et_id);
        et_name = (EditText)findViewById(R.id.et_name);
        et_password = (EditText)findViewById(R.id.et_password);
        et_phone_middle = (EditText)findViewById(R.id.et_phone_middle);
        et_phone_end = (EditText)findViewById(R.id.et_phone_end);


        arraylist = new ArrayList();
        arraylist.add("010");
        arraylist.add("011");

        arraylist2 = new ArrayList();
        arraylist2.add("게임소프트웨어학과");
        arraylist2.add("유아교육과");
        arraylist2.add("정보보호학과");
        arraylist2.add("인쇄미디어학과");
        arraylist2.add("실용음악과");

        // 스피너 속성과 선택값 얻어오기
        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arraylist);
        final Spinner sp_phone_front = (Spinner)findViewById(R.id.sp_phone_front);
        sp_phone_front.setAdapter(adapter1);
        sp_phone_front.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                phone_front = String.valueOf(arraylist.get(arg2));
            }

            @Override
            public void onNothingSelected(AdapterView arg0) {
                // TODO Auto-generated method stub

            }
        });

        // 스피너 속성과 선택값 얻어오기
        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arraylist2);
        Spinner sp_major = (Spinner)findViewById(R.id.sp_major);
        sp_major.setAdapter(adapter2);
        sp_major.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                major1 = String.valueOf(arraylist2.get(arg2));
                Toast.makeText(Signup.this,"major1 : "+major1,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView arg0) {
                // TODO Auto-generated method stub

            }
        });

        bt_complete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                id = et_id.getText().toString();
                pw = et_password.getText().toString();
                name = et_name.getText().toString();
                phone_middle = et_phone_middle.getText().toString();
                phone_end = et_phone_end.getText().toString();

                phone_number = phone_front+phone_middle+phone_end;

                if(bt_idcheck.isClickable()==true)
                {
                    Toast.makeText(Signup.this,"아이디 중복확인해주세요",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(et_name.getText().toString().length()==0 || et_phone_middle.getText().toString().length()==0 || et_phone_end.getText().toString().length()==0 || et_id.getText().toString().length()==0 || et_password.getText().toString().length()==0)
                    {
                        Toast.makeText(Signup.this,"빈칸을 채워주세요",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        new signup().execute(id, pw, name, major1, phone_number);
                        Intent intent = new Intent(Signup.this, ND_Menu.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        bt_idcheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(et_id.getText().toString().length()==0){
                    Toast.makeText(Signup.this,"아이디를 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else {
                    id = et_id.getText().toString();
                    new idcheck().execute(id);
                }
            }
        });
    }

    private class idcheck extends AsyncTask<String, String, String> {

        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("http://graduation.dothome.co.kr/idcheck.php");
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
                        .appendQueryParameter("id", params[0]);

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

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(Signup.this,result,Toast.LENGTH_LONG).show();
            String idcheckresult = "";
            try {
                JSONArray jsonArray_idcheck = new JSONArray(result.toString());
                for (int a = 0; a < jsonArray_idcheck.length(); a++){
                    JSONObject jsonObject = jsonArray_idcheck.getJSONObject(a);

                    idcheckresult = jsonObject.getString("result");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (idcheckresult.contains("true")){
                Toast.makeText(Signup.this, "사용 가능한 아이디입니다.",Toast.LENGTH_LONG).show();
                bt_idcheck.setText("확인완료");
                bt_idcheck.setClickable(false);
                et_id.setFocusable(false);
            } else {
                Toast.makeText(Signup.this, "이미 사용중인 아이디입니다.",Toast.LENGTH_LONG).show();
            }
        }
    }


    private class signup extends AsyncTask<String, String, String> {

        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("http://graduation.dothome.co.kr/signup.php");
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
                        .appendQueryParameter("pw",params[1])
                        .appendQueryParameter("name",params[2])
                        .appendQueryParameter("major",params[3])
                        .appendQueryParameter("phone_number",params[4]);

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
            Toast.makeText(Signup.this,"result : "+result,Toast.LENGTH_LONG).show();
            try {
                JSONArray jsonarray = new JSONArray(result.toString());

                for (int a = 0; a < jsonarray.length(); a++){
                    JSONObject jsonObject = jsonarray.getJSONObject(a);

                    phpresult = jsonObject.getString("result");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (phpresult.contains("true")){
                Toast.makeText(Signup.this, "가입 성공",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(Signup.this, "가입에 실패했습니다.",Toast.LENGTH_LONG).show();
            }
        }
    }
}
