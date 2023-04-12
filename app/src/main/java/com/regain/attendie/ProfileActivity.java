package com.regain.attendie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    public static String version = "1";
    RecyclerView recyclerView;
    TextView fullname, branch_textview, degree_textview;
    ArrayList<Model_Profile> al;
    ImageView profile_image;
    public static String usrname = "";
    BottomSheetDialog bottomSheetDialog;
    String s_reg="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setElevation(0);
        fullname = findViewById(R.id.fullname);
        branch_textview = findViewById(R.id.branch);
        degree_textview = findViewById(R.id.degree);
        profile_image = findViewById(R.id.profile_image);
        usrname = getIntent().getStringExtra("fullName");
        fullname.setText(usrname);
        recyclerView = findViewById(R.id.profile_recyclerview);
        al = new ArrayList<>();
        getPic();
        CustomAdapter customAdapter = new CustomAdapter(this, al);
        String[] a = {"Attendance", "Result", "Iter Moodle", "Study Collection", "Exam Notice", "Student Notice", "Time Table", "Holiday List"};
        int img_id[] = {R.drawable.attendance_register, R.drawable.result, R.drawable.moodle, R.drawable.coin_collecting, R.drawable.test, R.drawable.graduate, R.drawable.timetable, R.drawable.calendar_logo};
        Model_Profile modelProfile;
        for (int i = 0; i < a.length; i++) {
            modelProfile = new Model_Profile(a[i], img_id[i]);
            al.add(modelProfile);
        }
        customAdapter.notifyDataSetChanged();
        recyclerView.setItemViewCacheSize(100);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        checkOfflineData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_stting:
                bottomSheetDialog = new BottomSheetDialog(ProfileActivity.this, R.style.BottomSheetTheme);
                View v = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.setting_bottomsheet, null);
                bottomSheetDialog.setContentView(v);
                bottomSheetDialog.show();
                try {
                    if (!MainActivity.cook.equals(version)) {
                        v.findViewById(R.id.check_for_update).setVisibility(View.VISIBLE);
                        v.findViewById(R.id.check_for_update).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent itd = new Intent(Intent.ACTION_VIEW);
                                itd.setData(Uri.parse("https://attendie.github.io/AttendieApp/"));
                                if (itd.resolveActivity(getPackageManager()) != null) {
                                    startActivity(itd);
                                }
                            }
                        });
                    }
                } catch (Exception e) {

                }
                v.findViewById(R.id.update_pass).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ProfileActivity.this, Update_Password.class));
                    }
                });
                v.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logout();
                    }
                });
                v.findViewById(R.id.add_account).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.cancel();
                        logout();
                    }
                });
                v.findViewById(R.id.share_item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.cancel();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Intent share_app = new Intent(Intent.ACTION_SEND);
                                share_app.setType("text/plain");
                                share_app.putExtra(Intent.EXTRA_SUBJECT, "Attendie App");
                                share_app.putExtra(Intent.EXTRA_TEXT, "Track Your Attendance, Result ,Iter Moodle, Study Collection, Exam Notice, Student Notice ,TimeTable ,Set an Alarm for Your Class Routine ,Plan your vaccation using Holiday and many more.. Currently available for Android devices.https://attendie.github.io/AttendieApp/");
                                startActivity(Intent.createChooser(share_app, "Share Attendie Using"));
                            }
                        }).start();

                    }
                });
                v.findViewById(R.id.about_info).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.aboutapp_layout, null, false);
                        new AlertDialog.Builder(ProfileActivity.this)
                                .setTitle("About App")
                                .setView(view)
                                .setCancelable(true)
                                .create().show();
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getPic() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call<Object> info = API_COLLEGE.getPostService().getInfo();
                info.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        String str_info = new Gson().toJson(response.body());
                        try {
                            JSONObject jsonObject = new JSONObject(str_info);
                            JSONArray jsonArray = jsonObject.getJSONArray("detail");
                            JSONObject js = jsonArray.getJSONObject(0);
                            String branch = js.getString("branchdesc");
                            String degree = js.getString("programdesc");
                            String student_semester=js.getString("stynumber");
                            branch_textview.setText(branch);
                            degree_textview.setText(degree);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Toast.makeText(ProfileActivity.this, "Iter Server is not responding", Toast.LENGTH_LONG).show();
                    }
                });
                Call<ResponseBody> photo = API_COLLEGE.getPostService().getPhoto();
                photo.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {


                            InputStream inputStream = response.body().byteStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            Glide.with(ProfileActivity.this).load(bitmap).into(profile_image);
                        }catch (Exception e){

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        }).start();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }

    private void logout() {
        SharedPreferences shrd = getSharedPreferences("mypref", MODE_PRIVATE);
        SharedPreferences.Editor editor = shrd.edit();
        editor.clear();
        editor.apply();
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        finishAffinity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(CookieJar.l<1) {
            startActivity(new Intent(ProfileActivity.this,Splash_Screen.class));
            finishAffinity();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    private void checkOfflineData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences logindata=getSharedPreferences("mypref",MODE_PRIVATE);
                String login_reg=logindata.getString("username",null);
                SharedPreferences result_sharedData = getSharedPreferences("myResult", MODE_PRIVATE);
                String result_data = result_sharedData.getString("resultData", null);
                String result_login=result_sharedData.getString("result_login_name",null);
                    Call<Object> getresult = API_COLLEGE.getPostService().result();
                    getresult.enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            String result_json = new Gson().toJson(response.body());
                            SharedPreferences.Editor shrd = result_sharedData.edit();
                            int arr_length=result_sharedData.getInt("result_length",0);
                            shrd.putString("resultData", result_json);
                            try {
                                JSONObject jsonObject = new JSONObject(result_json);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                int jsonArray_length=jsonArray.length();
                                if(arr_length!=jsonArray_length || !login_reg.equals(result_login)) {
                                    shrd.putInt("result_length", jsonArray_length);
                                    shrd.putString("result_login_name",login_reg);

                                    for (int k = 0; k < jsonArray_length; k++) {
                                        String result_name = "st" + k;
                                        Semester sem = new Semester((k + 1) + "");
                                        Call<Object> detailed_data = API_COLLEGE.getPostService().getDetailedResult(sem);
                                        detailed_data.enqueue(new Callback<Object>() {
                                            @Override
                                            public void onResponse(Call<Object> call, Response<Object> response) {
                                                String str = new Gson().toJson(response.body());
                                                shrd.putString(result_name, str);
                                                shrd.apply();
                                            }

                                            @Override
                                            public void onFailure(Call<Object> call, Throwable t) {

                                            }
                                        });
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            shrd.apply();
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {

                        }
                    });

                Call<Object> attendance_reg=API_COLLEGE.getPostService().attendanceInfos();
                attendance_reg.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        String json_reg=new Gson().toJson(response.body());

                        try {
                            JSONObject jspn=new JSONObject(json_reg);
                            s_reg=jspn.getString("reglov");
                            JSONObject jspn2=new JSONObject(s_reg);
                            Iterator<?> keys = jspn2.keys();

                            while( keys.hasNext() ) {
                                String key = (String) keys.next();
                                s_reg=key;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ///////////////////////////
                        SharedPreferences shrd=getSharedPreferences("mypref",MODE_PRIVATE);
                        String user_reg=s_reg;
                        RegistrationAttendance registrationAttendance=new RegistrationAttendance(user_reg);
                        Call<Object> offlineattendance=API_COLLEGE.getPostService().attendanceInfo(registrationAttendance);
                        offlineattendance.enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                String json_data=new Gson().toJson(response.body());
                                SharedPreferences sharedPreferences_attend=getSharedPreferences("myattendance",MODE_PRIVATE);
                                SharedPreferences.Editor sharededit=sharedPreferences_attend.edit();
                                sharededit.putString("attendance_data",json_data);
                                sharededit.apply();
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {

                            }
                        });
                        /////////////////////////////////////////////////
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });


            }
        }).start();
    }
}