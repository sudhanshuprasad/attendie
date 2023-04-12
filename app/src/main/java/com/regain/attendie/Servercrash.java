package com.regain.attendie;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class Servercrash extends AppCompatActivity {
Button button_tryagain,button_offline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servercrash);
        button_tryagain=findViewById(R.id.button_tryagain);
        button_offline=findViewById(R.id.button_offline);
        button_tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Servercrash.this,MainActivity.class));
                finish();
            }
        });
        button_offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Servercrash.this,NotRespond.class));
                finish();
            }
        });

    }


}