package com.doqtqu.qrdodentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class ArtGalleryContentActivity extends AppCompatActivity {

    ArrayList<ArtInfo> corrnet_artInfoList;
    ArrayList<ArtInfo> future_artInfoList;
    private GalleryListAdapter1 mServiceAdapter1;
    private GalleryListAdapter2 mServiceAdapter2;
    private Button placeBtn;
    private String placeName;

    private ListView lvNavList;
    private FrameLayout flContainer;
    private DrawerLayout dlDrawer;
    private Button menubtn;
    private ConstraintLayout drawLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_gallery_content);
        placeName = getIntent().getStringExtra("ArtGallery");
        placeBtn = (Button)findViewById(R.id.placeBtn);

        corrnet_artInfoList = new ArrayList<ArtInfo>();
        future_artInfoList = new ArrayList<ArtInfo>();

        String dirPath = getFilesDir().getAbsolutePath();
        File file = new File(dirPath); // 일치하는 폴더가 없으면 생성

        // 파일이 1개 이상이면 파일 이름 출력
        if (file.listFiles().length > 0)
            for (File f : file.listFiles()) {
                String str = f.getName();
                Log.v(null, "fileName : " + str); // 파일 내용 읽어오기
                String loadPath = dirPath + "/" + str;
                try {
                    FileInputStream fis = new FileInputStream(loadPath);
                    BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fis));
                    String content = "", temp = "";
                    while ((temp = bufferReader.readLine()) != null) {
                        content += temp;
                    }
                    parseJson(content,placeName);
                    bufferReader.close();
                } catch (Exception e) {
                }
            }
        if(corrnet_artInfoList.size()!=0){
            TextView textView = (TextView)findViewById(R.id.corrent);
            textView.setText("현재전시");
        } else {
            TextView textView = (TextView)findViewById(R.id.corrent);
            textView.setTextColor(Color.GRAY);
            textView.setText("현재 전시 중인 작품이 없습니다.");
        }
        if(future_artInfoList.size()!=0){
            TextView textView = (TextView)findViewById(R.id.future);
            textView.setText("전시예정");
        } else {
            TextView textView = (TextView)findViewById(R.id.future);
            textView.setTextColor(Color.GRAY);
            textView.setText("전시 예정인 작품이 없습니다.");
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView recyclerView2 = findViewById(R.id.recyclerView2);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView2.setLayoutManager(linearLayoutManager2);

        mServiceAdapter1 = new GalleryListAdapter1(this);
        recyclerView.setAdapter(mServiceAdapter1);

        mServiceAdapter2 = new GalleryListAdapter2(this);
        recyclerView2.setAdapter(mServiceAdapter2);

        placeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ArtGalleryContentActivity.this, GalleryPlaceActivity.class);
                intent.putExtra("placeName", placeName);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        Button home_menu = (Button)findViewById(R.id.home_menu);
        home_menu.setOnClickListener(sClickListener);
        lvNavList = (ListView) findViewById(R.id.lv_activity_main_nav_list);
        drawLayout = (ConstraintLayout) findViewById(R.id.drawLayout);

        flContainer = (FrameLayout) findViewById(R.id.fl_activity_main_container);
        menubtn = (Button) findViewById(R.id.menuBtn);
        menubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlDrawer.openDrawer(drawLayout);
            }
        });

        String[] navItems = {"서소문본관","북서울미술관", "남서울미술관","난지미술창작스튜디오","SeMA창고","백남준기념관","SeMA벙커"};
        dlDrawer = (DrawerLayout) findViewById(R.id.dl_activity_main_drawer);

        lvNavList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));
        lvNavList.setOnItemClickListener(new DrawerItemClickListener());

        Button qrcodeBtn = (Button)findViewById(R.id.qrcodeBtn);
        qrcodeBtn.setOnClickListener(sClickListener);
    }
    View.OnClickListener sClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.home_menu:
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                    break;
                case R.id.qrcodeBtn:
                    Intent intent2 = new Intent(v.getContext(), QRCodeActivity.class);
                    startActivity(intent2);
                    overridePendingTransition(0, 0);
                    break;
            }
        }
    };
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            Intent intent = new Intent(view.getContext(), ArtGalleryContentActivity.class);
            switch (position) {
                case 0:
                    intent.putExtra("ArtGallery","서소문본관");
                    break;
                case 1:
                    intent.putExtra("ArtGallery","북서울미술관");
                    break;
                case 2:
                    intent.putExtra("ArtGallery","남서울미술관");
                    break;
                case 3:
                    intent.putExtra("ArtGallery","난지미술창작스튜디오");
                    break;
                case 4:
                    intent.putExtra("ArtGallery","SeMA창고");
                    break;
                case 5:
                    intent.putExtra("ArtGallery","백남준기념관");
                    break;
                case 6:
                    intent.putExtra("ArtGallery","SeMA벙커");
                    break;
            }
            startActivity(intent);
            overridePendingTransition(0, 0);
            dlDrawer.closeDrawer(drawLayout);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }

    @Override
    public void onBackPressed() {
        if (dlDrawer.isDrawerOpen(drawLayout)) {
            dlDrawer.closeDrawer(drawLayout);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Button infoBtn = (Button) findViewById(R.id.infoBtn);
        infoBtn.setVisibility(View.GONE);
        Button docentBtn = (Button) findViewById(R.id.docentBtn);
        docentBtn.setVisibility(View.GONE);
        placeBtn.setVisibility(View.VISIBLE);
    }
    void parseJson(String str,String placeName) {
        try {
            JSONObject root = new JSONObject(str); // JSON 문서를 JSONObject 객체로 받는다.
            root = root.getJSONObject("ListExhibitionOfSeoulMOAService"); // result 구문에 있는 값을 get
            JSONArray ja = root.getJSONArray("row"); // result 구문에 있는 값을 get

            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
            String mTime = mSimpleDateFormat.format(new Date());
            String date = mTime;

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                ArtInfo arr = new ArtInfo(jo.getString("DP_SEQ"), jo.getString("DP_NAME"), jo.getString("DP_SUBNAME"), jo.getString("DP_PLACE"), jo.getString("DP_START"), jo.getString("DP_END"),
                        jo.getString("DP_HOMEPAGE"), jo.getString("DP_SPONSOR"), jo.getString("DP_VIEWTIME"), jo.getString("DP_VIEWCHARGE"), jo.getString("DP_ART_PART"),
                        jo.getString("DP_ART_CNT"), jo.getString("DP_ARTIST"), jo.getString("DP_DOCENT"), jo.getString("DP_VIEWPOINT"), jo.getString("DP_MASTER"),
                        jo.getString("DP_PHONE"), jo.getString("DP_INFO"), jo.getString("DP_MAIN_IMG"));

                if(placeName.equals(arr.dp_place)) {
                    if (date.compareTo(arr.dp_start) > 0 && date.compareTo(arr.dp_end) < 0) { // 현재
                        corrnet_artInfoList.add(arr);
                    }
                    if (date.compareTo(arr.dp_start) < 0){ // 미래
                        future_artInfoList.add(arr);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace(); // 에러 메세지의 발생 근원지를 찾아서 단계별로 에러를 출력한다.
        }
    }
    private class GalleryListAdapter1 extends RecyclerView.Adapter<GalleryListAdapter1.GalleryViewHolder> {
        private Context mContext = null;
        public class GalleryViewHolder extends RecyclerView.ViewHolder {
            private ImageView mImageVew = null;
            private TextView artTitle = null;
            private TextView artDate = null;
            public GalleryViewHolder(@NonNull View itemView) {
                super(itemView);
                mImageVew = (ImageView)itemView.findViewById(R.id.image_container);
                artTitle = (TextView) itemView.findViewById(R.id.artTitle);
                artDate = (TextView) itemView.findViewById(R.id.artDate);
            }

            void onBind(final ArtInfo artInfo) {
                artTitle.setText(artInfo.dp_name);
                artDate.setText(artInfo.dp_start+" ~ "+artInfo.dp_end);
                Glide.with(mContext)
                        .load(artInfo.dp_main_image)
                        .skipMemoryCache(true)
                        .into(mImageVew);
            }
        }

        public GalleryListAdapter1(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @NonNull
        @Override
        public GalleryListAdapter1.GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_viewpager, parent, false);
            GalleryListAdapter1.GalleryViewHolder viewHolder = new GalleryListAdapter1.GalleryViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull GalleryListAdapter1.GalleryViewHolder holder, final int position) {
            holder.onBind(corrnet_artInfoList.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ArtContentActivity.class);
                    intent.putExtra("artInfo",corrnet_artInfoList.get(position));
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            });
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getItemCount() {
            return corrnet_artInfoList.size();
        }
    }

    private class GalleryListAdapter2 extends RecyclerView.Adapter<GalleryListAdapter2.GalleryViewHolder> {
        private Context mContext = null;
        public class GalleryViewHolder extends RecyclerView.ViewHolder {
            private ImageView mImageVew = null;
            private TextView artTitle = null;
            private TextView artDate = null;
            public GalleryViewHolder(@NonNull View itemView) {
                super(itemView);
                mImageVew = (ImageView)itemView.findViewById(R.id.image_container);
                artTitle = (TextView) itemView.findViewById(R.id.artTitle);
                artDate = (TextView) itemView.findViewById(R.id.artDate);
            }

            void onBind(final ArtInfo artInfo) {
                artTitle.setText(artInfo.dp_name);
                artDate.setText(artInfo.dp_start+" ~ "+artInfo.dp_end);
                Glide.with(mContext)
                        .load(artInfo.dp_main_image)
                        .skipMemoryCache(true)
                        .into(mImageVew);
            }
        }

        public GalleryListAdapter2(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @NonNull
        @Override
        public GalleryListAdapter2.GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_viewpager, parent, false);
            GalleryListAdapter2.GalleryViewHolder viewHolder = new GalleryListAdapter2.GalleryViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull GalleryListAdapter2.GalleryViewHolder holder, final int position) {
            holder.onBind(future_artInfoList.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ArtContentActivity.class);
                    intent.putExtra("artInfo",future_artInfoList.get(position));
                    startActivity(intent);
                }
            });
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getItemCount() {
            return future_artInfoList.size();
        }
    }

}
