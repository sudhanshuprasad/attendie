package com.regain.attendie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadResultActivity extends AppCompatActivity {
ImageView result_image;
String sem;
CoordinatorLayout download_coordinator;
LinearLayout downloadlinear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_result);
        download_coordinator=findViewById(R.id.download_coordinator);
        downloadlinear=findViewById(R.id.downloadlinear);
        sem=getIntent().getStringExtra("sem");
getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(ActivityCompat.checkSelfPermission(DownloadResultActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            showResult(sem);
        }else{
            checkPermission(sem);
        }
    }
    private void showResult(String sem) {

        DownloadResult downloadResult = new DownloadResult(sem);
        Call<ResponseBody> download_data=API_COLLEGE.getPostService().downloadResult(downloadResult);
        download_data.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                downloadlinear.setVisibility(View.GONE);
                InputStream inputStream=response.body().byteStream();
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        downloadFinallyResult(inputStream);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void unused) {
                        super.onPostExecute(unused);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 2000);

                    }
                }.execute();

//                String ext= Environment.getExternalStoragePublicDirectory(Directory.);
//                File f=new File();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private void checkPermission(String sem){

        if (ActivityCompat.shouldShowRequestPermissionRationale(DownloadResultActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(DownloadResultActivity.this)
                    .setMessage("Please give permission of storage if you want to download the result")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(DownloadResultActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},101);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create().show();
        }else{
            ActivityCompat.requestPermissions(DownloadResultActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==101){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                showResult(sem);
            }else{
                downloadlinear.setVisibility(View.GONE);
                Snackbar.make(download_coordinator,"Please allow Storage Permission",Snackbar.LENGTH_LONG).show();
           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {
                   finish();
               }
           },2000);
            }
        }
    }
    private void downloadFinallyResult(InputStream inputStream1) {
        Snackbar.make(download_coordinator,"Downloading started please wait.....",Snackbar.LENGTH_INDEFINITE).show();
InputStream inputStream=inputStream1;
try {
    byte[] fileReader = new byte[4096];
    ContextWrapper contextWrapper=new ContextWrapper(getApplicationContext());
    File f=contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) , ProfileActivity.usrname+"Result_"+sem+".pdf");
 FileOutputStream fileOutputStream = new FileOutputStream(file );
    int read = 0;
    while ((read = inputStream.read(fileReader)) != -1) {
        fileOutputStream.write(fileReader, 0, read);
    }
    inputStream.close();
    fileOutputStream.close();
    inputStream1.close();
}catch (Exception e){
    e.printStackTrace();
}


        Snackbar.make(download_coordinator,"Download Successfully.check your download folder",Snackbar.LENGTH_LONG).show();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
