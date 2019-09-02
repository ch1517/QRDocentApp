package com.doqtqu.qrdodentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class ArtContentActivity extends AppCompatActivity {

    private ArtInfo artInfo = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_content);
        artInfo = (ArtInfo) getIntent().getSerializableExtra("artInfo");


        TextView titleText = (TextView) findViewById(R.id.titleText);
        titleText.setText(artInfo.dp_name);

        TextView artDate = (TextView) findViewById(R.id.dateText);
        artDate.setText(artInfo.dp_start + " ~ " + artInfo.dp_end);

        ImageView image_container = (ImageView) findViewById(R.id.contentImage);
        Glide.with(this).load(artInfo.dp_main_image).into(image_container);

//        TextView contentText = (TextView)findViewById(R.id.content);
//        contentText.setMovementMethod(ScrollingMovementMethod.getInstance());
//        contentText.setText((Html.fromHtml(artInfo.dp_info)));
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("artInfo.dp_seq", artInfo.dp_seq);
                    Document doc = Jsoup.connect("https://sema.seoul.go.kr/ex/exDetail?exNo=" + artInfo.dp_seq + "&glolangType=KOR&searchDateType=CURR").get();
                    Element con = doc.select(".swiper-wrapper").first();
                    Elements contents = con.select("img");
                    int count = 0;
                    for (Element content : contents) {
                        String attr = content.attr("src");
//                        arr.add("https://sema.seoul.go.kr" + attr);
                        Log.d("atrr", attr);
                        count++;
                    }
                } catch (IOException e) { //Jsoup의 connect 부분에서 IOException 오류가 날 수 있으므로 사용한다.
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


}
