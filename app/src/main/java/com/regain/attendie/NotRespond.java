package com.regain.attendie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import java.util.ArrayList;

public class NotRespond extends AppCompatActivity {

    ArrayList<Model_Profile> al;
    RecyclerView notrespondrecyclerview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_respond);
        al=new ArrayList<Model_Profile>();
        notrespondrecyclerview=findViewById(R.id.notrespondrecyclerview);
        notrespondrecyclerview.setLayoutManager(new GridLayoutManager(this,2));

        String[] a={"Attendance","Result","Iter Moodle","Study Collection","Exam Notice","Student Notice","Time Table","Holiday List"};
        int img_id[]={R.drawable.attendance_register,R.drawable.result,R.drawable.moodle,R.drawable.coin_collecting,R.drawable.test,R.drawable.graduate,R.drawable.timetable,R.drawable.calendar_logo};
        Model_Profile modelProfile;
        for(int i=0;i<a.length;i++){
            modelProfile=new Model_Profile(a[i],img_id[i]);
            al.add(modelProfile);
        }
        Not_Recycler_Adapter not_recycler_adapter=new Not_Recycler_Adapter(this,al);
        not_recycler_adapter.notifyDataSetChanged();
        notrespondrecyclerview.setItemViewCacheSize(100);
        notrespondrecyclerview.setAdapter(not_recycler_adapter);
    }

}