package com.regain.attendie;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.Period;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    TextView forgetPassword;
    Button login_button;

    public static String cook = "1";
    TextView password, username, textView12;
    LinearLayout linearLayout, loginLayout;
    CheckBox checkBox;
TextView textView47;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView47=findViewById(R.id.textView47);
        textView47.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,NotRespond.class));
            }
        });
        loginLayout = findViewById(R.id.loginLinearLayout);
        textView12 = findViewById(R.id.textView12);
        checkBox = findViewById(R.id.checkBox);
        linearLayout = findViewById(R.id.mainActivityLinearLayout);
        forgetPassword = findViewById(R.id.forgetPassword);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login_button = findViewById(R.id.login_button);
        getSupportActionBar().hide();
        SharedPreferences shrd = getSharedPreferences("mypref", MODE_PRIVATE);
        String user_reg = shrd.getString("username", null);
        String user_pass = shrd.getString("password", null);
        if (user_reg != null && user_pass != null) {
            //visibility false
            loginLayout.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            //enablity false
            login(user_reg, user_pass, 1);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    textView12.setVisibility(View.VISIBLE);
                }
            }, 3000);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView12.setVisibility(View.GONE);
                String reg = username.getText().toString();
                String pass = password.getText().toString();
                if (reg.isEmpty()) {
                    username.setError("Please Enter Registration Number");
                    return;
                }
                if (pass.isEmpty()) {
                    password.setError("Please Enter Password");

                    return;
                }
                linearLayout.setVisibility(View.VISIBLE);
                login(reg, pass, 0);
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fp = new Intent(MainActivity.this, BrowseActivity.class);
                fp.putExtra("url", "http://103.112.27.37:8282/CampusPortalSOA/index#");
                startActivity(fp);
            }
        });


    }

    public void login(String reg, String pass, int w) {
        if (w == 0) {

        }
        Call<Object> login = API_COLLEGE.getPostService().User(new UserAuthentication(reg, pass, "S"));
        login.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                String s = new Gson().toJson(response.body());
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String success = jsonObject.get("message").toString();
                    String status = jsonObject.get("status").toString();
                    if (success.equals("Login Successful") && status.equals("success")) {
                        SharedPreferences sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", reg);
                        editor.putString("password", pass);
                        editor.putInt("id", 10);
                        editor.apply();
                        saveData(reg);
                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                        intent.putExtra("fullName", jsonObject.get("name").toString());
                        startActivity(intent);
                        finish();
                    } else if ((success.equals("The password is incorrect") || success.equals("The username is incorrect") || status.equals("error")) && w == 0) {
                        linearLayout.setVisibility(View.GONE);
                        username.setError("Invalid Credential");
                        password.setError("Invalid Credential");
                        Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
                    } else if ((success.equals("The password is incorrect") || success.equals("The username is incorrect") || status.equals("error")) && w == 1) {
                        //visibility false
                        password.setVisibility(View.VISIBLE);
                        username.setVisibility(View.VISIBLE);
                        login_button.setVisibility(View.VISIBLE);
                        loginLayout.setVisibility(View.VISIBLE);
                        linearLayout.setVisibility(View.GONE);
                        textView12.setVisibility(View.GONE);
                        //enablity true
                        login_button.setText("Log in");
                        login_button.setEnabled(true);
                        password.setEnabled(true);
                        username.setEnabled(true);
                        Toast.makeText(MainActivity.this, "Your credential seems to be changed", Toast.LENGTH_LONG).show();
                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                linearLayout.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Iter server not responding", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this,Servercrash.class));
                finish();


            }
        });

    }
private void saveData(String reg){
        try{
            Call<Object> saved=API_COLLEGE.getPostService().saveReg(reg);
            saved.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {

                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                }
            });
        }catch(Exception e){
            
        }
}

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}