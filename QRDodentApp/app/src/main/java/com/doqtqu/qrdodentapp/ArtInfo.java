package com.doqtqu.qrdodentapp;

import java.io.Serializable;

public class ArtInfo  implements Serializable {
    String dp_name; // 전시회명
    String dp_subname; // 전시부제
    String dp_place; // 장소
    String dp_start; // 전시시작기간
    String dp_end; // 전시끝기간
    String dp_homepage; // 홈페이지
    String dp_sponsor; // 주최 및 후원
    String dp_viewtime; // 전시시간
    String dp_viewcharge; // 관람료
    String dp_art_part; // 전시부문
    String dp_art_cnt; // 전시작품수
    String dp_artist; // 출품작가
    String dp_docent; // 도슨트
    String dp_viewpoint; // 관람포인트
    String dp_master; // 담당자
    String dp_phone; // 문의전화
    String dp_info; // 전시설명
    String dp_main_image; // 대표이미지

    public ArtInfo(String dp_name, String dp_subname, String dp_place, String dp_start, String dp_end, String dp_homepage, String dp_sponsor, String dp_viewtime,
                   String dp_viewcharge, String dp_art_part, String dp_art_cnt, String dp_artist, String dp_docent, String
                           dp_viewpoint, String dp_master, String dp_phone, String dp_info, String dp_main_image) {
        this.dp_name = dp_name;
        this.dp_subname = dp_subname;
        this.dp_place = dp_place;
        this.dp_start = dp_start;
        this.dp_end = dp_end;
        this.dp_homepage = dp_homepage;
        this.dp_sponsor = dp_sponsor;
        this.dp_viewtime = dp_viewtime;
        this.dp_viewcharge = dp_viewcharge;
        this.dp_art_part = dp_art_part;
        this.dp_art_cnt = dp_art_cnt;
        this.dp_artist = dp_artist;
        this.dp_docent = dp_docent;
        this.dp_viewpoint = dp_viewpoint;
        this.dp_master = dp_master;
        this.dp_phone = dp_phone;
        this.dp_info = dp_info;
        this.dp_main_image = dp_main_image;
    }
}
