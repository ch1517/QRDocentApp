package com.doqtqu.qrdodentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonReader;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

// 뷰 크기 신경쓰기
public class ArtContentActivity extends AppCompatActivity {

    private ArtInfo artInfo = null;
    private ImageListAdapter mImageListAdapter;
    private DocentListAdapter mdocentListAdapter;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private Button infoBtn;
    private Button docentBtn;
    private ScrollView mainInfo;
    private ConstraintLayout displayInfo;
    private ConstraintLayout docentInfo;
    private int MODE_INFO = 0;
    private Context context;

    private ListView lvNavList;
    private FrameLayout flContainer;
    private DrawerLayout dlDrawer;
    private Button menubtn;
    private ConstraintLayout drawLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_content);

        context = getApplicationContext();

        infoBtn = (Button) findViewById(R.id.infoBtn);
        infoBtn.setOnClickListener(mClickListener);

        docentBtn = (Button) findViewById(R.id.docentBtn);
        docentBtn.setOnClickListener(mClickListener);

        mainInfo = (ScrollView) findViewById(R.id.mainInfo);
        displayInfo = (ConstraintLayout) findViewById(R.id.displayInfo);
        docentInfo = (ConstraintLayout) findViewById(R.id.docentInfo);

        recyclerView = findViewById(R.id.imageRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView2 = findViewById(R.id.imageRecyclerView2);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView2.setLayoutManager(linearLayoutManager2);

        artInfo = (ArtInfo) getIntent().getSerializableExtra("artInfo");

        TextView titleText = (TextView) findViewById(R.id.titleText);
        titleText.setText(artInfo.dp_name);

        TextView subTitle = (TextView) findViewById(R.id.subTitle);
        if (!artInfo.dp_subname.equals("") && artInfo.dp_subname != null) {
            subTitle.setText("부제 : " + artInfo.dp_subname);
        } else {
            subTitle.setVisibility(View.GONE);
        }

        TextView artDate = (TextView) findViewById(R.id.dateText);
        artDate.setText("전시일정 : " + artInfo.dp_start + " ~ " + artInfo.dp_end);

        TextView artistName = (TextView) findViewById(R.id.artistName);
        artistName.setText("출품작가 : " + artInfo.dp_artist);

        TextView placeName = (TextView) findViewById(R.id.placeName);
        placeName.setText("위치 : " + artInfo.dp_place);

        ImageView image_container = (ImageView) findViewById(R.id.contentImage);
        Glide.with(this).load(artInfo.dp_main_image).into(image_container);

        TextView contentText = (TextView) findViewById(R.id.content);
        contentText.setMovementMethod(ScrollingMovementMethod.getInstance());
        contentText.setText((Html.fromHtml(artInfo.dp_info)));

        new ImageLinkTask().execute(this);

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

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.infoBtn:
                    MODE_INFO = 1;
                    mainInfo.setVisibility(View.GONE);
                    displayInfo.setVisibility(View.VISIBLE);
                    docentInfo.setVisibility(View.GONE);
                    break;
                case R.id.docentBtn:
                    Log.d("docentInfo", "docentInfo");
                    MODE_INFO = 2;
                    mainInfo.setVisibility(View.GONE);
                    displayInfo.setVisibility(View.GONE);
                    docentInfo.setVisibility(View.VISIBLE);
                    new DocentListTak().execute(context);
            }
        }
    };

    private class ImageLinkTask extends AsyncTask<Context, Void, ArrayList<String>> {
        Context mcontext;
        int dcount = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<String> doInBackground(Context... contexts) {
            ArrayList<String> arr = new ArrayList<String>();
            try {
                Document doc = Jsoup.connect("https://sema.seoul.go.kr/ex/exDetail/exVoice?exNo=" + artInfo.dp_seq).get();
                Element con = doc.select(".slid_smPhoto.swiper-wrapper").first();
                Elements contents = con.select("img");
                for (Element content : contents) {
                    dcount++;
                }
            } catch (IOException e) { //Jsoup의 connect 부분에서 IOException 오류가 날 수 있으므로 사용한다.
                e.printStackTrace();
            }
            mcontext = contexts[0];
            try {
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
            if (dcount == 0) {
                docentBtn.setVisibility(View.GONE);
            }
            mImageListAdapter = new ImageListAdapter(mcontext, s);
            recyclerView.setAdapter(mImageListAdapter);
        }
    }

    private class DocentListTak extends AsyncTask<Context, Void, ArrayList<String>> {
        Context mcontext;
        int dcount = 0;
        ArrayList<DocentInfo> docentList;
        ProgressDialog asyncDialog = new ProgressDialog(ArtContentActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            docentList = new ArrayList<DocentInfo>();
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("잠시만 기다려주세요...");
            asyncDialog.show();
        }

        @Override
        protected ArrayList<String> doInBackground(Context... contexts) {
            ArrayList<String> arr = new ArrayList<String>();
            try {
                Document doc = Jsoup.connect("https://sema.seoul.go.kr/ex/exDetail/exVoice?exNo=" + artInfo.dp_seq).get();
                Log.d("artInfo.dp_seq",artInfo.dp_seq);
                Element con = doc.select(".slid_smPhoto.swiper-wrapper").first();
                Elements contents = con.select("img");

                Elements contents2 = con.select("a");
                int count = 0;
                String parameta1; // javascript:audioView(this, parameta1,parameta2) ;
                String parameta2;
                for (Element content : contents) {
                    String attr = content.attr("src");
                    String attr_href = contents2.get(count).attr("href");
                    parameta1 = attr_href.split("'")[1];
                    parameta2 = attr_href.split("'")[3];
                    Log.d("parameta", parameta1+" "+parameta2);
                    URL postUrl = new URL("https://sema.seoul.go.kr/ex/audio/getexaudoapiajax?glolangType=KOR&ex_id=" + parameta1 + "&pr_id=" + parameta2);
                    URLConnection connection = postUrl.openConnection();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuffer response = new StringBuffer();
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                    bufferedReader.close();
                    arr.add(attr);
                    docentList.add(parseJsonData(response.toString()));
                    count++;
                }
            } catch (IOException e) { //Jsoup의 connect 부분에서 IOException 오류가 날 수 있으므로 사용한다.
                e.printStackTrace();
            }
            return arr;
        }
        DocentInfo parseJsonData(String str){
            DocentInfo result = null;
            try{
                JSONObject jsonObject = new JSONObject(str);
                JSONObject arr = jsonObject.getJSONArray("list").getJSONObject(0);
                result = new DocentInfo(arr.getString("pr_title"),arr.getString("pr_kor_sound"),arr.getString("pr_text"));
            }catch (Exception e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            super.onPostExecute(s);
            mdocentListAdapter = new DocentListAdapter(mcontext, s, docentList);
            recyclerView2.setAdapter(mdocentListAdapter);
            asyncDialog.dismiss();
        }

    }

    private class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageListHolder> {
        ArrayList<String> imageName = null;
        private Context mContext = null;

        public ImageListAdapter(Context mContext, ArrayList<String> arr) {
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

            void onBind(final String imageName) {
                Log.d("place_name", imageName);
                Glide.with(mContext).load(imageName).into(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 이미지랑 텍스트 팝업으로 띄우기
                        Intent intent = new Intent(view.getContext(), PopUpImageActivity.class);
                        intent.putExtra("imageName",imageName);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                });
            }
        }

        @NonNull
        @Override
        public ImageListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_imageview_list, parent, false);
            return new ImageListHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull ImageListHolder holder, int position) {
            holder.onBind(imageName.get(position));

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

    private class DocentListAdapter extends RecyclerView.Adapter<DocentListAdapter.DocentListHolder> {
        ArrayList<String> imageName = null;
        ArrayList<DocentInfo> docentStr = null;
        private Context mContext = null;

        public DocentListAdapter(Context mContext, ArrayList<String> arr, ArrayList<DocentInfo> docentArr) {
            super();
            imageName = arr;
            docentStr = docentArr;
            this.mContext = mContext;
        }

        public class DocentListHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView textView;

            public DocentListHolder(@NonNull View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.docent_image);
                textView = (TextView) itemView.findViewById(R.id.scriptTxt);
            }

            void onBind(final String place_name, String str) {
                Log.d("place_name2", place_name);
//                Glide.with(mContext).load(place_name).into(imageView);
                Glide.with(context)
                        .load(place_name)
                        .skipMemoryCache(true)
                        .into(imageView);

                textView.setText(str);
            }
        }

        @NonNull
        @Override
        public DocentListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_docent_list, parent, false);
            return new DocentListHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull DocentListHolder holder, final int position) {
            holder.onBind(imageName.get(position), docentStr.get(position).getPr_title());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), PopUpActivity.class);
                    intent.putExtra("DocentInfo",docentStr.get(position));
                    intent.putExtra("imageName",imageName.get(position));
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
            return imageName.size();
        }
    }

}

