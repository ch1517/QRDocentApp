package com.doqtqu.qrdodentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class PopUpImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pop_up_image);
        String imageName = getIntent().getStringExtra("imageName");

        ImageView imageView = (ImageView)findViewById(R.id.popupImg);
        Glide.with(this)
                .load(imageName)
                .skipMemoryCache(true)
                .into(imageView);

        Button exitBtn = (Button)findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
