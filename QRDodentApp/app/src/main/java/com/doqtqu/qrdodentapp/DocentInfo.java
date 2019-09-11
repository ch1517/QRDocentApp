package com.doqtqu.qrdodentapp;

import java.io.Serializable;

public class DocentInfo implements Serializable {
    private String pr_title;
    private String pr_kor_sound;
    private String pr_text;
    DocentInfo(String pr_title, String pr_kor_sound, String pr_text){
        this.pr_title = pr_title;
        this.pr_kor_sound = pr_kor_sound;
        this.pr_text = pr_text;
    }

    public String getPr_title() {
        return pr_title;
    }

    public String getPr_kor_sound() {
        return pr_kor_sound;
    }

    public String getPr_text() {
        return pr_text;
    }
}
