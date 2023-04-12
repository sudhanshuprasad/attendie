package com.regain.attendie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Update_Password extends AppCompatActivity {
TextView old_password,confirm_password,password_flag;
Button update_button;
private CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        old_password=findViewById(R.id.old_password);
        confirm_password=findViewById(R.id.confirm_password);
        update_button=findViewById(R.id.update_button);
        coordinatorLayout=findViewById(R.id.coordinatorLayout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_pass=old_password.getText().toString();
                String confirm_pass=confirm_password.getText().toString();
                if(old_pass.equals(confirm_pass)){
                    Update_Change_Password update_change_password=new Update_Change_Password(old_pass,confirm_pass);
                    update(update_change_password);
                }else{
                   showSnackbar("Password do not match");
                }
            }
        });
    }
    private void update(Update_Change_Password update_change_password){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call<Object> upd=API_COLLEGE.getPostService().Update_password(update_change_password);
                upd.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        String succeed=new Gson().toJson(response.body());
                        try {
                            JSONObject jsonObject=new JSONObject(succeed);
                            String msg=jsonObject.getString("Success");
                            if(msg.equals("Password Updated Successfully")){
//                                Snackbar snackbar=
                                SharedPreferences sharedPreferences=getSharedPreferences("mypref",MODE_PRIVATE);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.remove("password");
                                editor.putString("password",update_change_password.getConfirmpassword());
                                editor.apply();
                               showSnackbar("Successfully Changed");
                            }else{
                                showSnackbar("Something went wrong.Try Again later");
                            }
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
    private void showSnackbar(String s){
        Snackbar snackbar=Snackbar.make(coordinatorLayout,s,Snackbar.LENGTH_LONG);
//        snackbar.setTextColor(android.R.color.holo_orange_dark);
        snackbar.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}