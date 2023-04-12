package com.regain.attendie;

import android.widget.ImageView;
import android.widget.TextView;

public class Model_Profile {
    String text;
    int img_id;

    public int getImg_id() {
        return img_id;
    }

    public void setImg_id(int img_id) {
        this.img_id = img_id;
    }

    public Model_Profile() {
    }

    public Model_Profile(String text,int img_id) {
        this.text = text;
        this.img_id=img_id;

    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
