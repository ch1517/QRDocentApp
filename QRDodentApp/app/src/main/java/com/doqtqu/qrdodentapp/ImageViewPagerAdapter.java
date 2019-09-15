package com.doqtqu.qrdodentapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ImageViewPagerAdapter extends PagerAdapter {
    private Context context;
    ArrayList<ArtInfo> arrayList;
    ImageViewPagerAdapter(Context context, ArrayList<ArtInfo> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        //뷰페이지 슬라이딩 할 레이아웃 인플레이션
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.auto_viewpager,null);
        ImageView image_container = (ImageView) v.findViewById(R.id.image_container);
        Glide.with(context).load(arrayList.get(position).dp_main_image).into(image_container);
        TextView artTitle = (TextView)v.findViewById(R.id.artTitle);
        artTitle.setText(arrayList.get(position).dp_name);
        TextView artDate = (TextView)v.findViewById(R.id.artDate);
        artDate.setText(arrayList.get(position).dp_start+" ~ "+arrayList.get(position).dp_end);
        container.addView(v);
        v.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //this will log the page number that was click
                Intent intent = new Intent(context, ArtContentActivity.class);
                intent.putExtra("artInfo",arrayList.get(position));
                context.startActivity(intent);
            }
        });
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
