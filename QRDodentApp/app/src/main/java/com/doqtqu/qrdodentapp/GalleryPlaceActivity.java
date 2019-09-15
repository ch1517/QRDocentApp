package com.doqtqu.qrdodentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;

public class GalleryPlaceActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HashMap<String,GalleryInfo> arrayList;
    private String placeNmae;
    private TextView addressTxt;
    private TextView placeTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_place);

        Button infoBtn = (Button)findViewById(R.id.infoBtn) ;
        infoBtn.setVisibility(View.GONE);
        Button docentBtn = (Button)findViewById(R.id.docentBtn) ;
        docentBtn.setVisibility(View.GONE);

        arrayList = new HashMap<>();
        arrayList.put("서소문본관",new GalleryInfo("서소문본관","서울특별시 중구 서소문동 덕수궁길 61", 37.564047, 126.973758));
        arrayList.put("북서울미술관",new GalleryInfo("북서울미술관","서울특별시 노원구 중계2.3동 동일로 1238", 37.640981, 127.066806));
        arrayList.put("남서울미술관",new GalleryInfo("남서울미술관","서울특별시 관악구 남현동 1059-13", 37.476226, 126.979424));
        arrayList.put("난지미술창작스튜디오",new GalleryInfo("난지미술창작스튜디오","서울특별시 마포구 상암동 하늘공원로 108-1", 37.564047, 126.973758));
        arrayList.put("SeMA창고",new GalleryInfo("SeMA창고","서울특별시 은평구 녹번동 산1-55",37.609430, 126.934259));
        arrayList.put("백남준기념관",new GalleryInfo("백남준기념관","서울특별시 종로구 창신동 종로53길 12-1", 37.573291, 127.013759));
        arrayList.put("SeMA벙커",new GalleryInfo("SeMA벙커","서울특별시 영등포구 여의도동 2-11", 37.525681, 126.924186));

        Intent intent = getIntent();
        addressTxt = (TextView)findViewById(R.id.address);
        placeTxt = (TextView)findViewById(R.id.placeName);
        placeNmae = intent.getStringExtra("placeName");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        GalleryInfo gi = arrayList.get(placeNmae);
        LatLng SEOUL = new LatLng(gi.xCode, gi.YCode);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title(gi.place_name);

        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

        placeTxt.setText(gi.place_name);
        addressTxt.setText("주소 : " + gi.address);
    }

    class GalleryInfo {
        private String place_name;
        private String address;
        private Double xCode;
        private Double YCode;
        public GalleryInfo(String place_name, String address, Double xCode, Double YCode) {
            this.place_name = place_name;
            this.address = address;
            this.xCode = xCode;
            this.YCode = YCode;
        }
    }
}