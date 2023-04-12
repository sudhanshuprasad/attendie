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

public class DetailedResultAdapter extends RecyclerView.Adapter<DetailedResultAdapter.DetailResultViewHolder>{
    Context context;
    ArrayList<DetailedResultModel> al;

    public DetailedResultAdapter(Context context, ArrayList<DetailedResultModel> al) {
        this.context = context;
        this.al = al;
    }


    @Override
    public DetailResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.detail_result_card,parent,false);
        return new DetailResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DetailResultViewHolder holder, int position) {
DetailedResultModel detailedResultModel=al.get(position);
holder.sub_name.setText(detailedResultModel.getSub_name());
holder.subject_code.setText(detailedResultModel.getSubject_code());
holder.grade.setText(detailedResultModel.getGrade());
holder.credit_earned.setText(detailedResultModel.getCredit_earned());

    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    class DetailResultViewHolder extends RecyclerView.ViewHolder{
TextView sub_name,subject_code,grade,credit_earned;
        public DetailResultViewHolder(View itemView) {
            super(itemView);
            sub_name=itemView.findViewById(R.id.sub_name);
            subject_code=itemView.findViewById(R.id.subject_code);
            grade=itemView.findViewById(R.id.grade);
            credit_earned=itemView.findViewById(R.id.credit_earned);
        }
    }
}
