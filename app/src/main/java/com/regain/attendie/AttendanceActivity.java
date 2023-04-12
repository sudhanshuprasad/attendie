package com.regain.attendie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceActivity extends AppCompatActivity {
    ArrayList<AttendanceModel2> a_Models;
    RecyclerView recyclerView;
    LinearLayout attendanceLinear;
    TextView attendance_delay_text;
    ProgressBar progressBar;
    AttendanceAdapter attendanceAdapter;
    boolean offline = false;
    AdView result_adView;
    String s_reg="";
    LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        try{
            lottieAnimationView=findViewById(R.id.no_data_anim);
        }catch (Exception e){

        }
        result_adView=findViewById(R.id.attend_adView);
        attendanceLinear = findViewById(R.id.attendanceLinear);
        progressBar = findViewById(R.id.progressBar5);
        attendance_delay_text = findViewById(R.id.attendance_delay_text);
        getSupportActionBar().setTitle(ProfileActivity.usrname);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        a_Models = new ArrayList<>();
        recyclerView = findViewById(R.id.attendance_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(100);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        attendanceAdapter = new AttendanceAdapter(this, a_Models);
        recyclerView.setAdapter(attendanceAdapter);
        //uncomment if not work start method
        offline = getIntent().getBooleanExtra("offline", false);
        loadAds();
        if (offline) {
            getOfflineData();
           getSupportActionBar().setTitle("Offline View");
        } else {
            getAttendanceDetail();
        }

    }

    private void getAttendanceDetail() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                attendance_delay_text.setVisibility(View.VISIBLE);
            }
        }, 3000);
        SharedPreferences shrd = getSharedPreferences("mypref", MODE_PRIVATE);
        String user_reg = shrd.getString("username", null);
