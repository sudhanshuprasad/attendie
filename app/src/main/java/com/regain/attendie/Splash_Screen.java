package com.regain.attendie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash_Screen extends AppCompatActivity {
    ProgressBar progressBar;
    String url_recieved=null;
    public static String cook = "1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        progressBar=findViewById(R.id.progressBar_splash);
        getSupportActionBar().hide();
        url_recieved=getIntent().getStringExtra("url");
        if(url_recieved!=null){
            startActivity(new Intent(this,BrowseActivity.class).putExtra("url",url_recieved));
            finish();
            return;
        }
        subscribeTopic();
        adsinitialise();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splash_login();
            }
        },1000);

    }

    private void adsinitialise() {
        try {
            MobileAds.initialize(Splash_Screen.this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

                }
            });
        }catch (Exception e){

        }
    }

    private void splash_login() {
        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences=getSharedPreferences("mypref",MODE_PRIVATE);
        String username=sharedPreferences.getString("username",null);
        String password=sharedPreferences.getString("password",null);
        if(username!=null&&password!=null){
            login_user(username,password);
        }else{
            progressBar.setVisibility(View.GONE);
            startActivity(new Intent(Splash_Screen.this, MainActivity.class));
            finish();
        }
    }

    private void login_user(String user,String pass) {
        Call<Object> res=API_COLLEGE.getPostService().User(new UserAuthentication(user, pass, "S"));
        res.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
//                GsonBuilder gsonBuilder = new GsonBuilder();
                String s = new Gson().toJson(response.body());
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String success = jsonObject.get("message").toString();
                    String status = jsonObject.get("status").toString();
                    if (success.equals("Login Successful") && status.equals("success")) {
//                        saveData(user);
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(Splash_Screen.this, ProfileActivity.class);
                        intent.putExtra("fullName", jsonObject.get("name").toString());
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(Splash_Screen.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                startActivity(new Intent(Splash_Screen.this,Servercrash.class));
                finish();
            }
        });
    }
    private void subscribeTopic(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MyNotifications", "Attendie", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        try {
            FirebaseMessaging.getInstance().subscribeToTopic("myapp")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "done";
                            if (!task.isSuccessful()) {
                                msg = "not done";
                            }

                        }
                    });
            FirebaseMessaging.getInstance().subscribeToTopic("newapp")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "done";
                            if (!task.isSuccessful()) {
                                msg = "not done";
                            }

                        }
                    });

        }catch (Exception e){

        }

    }


}