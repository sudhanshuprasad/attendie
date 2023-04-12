package com.regain.attendie;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class CookieJar implements okhttp3.CookieJar {
    List<Cookie> cookieStore;
static  int l=0;
   CookieJar() {
        this.cookieStore = new ArrayList<>();
    }


    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        return this.cookieStore;
    }

    @Override
    public void saveFromResponse( HttpUrl httpUrl, List<Cookie> list) {
cookieStore.addAll(list);
l=cookieStore.size();
    }

}
