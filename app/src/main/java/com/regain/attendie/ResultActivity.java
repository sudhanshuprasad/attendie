package com.regain.attendie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity {
    ArrayList<Result_Model> r_al;
    RecyclerView recyclerView;
    LinearLayout resultLinear;
    TextView delay_text,cgpa_textview;
    ProgressBar progressBar3;
    ResultAdapter resultAdapter;
    boolean offline = false;
    AdView result_adView;
    double cgpa=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        result_adView=findViewById(R.id.result_adView);
        resultLinear = findViewById(R.id.resultLinear);
        progressBar3 = findViewById(R.id.progressBar3);
        delay_text = findViewById(R.id.delay_text);
        cgpa_textview=findViewById(R.id.cgpa_textview);
        getSupportActionBar().setTitle(ProfileActivity.usrname);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        r_al = new ArrayList<>();
        recyclerView = findViewById(R.id.result_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        offline = getIntent().getBooleanExtra("offline", false);
        resultAdapter = new ResultAdapter(this, r_al,offline);
        recyclerView.setAdapter(resultAdapter);
        loadAds();
//recieve intent data from Not_respond activity

//offline data showing if offline is false
        if (offline) {
            //access sharepreferenced for offline data
            SharedPreferences shrdPrefeneces = getSharedPreferences("myResult", MODE_PRIVATE);
            String dta = shrdPrefeneces.getString("resultData", null);
            getSupportActionBar().setTitle("Offline View");
            //check data is null or not null
            if (dta != null) {
//data write for offline.....
                showOfflineData(dta);
            } else {
                //if offline data is not present
                delay_text.setVisibility(View.VISIBLE);
                progressBar3.setVisibility(View.GONE);
                delay_text.setText("Data is not present for offline.\nYou must login once to save your data.");
            }
        } else {
            //show online data if offline is false
            showResult();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void showResult() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                delay_text.setVisibility(View.VISIBLE);
            }
        }, 3000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call<Object> getresult = API_COLLEGE.getPostService().result();
                getresult.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        resultLinear.setVisibility(View.GONE);
                        String result_json = new Gson().toJson(response.body());
                        try {
                            JSONObject jsonObject = new JSONObject(result_json);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            Result_Model model;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                model = new Result_Model();
                                JSONObject f = jsonArray.getJSONObject(i);
                                model.setSemester_name(f.getString("Semesterdesc"));
                                model.setSgpa(f.getString("sgpaR"));
                                model.setTotalearnedcredit(f.getString("totalearnedcredit"));
                                r_al.add(model);
                                cgpa+=Double.parseDouble(f.getString("sgpaR"));
                            }
                            cgpa_textview.setText("CGPA:"+new DecimalFormat("##.##").format(cgpa/r_al.size()));
                            resultAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });
            }
        }).start();
    }

    private void showOfflineData(String result_data) {
        resultLinear.setVisibility(View.GONE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Result_Model model;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        model = new Result_Model();
                        JSONObject f = jsonArray.getJSONObject(i);
                        model.setSemester_name(f.getString("Semesterdesc"));
                        model.setSgpa(f.getString("sgpaR"));
                        model.setTotalearnedcredit(f.getString("totalearnedcredit"));
                        r_al.add(model);
                        cgpa+=Double.parseDouble(f.getString("sgpaR"));
                    }
                    cgpa_textview.setText("CGPA:"+new DecimalFormat("##.##").format(cgpa/r_al.size()));
                    resultAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(CookieJar.l<1 &&offline==false) {
            startActivity(new Intent(ResultActivity.this,Splash_Screen.class));
            finishAffinity();
        }
    }
    private void loadAds() {
        AdRequest adRequest=new AdRequest.Builder().build();
        result_adView.loadAd(adRequest);

    }
}