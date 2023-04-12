package com.regain.attendie;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class BrowseActivity extends AppCompatActivity {
WebView webView;
ProgressBar progressBar;
AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        webView=findViewById(R.id.webView);
        adView=findViewById(R.id.adView);
        progressBar=findViewById(R.id.progressBar);
        String url=getIntent().getStringExtra("url");
        webView.setWebViewClient(new myWebViewClient());
        webView.loadUrl(url);
        progressBar.setVisibility(View.VISIBLE);


        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

//                progressBar.setProgress(newProgress);

            }


            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                getSupportActionBar().setTitle(title);

            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
            }

        });
        loadAds();
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //webview Download
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
               try {
                   final String filename = URLUtil.guessFileName(url, contentDisposition, mimetype);
                   DownloadManager.Request myRequest = new DownloadManager.Request(Uri.parse(url));
                   myRequest.setMimeType(mimetype);
                   String cookies = CookieManager.getInstance().getCookie(url);
                   myRequest.addRequestHeader("cookie", cookies);
                   myRequest.addRequestHeader("User-Agent", userAgent);
                   myRequest.setDescription("Downlaoding file....");
                   myRequest.allowScanningByMediaScanner();
                   myRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                   DownloadManager myManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                   myRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                   myManager.enqueue(myRequest);
                   Toast.makeText(BrowseActivity.this, "Downloading.....check notification", Toast.LENGTH_SHORT).show();
               }catch (Exception e){

               }
               }
        });
    }

    private void loadAds() {
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    @Override
    public void onBackPressed() {

        if(webView.canGoBack())
        {
           // getSupportActionBar().show();
            webView.goBack();
        }else{
            finish();
            super.onBackPressed();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        if(webView.canGoBack())
        {
            webView.goBack();
        }else{
            finish();
        }
        return super.onSupportNavigateUp();
    }

    private class myWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            getSupportActionBar().hide();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//            view.setVisibility(View.GONE);
            String filename="/index.html";
            webView.loadUrl(filename);



        }
    }
}