package com.doqtqu.qrdodentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {
    String tmp = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader br=null;
                String key = "51554a506d646f7137306c684f6c55";
                String queryUrl="http://openapi.seoul.go.kr:8088/"+key+"/json/ListExhibitionOfSeoulMOAService/1/1000/KOR/";

                String dirPath = getFilesDir().getAbsolutePath();
                File file = new File(dirPath); // 일치하는 폴더가 없으면 생성
                if(!file.exists()){
                    file.mkdirs();
                    Log.d("Success","Success");
                }
                File savefile = new File(dirPath+"/test.txt");
                try{
                    URL url = new URL(queryUrl);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    if (con != null) {
                        con.setConnectTimeout(1000);
                        con.setUseCaches(false);
                        // 연결되었음 코드가 리턴되면.
                        if (HttpURLConnection.HTTP_OK == con.getResponseCode()) {
                            br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                            tmp = br.readLine();
                            FileOutputStream fos = new FileOutputStream(savefile);
                            fos.write(tmp.getBytes());
                            fos.close();
                            Log.d("tmp",dirPath);
                        }
                        con.disconnect();
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                } catch(Exception e){
                    e.printStackTrace();
                    Log.i("error","Error!");
                }
            }
        });
        thread.start();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}
