package com.doqtqu.qrdodentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ArtContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_content);
        ArtInfo artInfo = (ArtInfo)getIntent().getSerializableExtra("artInfo");

        TextView titleText = (TextView)findViewById(R.id.titleText);
        titleText.setText(artInfo.dp_name);

        TextView artDate = (TextView)findViewById(R.id.dateText);
        artDate.setText(artInfo.dp_start+" ~ "+artInfo.dp_end);

        ImageView image_container = (ImageView) findViewById(R.id.contentImage);
        Glide.with(this).load(artInfo.dp_main_image).into(image_container);

        TextView contentText = (TextView)findViewById(R.id.content);
        Log.d("contentText",artInfo.dp_main_image);
        contentText.setMovementMethod(ScrollingMovementMethod.getInstance());
        contentText.setText((Html.fromHtml(artInfo.dp_info)));
    }
}
