package com.doqtqu.qrdodentapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class GalleryPlaceActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HashMap<String,GalleryInfo> arrayList;
    private String placeNmae;
    private TextView addressTxt;
    private TextView placeTxt;
    private TextView trafficInfo;

    private ListView lvNavList;
    private FrameLayout flContainer;
    private DrawerLayout dlDrawer;
    private Button menubtn;
    private ConstraintLayout drawLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_place);

        Button infoBtn = (Button)findViewById(R.id.infoBtn) ;
        infoBtn.setVisibility(View.GONE);
        Button docentBtn = (Button)findViewById(R.id.docentBtn) ;
        docentBtn.setVisibility(View.GONE);

        arrayList = new HashMap<>();
        arrayList.put("서소문본관",new GalleryInfo("서소문본관","서울특별시 중구 서소문동 덕수궁길 61", 37.564047, 126.973758, "ORG01"));
        arrayList.put("북서울미술관",new GalleryInfo("북서울미술관","서울특별시 노원구 중계2.3동 동일로 1238", 37.640981, 127.066806,"ORG08"));
        arrayList.put("남서울미술관",new GalleryInfo("남서울미술관","서울특별시 관악구 남현동 1059-13", 37.476226, 126.979424,"ORG03"));
        arrayList.put("난지미술창작스튜디오",new GalleryInfo("난지미술창작스튜디오","서울특별시 마포구 상암동 하늘공원로 108-1", 37.569009, 126.878871,"ORG04"));
        arrayList.put("SeMA창고",new GalleryInfo("SeMA창고","서울특별시 은평구 녹번동 산1-55",37.609430, 126.934259,"ORG11"));
        arrayList.put("백남준기념관",new GalleryInfo("백남준기념관","서울특별시 종로구 창신동 종로53길 12-1", 37.573291, 127.013759,"ORG10"));
        arrayList.put("SeMA벙커",new GalleryInfo("SeMA벙커","서울특별시 영등포구 여의도동 2-11", 37.525681, 126.924186,"ORG12"));

        Intent intent = getIntent();
        addressTxt = (TextView)findViewById(R.id.address);
        placeTxt = (TextView)findViewById(R.id.placeName);
        trafficInfo = (TextView)findViewById(R.id.trafficInfo);
        placeNmae = intent.getStringExtra("placeName");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        new PlaceInfoTask().execute();

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
    public void onBackPressed() {
        if (dlDrawer.isDrawerOpen(drawLayout)) {
            dlDrawer.closeDrawer(drawLayout);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        GalleryInfo gi = arrayList.get(placeNmae);

        // 서울 여의도에 대한 위치 설정
        LatLng seoul = new LatLng(gi.xCode, gi.YCode);

        // 구글 맵에 표시할 마커에 대한 옵션 설정
        MarkerOptions makerOptions = new MarkerOptions();
        makerOptions
                .position(seoul)
                .title(gi.place_name);

        // 마커를 생성한다.
        mMap.addMarker(makerOptions);
        //카메라를 여의도 위치로 옮긴다.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul,17));

        placeTxt.setText(gi.place_name);
        addressTxt.setText("주소 : " + gi.address);

        mMap = googleMap;

    }
    private class PlaceInfoTask extends AsyncTask {

        Context mcontext;
        int dcount = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            String str="";
            try {
                Document doc = Jsoup.connect("https://sema.seoul.go.kr/it/getMap?museumCd=" + arrayList.get(placeNmae).extensionNum).get();
                Element con = doc.select(".traffic_info").first();
                Log.d("lengthlength",con.html());
                str = con.html();
//                for (Element content : contents) {
//                    Log.d("concon",content.toString());
//                }
            } catch (IOException e) { //Jsoup의 connect 부분에서 IOException 오류가 날 수 있으므로 사용한다.
                e.printStackTrace();
            }
            return str;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            trafficInfo.setText(Html.fromHtml((String)o));
        }
    }
    class GalleryInfo {
        private String place_name;
        private String address;
        private Double xCode;
        private Double YCode;
        private String extensionNum;
        public GalleryInfo(String place_name, String address, Double xCode, Double YCode, String extensionNum) {
            this.place_name = place_name;
            this.address = address;
            this.xCode = xCode;
            this.YCode = YCode;
            this.extensionNum = extensionNum;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }
}