//        RegistrationAttendance registrationAttendance = new RegistrationAttendance(user_reg);
        new Thread(new Runnable() {
            @Override
            public void run() {

                Call<Object> attendance_reg=API_COLLEGE.getPostService().lovInfos();
                attendance_reg.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        String json_reg=new Gson().toJson(response.body());

                        try {
                            JSONObject jo=new JSONObject(json_reg);
                            JSONArray jsa=jo.getJSONArray("studentdata");
                            JSONObject object= jsa.getJSONObject(0);
                            s_reg=object.getString("REGISTRATIONID");
//                            Log.d("hhd",s_reg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        Toast.makeText(AttendanceActivity.this,""+s_reg,Toast.LENGTH_LONG).show();
                        RegistrationAttendance registrationAttendance = new RegistrationAttendance(s_reg);
                        Call<Object> attendance_data = API_COLLEGE.getPostService().attendanceInfo(registrationAttendance);
                        attendance_data.enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                attendanceLinear.setVisibility(View.GONE);
                                String json = new Gson().toJson(response.body());

                                if (json.equals("{}") && json.length() < 5) {
                                    attendanceLinear.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    attendance_delay_text.setVisibility(View.VISIBLE);
                                    try {
                                        lottieAnimationView.setVisibility(View.VISIBLE);
                                    }catch (Exception e){

                                    }
                                    attendance_delay_text.setText("College has not yet updated attendance record");
                                    //debug
//                      json="{\"griddata\":[{\"subjectcode\":\"cs1001\",\"subject\":\"cse\",\"Latt\":\"12/12\",\"Patt\":\"35/39\",\"TotalAttandance\":\"60\"},{\"subjectcode\":\"ec1001\",\"subject\":\"ECE\",\"Latt\":\"23/38\",\"Patt\":\"20/25\",\"TotalAttendance\":\"60\"},{\"subjectcode\":\"mech1001\",\"subject\":\"Mechn\",\"Latt\":\"Not Applicable\",\"Patt\":\"36/39\",\"TotalAttendance\":\"60\"},{\"subjectcode\":\"csit1001\",\"subject\":\"CSIT\",\"Latt\":\"27/30\",\"Patt\":\"12/18\",\"TotalAttendance\":\"60\"},{\"subjectcode\":\"law1001\",\"subject\":\"LAW\",\"Latt\":\"44/50\",\"Patt\":\"Not Applicable\",\"TotalAttendance\":\"60\"}]}";
                                }

                                try {
                                    JSONObject jsonObject = new JSONObject(json);
//                                    Log.d("hhd",jsonObject.toString());
                                    JSONArray jsonArray = jsonObject.getJSONArray("griddata");
                                    int l = jsonArray.length();
                                    for (int i = 0; i < l; i++) {
                                        JSONObject js = jsonArray.getJSONObject(i);
                                        AttendanceModel2 attendanceModel = new AttendanceModel2();
                                        attendanceModel.lastupdation=js.getString("lastupdatedon");
                                        attendanceModel.stynumber=js.getString("stynumber");
                                        attendanceModel.subjectcode = js.getString("subjectcode");
                                        attendanceModel.subject = js.getString("subject");
                                        attendanceModel.setPatt(js.getString("Patt"));
                                        attendanceModel.setLatt(js.getString("Latt"));
                                        attendanceModel.setTotalAttandence(js.getString("TotalAttandence"));
                                        attendanceModel.getTotalclasses();
                                        attendanceModel.setAttendanceProcedure();
                                        a_Models.add(attendanceModel);

                                    }
                                    attendanceAdapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {
                                Toast.makeText(AttendanceActivity.this, "Iter server is not responding", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });

            }
        }).start();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void getOfflineData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                SharedPreferences sharedPreferences=getSharedPreferences("myattendance",MODE_PRIVATE);
                String json=sharedPreferences.getString("attendance_data",null);
                attendanceLinear.setVisibility(View.GONE);
//                String json = new Gson().toJson(response.body());
//                Toast.makeText(AttendanceActivity.this, json, Toast.LENGTH_SHORT).show();
                if (json.equals("{}") && json.length() < 5) {
                    attendanceLinear.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    attendance_delay_text.setVisibility(View.VISIBLE);
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    attendance_delay_text.setText("College has not yet updated attendance record");
//                      json="{\"griddata\":[{\"subjectcode\":\"cs1001\",\"subject\":\"cse\",\"Latt\":\"12/12\",\"Patt\":\"35/39\",\"TotalAttandance\":\"60\"},{\"subjectcode\":\"ec1001\",\"subject\":\"ECE\",\"Latt\":\"23/38\",\"Patt\":\"20/25\",\"TotalAttendance\":\"60\"},{\"subjectcode\":\"mech1001\",\"subject\":\"Mechn\",\"Latt\":\"Not Applicable\",\"Patt\":\"36/39\",\"TotalAttendance\":\"60\"},{\"subjectcode\":\"csit1001\",\"subject\":\"CSIT\",\"Latt\":\"27/30\",\"Patt\":\"12/18\",\"TotalAttendance\":\"60\"},{\"subjectcode\":\"law1001\",\"subject\":\"LAW\",\"Latt\":\"44/50\",\"Patt\":\"Not Applicable\",\"TotalAttendance\":\"60\"}]}";
                }


                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONArray("griddata");
                    int l = jsonArray.length();
                    for (int i = 0; i < l; i++) {
                        JSONObject js = jsonArray.getJSONObject(i);
                        AttendanceModel2 attendanceModel = new AttendanceModel2();
                        attendanceModel.lastupdation=js.getString("lastupdatedon");
                        attendanceModel.stynumber=js.getString("stynumber");
                        attendanceModel.subjectcode = js.getString("subjectcode");
                        attendanceModel.subject = js.getString("subject");
                        attendanceModel.setPatt(js.getString("Patt"));
                        attendanceModel.setLatt(js.getString("Latt"));
                        attendanceModel.setTotalAttandence(js.getString("TotalAttandence"));
                        attendanceModel.getTotalclasses();
                        attendanceModel.setAttendanceProcedure();
                        a_Models.add(attendanceModel);

                    }
                    attendanceAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(CookieJar.l<1&&offline==false) {
            startActivity(new Intent(AttendanceActivity.this,Splash_Screen.class));
            finishAffinity();
        }
//        getAttendanceDetail();
    }
    private void loadAds() {
        AdRequest adRequest=new AdRequest.Builder().build();
        result_adView.loadAd(adRequest);

    }

}