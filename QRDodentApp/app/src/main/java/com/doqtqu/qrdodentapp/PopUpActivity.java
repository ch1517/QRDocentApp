package com.doqtqu.qrdodentapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class PopUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pop_up);


        Intent intent = getIntent();
        DocentInfo docentInfo = (DocentInfo)intent.getSerializableExtra("DocentInfo");
        String imageName = intent.getStringExtra("imageName");

        final WebView webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true); // 자바스크립트 허용
        webView.loadUrl(docentInfo.getPr_kor_sound());

        ImageView imageView = (ImageView)findViewById(R.id.popupImg);
        Glide.with(this)
                .load(imageName)
                .skipMemoryCache(true)
                .into(imageView);

        TextView textView = (TextView)findViewById(R.id.scriptTxt);
        textView.setText(docentInfo.getPr_text());
        textView.setMovementMethod(new ScrollingMovementMethod());
        Log.d("docentMP3",docentInfo.getPr_kor_sound());

        Button exitBtn = (Button)findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                webView.destroy();
            }
        });

    }
}
