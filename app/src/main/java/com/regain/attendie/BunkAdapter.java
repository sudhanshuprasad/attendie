package com.regain.attendie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BunkAdapter extends RecyclerView.Adapter<BunkAdapter.BunkViewHolder>{
    Context context;
    ArrayList<String> ss;

    public BunkAdapter(Context context, ArrayList<String> ss) {
        this.context = context;
        this.ss = ss;
    }

    @Override
    public BunkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.attendance_card_adapter,parent,false);
        return new BunkViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BunkViewHolder holder, int position) {
holder.bunk_text.setText(ss.get(position));
    }

    @Override
    public int getItemCount() {
        return ss.size();
    }

    class BunkViewHolder extends RecyclerView.ViewHolder{
TextView bunk_text;
        public BunkViewHolder( View itemView) {
            super(itemView);
            bunk_text=itemView.findViewById(R.id.bunk_text);
        }
    }
}
