package com.doqtqu.pafa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    static ArrayList<ArtInfo> arrayList = null;
    private String tmp = "";
    private String date = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button)findViewById(R.id.nextpageBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
        arrayList = new ArrayList<>();
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy-MM-dd", Locale.KOREA );
        String mTime = mSimpleDateFormat.format(new Date());
        date = mTime;

        String key = "51554a506d646f7137306c684f6c55";
        String queryUrl="http://openapi.seoul.go.kr:8088/"+key+"/json/ListExhibitionOfSeoulMOAService/1/1000/KOR/";
        new BackgroundTask().execute(queryUrl);

    }
    @Override
    protected void onResume() {
        super.onResume();

    }
    class BackgroundTask extends AsyncTask<String, Void, String> {
        private ProgressDialog mDlg = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            mDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mDlg.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            final StringBuilder builder = new StringBuilder();
            BufferedReader br=null;
            try{
                URL url = new URL(urls[0]);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                if (con != null) {
                    con.setConnectTimeout(1000);
                    con.setUseCaches(false);
                    // 연결되었음 코드가 리턴되면.
                    if (HttpURLConnection.HTTP_OK == con.getResponseCode()) {
                        br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                        tmp = br.readLine();
                        Log.d("tmp",tmp);
                    }
                    con.disconnect();
                }
                return tmp;
            } catch(Exception e){
                e.printStackTrace();
                Log.i("error","Error!");
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            mDlg.dismiss();
            Log.d("result",result);
            try {
                JSONObject root = new JSONObject(result); // JSON 문서를 JSONObject 객체로 받는다.
                root = root.getJSONObject("ListExhibitionOfSeoulMOAService"); // result 구문에 있는 값을 get
                int count = root.getInt("list_total_count");
                JSONArray ja = root.getJSONArray("row"); // result 구문에 있는 값을 get
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    ArtInfo arr = new ArtInfo(jo.getString("DP_NAME"),jo.getString("DP_SUBNAME"),jo.getString("DP_PLACE"),jo.getString("DP_START"),jo.getString("DP_END"),
                            jo.getString("DP_HOMEPAGE"),jo.getString("DP_SPONSOR"),jo.getString("DP_VIEWTIME"),jo.getString("DP_VIEWCHARGE"),jo.getString("DP_ART_PART"),
                            jo.getString("DP_ART_CNT"),jo.getString("DP_ARTIST"),jo.getString("DP_DOCENT"),jo.getString("DP_VIEWPOINT"),jo.getString("DP_MASTER"),
                            jo.getString("DP_PHONE"),jo.getString("DP_INFO"),jo.getString("DP_MAIN_IMG"));
                    if(date.compareTo(jo.getString("DP_START"))>0){
                        if(date.compareTo(jo.getString("DP_END"))<0){
                            // 현재
                        } else {
                            // 과거
                        }
                    } else {
                        // 미래
                    }
                    arrayList.add(arr);
                }
            } catch (JSONException e) {
                e.printStackTrace(); // 에러 메세지의 발생 근원지를 찾아서 단계별로 에러를 출력한다.
            }

        }

    }
}
