package com.doqtqu.qrdodentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
}