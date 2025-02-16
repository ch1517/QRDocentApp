package com.doqtqu.qrdodentapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class MainActivity extends AppCompatActivity {
    HashMap<String, ArrayList<ArtInfo>> artInfoList;
    private MainListAdapter mServiceAdapter;
    private ListView lvNavList;
    private FrameLayout flContainer;
    private DrawerLayout dlDrawer;
    private Button menubtn;
    private ConstraintLayout drawLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        artInfoList = new HashMap<String, ArrayList<ArtInfo>>();
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
                    BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
                    String content = "", temp = "";
                    while ((temp = bufferReader.readLine()) != null) {
                        content += temp;
                    }
                    parseJson(content);
                } catch (Exception e) {
                }
            }

        Button home_menu = (Button)findViewById(R.id.home_menu);
        home_menu.setOnClickListener(sClickListener);
        // Initializes list view adapter.
        String[] navItems = {"서소문본관","북서울미술관", "남서울미술관","난지미술창작스튜디오","SeMA창고","백남준기념관","SeMA벙커"};
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        mServiceAdapter = new MainListAdapter(this, new ArrayList(artInfoList.keySet()));
        recyclerView.setAdapter(mServiceAdapter);

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
                    overridePendingTransition(0, 0);
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
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
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }
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
    }

    void parseJson(String str) {
        try {
            Log.d("strstr",str);
            JSONObject root = new JSONObject(str); // JSON 문서를 JSONObject 객체로 받는다.
            root = root.getJSONObject("ListExhibitionOfSeoulMOAService"); // result 구문에 있는 값을 get
            JSONArray ja = root.getJSONArray("row"); // result 구문에 있는 값을 get

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                ArtInfo arr = new ArtInfo(jo.getString("DP_SEQ"), jo.getString("DP_NAME"), jo.getString("DP_SUBNAME"), jo.getString("DP_PLACE"), jo.getString("DP_START"), jo.getString("DP_END"),
                        jo.getString("DP_HOMEPAGE"), jo.getString("DP_SPONSOR"), jo.getString("DP_VIEWTIME"), jo.getString("DP_VIEWCHARGE"), jo.getString("DP_ART_PART"),
                        jo.getString("DP_ART_CNT"), jo.getString("DP_ARTIST"), jo.getString("DP_DOCENT"), jo.getString("DP_VIEWPOINT"), jo.getString("DP_MASTER"),
                        jo.getString("DP_PHONE"), jo.getString("DP_INFO"), jo.getString("DP_MAIN_IMG"));

                SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                String mTime = mSimpleDateFormat.format(new Date());
                String date = mTime;
                if (date.compareTo(jo.getString("DP_START")) > 0) {
                    if (date.compareTo(jo.getString("DP_END")) < 0) {
                        // 현재
                        if (artInfoList.get(arr.dp_place) == null) {
                            artInfoList.put(arr.dp_place, new ArrayList<ArtInfo>());
                        }
                        ArrayList<ArtInfo> arrayList = artInfoList.get(arr.dp_place);
                        arrayList.add(arr);
                        artInfoList.put(arr.dp_place, arrayList);
                    }
                }
//                    else {
//                        // 과거
//                    }
//                } else {
//                    // 미래
//                }
            }
        } catch (JSONException e) {
            e.printStackTrace(); // 에러 메세지의 발생 근원지를 찾아서 단계별로 에러를 출력한다.
        }
    }

    private class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MainViewHolder> {
        ArrayList<String> arrayList;
        private Context mContext = null;

        public class MainViewHolder extends RecyclerView.ViewHolder {
            TextView placeName;
            AutoScrollViewPager autoScrollViewPager;

            public MainViewHolder(@NonNull View itemView) {
                super(itemView);
                placeName = (TextView) itemView.findViewById(R.id.textView);
            }

            void onBind(String place_name) {
                placeName.setText(place_name);
                autoScrollViewPager = (AutoScrollViewPager) itemView.findViewById(R.id.viewPager);
                ImageViewPagerAdapter scrollAdapter = new ImageViewPagerAdapter(mContext, artInfoList.get(place_name));
                autoScrollViewPager.setAdapter(scrollAdapter); //Auto Viewpager에 Adapter 장착
                autoScrollViewPager.setInterval(5000); // 페이지 넘어갈 시간 간격 설정
                autoScrollViewPager.startAutoScroll(); //Auto Scroll 시작
                autoScrollViewPager.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
            }
        }

        public MainListAdapter(Context mContext, ArrayList<String> arr) {
            super();
            arrayList = arr;
            this.mContext = mContext;
        }

        @NonNull
        @Override
        public MainListAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainlist_item, parent, false);
            MainListAdapter.MainViewHolder viewHolder = new MainListAdapter.MainViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MainListAdapter.MainViewHolder holder, int position) {
            holder.onBind(arrayList.get(position));
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
}