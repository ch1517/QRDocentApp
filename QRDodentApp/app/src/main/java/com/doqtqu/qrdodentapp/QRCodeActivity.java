package com.doqtqu.qrdodentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class QRCodeActivity extends AppCompatActivity {
    public static QRCodeActivity aRCodeActivity;
    private IntentIntegrator qrScan;
    private String dp_seq;
    private String parameta1;
    private String parameta2;
    private String imageURL;

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        aRCodeActivity = QRCodeActivity.this;
        context = this;
//        "316700,153,2364" // dp_seq,parameta1,parameta2

        qrScan = new IntentIntegrator(this);

        qrScan.setCaptureActivity(CaptureForm.class);
        qrScan.setOrientationLocked(false); // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
        qrScan.setPrompt("");
        qrScan.initiateScan();
    }


    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        //  com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE
        //  = 0x0000c0de; // Only use bottom 16 bits
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            String[] result_parsing;
            if (result == null) {
                // 취소됨
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                result_parsing = result.getContents().split(",");
                if(result_parsing.length==3){
                    dp_seq = result_parsing[0];
                    parameta1 = result_parsing[1];
                    parameta2 = result_parsing[2];
                    new DocentListTak().execute(this);
                } else {
                    Toast.makeText(this, "올바르지 않은 코드입니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private class DocentListTak extends AsyncTask<Context, Void, DocentInfo> {
        ProgressDialog asyncDialog = new ProgressDialog(QRCodeActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("잠시만 기다려주세요...");
            asyncDialog.show();
        }

        @Override
        protected DocentInfo doInBackground(Context... contexts) {
            DocentInfo docentInfo=null;
            try {
                    URL postUrl = new URL("https://sema.seoul.go.kr/ex/audio/getexaudoapiajax?glolangType=KOR&ex_id=" + parameta1 + "&pr_id=" + parameta2);
                    URLConnection connection = postUrl.openConnection();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuffer response = new StringBuffer();
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                docentInfo=parseJsonData(response.toString());

            } catch (IOException e) { //Jsoup의 connect 부분에서 IOException 오류가 날 수 있으므로 사용한다.
                Toast.makeText(context, "올바르지 않은 코드입니다.", Toast.LENGTH_LONG).show();
                finish();
            }
            return docentInfo;
        }
        DocentInfo parseJsonData(String str){
            DocentInfo result = null;
            try{
                JSONObject jsonObject = new JSONObject(str);
                JSONObject arr = jsonObject.getJSONArray("list").getJSONObject(0);
                JSONObject imgArr = arr.getJSONArray("imgArr").getJSONObject(0);

                result = new DocentInfo(arr.getString("pr_title"),arr.getString("pr_kor_sound"),arr.getString("pr_text"));
                imageURL = imgArr.getString("img_img");
                Log.d("imageURL",imageURL);
            }catch (Exception e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(DocentInfo d) {
            super.onPostExecute(d);
            Intent intent = new Intent(context, PopUpActivity.class);
            intent.putExtra("DocentInfo",d);
            intent.putExtra("imageName",imageURL);
            intent.putExtra("QRCodeActivity","O");
            startActivity(intent);
            overridePendingTransition(0, 0);
            asyncDialog.dismiss();
        }

    }

}
