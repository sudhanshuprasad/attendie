package com.regain.attendie;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    Context context;
    ArrayList<Model_Profile> arrayList;

    public CustomAdapter(Context context, ArrayList<Model_Profile> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.profile_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Model_Profile model_profile = arrayList.get(position);
        holder.profile_card_text.setText(model_profile.getText());
        holder.profile_card_image.setImageResource(model_profile.getImg_id());
        if (position == 1) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context.getApplicationContext(), ResultActivity.class));
                }
            });
        }
        if (position == 7) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context.getApplicationContext(), HolidayActivity.class));
                }
            });
        }
        if (position == 0) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context.getApplicationContext(), AttendanceActivity.class));
                }
            });
        }
        if (position == 2) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mp = new Intent(context.getApplicationContext(), BrowseActivity.class);
                    mp.putExtra("url", "http://136.233.14.6/moodle/login/index.php");
                    context.startActivity(mp);
                }
            });
        }
        if (position == 4) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mp = new Intent(context.getApplicationContext(), BrowseActivity.class);
                    mp.putExtra("url", "https://www.soa.ac.in/iter-exam-notice");
                    context.startActivity(mp);
                }
            });
        }
        if (position == 5) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mp = new Intent(context.getApplicationContext(), BrowseActivity.class);
                    mp.putExtra("url", "https://www.soa.ac.in/iter-student-notice");
                    context.startActivity(mp);
                }
            });
        }
        if (position == 6) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mp = new Intent(context.getApplicationContext(), BrowseActivity.class);
                    mp.putExtra("url", "https://www.soa.ac.in/iter-time-table");
                    context.startActivity(mp);
                }
            });
        }
        if (position == 3) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mp = new Intent(context.getApplicationContext(), BrowseActivity.class);
                    mp.putExtra("url", "https://drive.google.com/drive/folders/1oCGtF2xAvgfk96Vw29KnfGOiDrxWwCi1?usp=sharing");
                    context.startActivity(mp);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_card_image;
        TextView profile_card_text;

        public ViewHolder(View itemView) {
            super(itemView);
            profile_card_text = itemView.findViewById(R.id.profile_card_text);
            profile_card_image = itemView.findViewById(R.id.profile_card_image);
        }
    }

}
