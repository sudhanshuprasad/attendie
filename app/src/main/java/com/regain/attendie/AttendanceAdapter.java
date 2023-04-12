package com.regain.attendie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>{
    Context context;
    ArrayList<AttendanceModel2> attendanceModels;

    public AttendanceAdapter(Context context, ArrayList<AttendanceModel2> attendanceModels) {
        this.context = context;
        this.attendanceModels = attendanceModels;
    }

    @Override
    public AttendanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.attendance_card,parent,false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AttendanceViewHolder holder, int position) {
AttendanceModel2 attendanceModel=attendanceModels.get(position);
holder.progressBar.setProgress((int)Double.parseDouble(attendanceModel.getTotalAttandence()));
holder.progress_percentage.setText(""+Double.parseDouble(attendanceModel.getTotalAttandence())+"%");
//if(attendanceModel.totalPercent()>74){
//
//}
holder.subject_name.setText(attendanceModel.subject);
        holder.theory.setText("" + attendanceModel.getLatt());
        holder.lab.setText("" + attendanceModel.getPatt());
//if(attendanceModel.getTheoryTotal()==0) {
//    holder.theory.setText("Not Available");
//}
//else{
//    holder.theory.setVisibility(View.INVISIBLE);
//}
//if(attendanceModel.getLabTotal()==0) {
//    holder.lab.setText("Not Available");
//}
//else{
//    holder.lab.setVisibility(View.INVISIBLE);
//}
ArrayList<String> ss=new ArrayList<>();
ArrayList<String> bunkAdpater=attendanceModel.bunk;
ArrayList<String> needAdpater=attendanceModel.need;
//Toast.makeText(context.getApplicationContext(), bunkAdpater.size()+":"+needAdpater.size(),Toast.LENGTH_LONG).show();
for(int i=0;i<bunkAdpater.size();i++){
    ss.add(bunkAdpater.get(i));
}
        for(int i=0;i<needAdpater.size();i++){
            ss.add(needAdpater.get(i));

        }
        boolean expanded= attendanceModel.isExpanded();
        holder.listView.setVisibility(expanded?View.VISIBLE:View.GONE);
        holder.last_update.setText("View More for detail");
        holder.total_class.setText(attendanceModel.getTotal_class());
        holder.attend_class.setText(attendanceModel.getAttend_class());
        holder.viewMore.setText(expanded?"View Less":"View More");
                BunkAdapter bunkAdapter=new BunkAdapter(context,ss);
        holder.listView.setLayoutManager(new LinearLayoutManager(context));
        holder.listView.setAdapter(bunkAdapter);

    }


    @Override
    public int getItemCount() {
        return attendanceModels.size();
    }

    class AttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView subject_name,theory,lab,progress_percentage,viewMore,last_update,total_class,attend_class;
        ProgressBar progressBar;
        RecyclerView listView;
        public AttendanceViewHolder(View itemView) {
            super(itemView);
            last_update=itemView.findViewById(R.id.last_update);
            progressBar=itemView.findViewById(R.id.progressBar);
            progress_percentage=itemView.findViewById(R.id.progress_percentage);
            subject_name=itemView.findViewById(R.id.subject_name);
            theory=itemView.findViewById(R.id.theory);
            lab=itemView.findViewById(R.id.lab);
            viewMore=itemView.findViewById(R.id.expand_textview);
            listView=itemView.findViewById(R.id.listview);
            total_class=itemView.findViewById(R.id.total_class);
            attend_class=itemView.findViewById(R.id.attend_class);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AttendanceModel2 model=attendanceModels.get(getAdapterPosition());
                    model.setExpanded(!model.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }

}
