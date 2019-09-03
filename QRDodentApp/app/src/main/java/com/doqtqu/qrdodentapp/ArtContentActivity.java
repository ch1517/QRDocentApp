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
import java.util.List;

// 뷰 크기 신경쓰기
public class ArtContentActivity extends AppCompatActivity {

    private ArtInfo artInfo = null;
    private ImageListAdapter mImageListAdapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_content);

        // Initializes list view adapter.
        recyclerView = findViewById(R.id.imageRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

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
        new ImageLinkTask().execute(this);
    }
    private class ImageLinkTask extends AsyncTask<Context,Void,ArrayList<String> > {
        Context mcontext;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<String> doInBackground(Context... contexts) {
            ArrayList<String> arr = new ArrayList<String>();
            mcontext = contexts[0];
            try {
                Log.d("artInfo.dp_seq", artInfo.dp_seq);
                Document doc = Jsoup.connect("https://sema.seoul.go.kr/ex/exDetail?exNo=" + artInfo.dp_seq + "&glolangType=KOR&searchDateType=CURR").get();
                Element con = doc.select(".swiper-wrapper").first();
                Elements contents = con.select("img");
                int count = 0;
                for (Element content : contents) {
                    String attr = content.attr("src");
                    arr.add("https://sema.seoul.go.kr" + attr);
                    count++;
                }
            } catch (IOException e) { //Jsoup의 connect 부분에서 IOException 오류가 날 수 있으므로 사용한다.
                e.printStackTrace();
            }
            return arr;
        }
        @Override
        protected void onPostExecute(ArrayList<String> s) {
            super.onPostExecute(s);
            mImageListAdapter = new ImageListAdapter(mcontext,s);
            recyclerView.setAdapter(mImageListAdapter);
        }
    }

    private class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageListHolder> {
        ArrayList<String> imageName = null;
        private Context mContext = null;

        public ImageListAdapter(Context mContext, ArrayList<String> arr){
            super();
            imageName = arr;
            this.mContext = mContext;
        }
        public class ImageListHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public ImageListHolder(@NonNull View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.list_image);
            }

            void onBind(String place_name) {
                Log.d("place_name",place_name);
                Glide.with(mContext).load(place_name).into(imageView);
            }
        }

        @NonNull
        @Override
        public ImageListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_imageview_list,parent,false);
            return new ImageListHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull ImageListHolder holder, int position) {
            holder.onBind(imageName.get(position));
//            holder.imageView
            Log.d("position",holder.getAdapterPosition()+"");
        }

        @Override
        public long getItemId(int i) {
            return i;
        }
        @Override
        public int getItemCount() {
            return imageName.size();
        }
    }
}

