package com.regain.attendie;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolders> {
    Context context;
    ArrayList<Result_Model> al;
    boolean offline = false;

    public ResultAdapter(Context context, ArrayList<Result_Model> al, boolean offfl) {
        this.context = context;
        this.al = al;
        this.offline = offfl;
    }


    @Override
    public ViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.result_card, parent, false);
        return new ViewHolders(v);
    }

    @Override
    public void onBindViewHolder(ViewHolders holder, int position) {
        if (offline)
            holder.download_button.setVisibility(View.GONE);
        holder.download_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultDownloadActivity = new Intent(context, DownloadResultActivity.class);
                resultDownloadActivity.putExtra("sem", (position + 1) + "");
                context.startActivity(resultDownloadActivity);
            }
        });
        Result_Model result_model = al.get(position);
        holder.semester_name.setText(result_model.getSemester_name());
        holder.total_credit.setText(result_model.getTotalearnedcredit());
        holder.sgpa.setText(result_model.getSgpa());
        holder.itemView
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<DetailedResultModel> al = new ArrayList<>();
                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
                        View viewsheet = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.bottom_sheet_layout, null);
                        if(!offline)
                        viewsheet.findViewById(R.id.download_result_button).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent resultDownloadActivity1 = new Intent(context, DownloadResultActivity.class);
                                resultDownloadActivity1.putExtra("sem", (position + 1) + "");
                                context.startActivity(resultDownloadActivity1);
                            }
                        });
                        else
                            viewsheet.findViewById(R.id.download_result_button).setVisibility(View.GONE);
                        TextView bottomsheet_result_caption = viewsheet.findViewById(R.id.bottomsheet_result_caption);
                        String pt = "th";
                        if (position == 0)
                            pt = "st";
                        else if (position == 1)
                            pt = "nd";
                        else if (position == 2)
                            pt = "rd";
                        bottomsheet_result_caption.setText((position + 1) + pt + " sem");
                        TextView c_earn = viewsheet.findViewById(R.id.c_earn);
                        TextView sgpa_earn = viewsheet.findViewById(R.id.sgpa_earn);
                        c_earn.setText(result_model.getTotalearnedcredit());
                        sgpa_earn.setText(result_model.getSgpa());
                        RecyclerView recyclerView = viewsheet.findViewById(R.id.bottom_sheet_recyclerview);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        DetailedResultAdapter detailedResultAdapter = new DetailedResultAdapter(context, al);

                        ///call api
                        Semester sem = new Semester((position + 1) + "");
                        if (offline) {

                            try {
                                SharedPreferences sharedPreferences = context.getSharedPreferences("myResult", Context.MODE_PRIVATE);

                                String str = sharedPreferences.getString("st"+position, null);
                                if(str==null){
                                    Toast.makeText(context,"Data not available",Toast.LENGTH_LONG).show();

                                }else {
                                    JSONObject jsonObject = new JSONObject(str);
                                    JSONArray jsonArray = jsonObject.getJSONArray("Semdata");
                                    viewsheet.findViewById(R.id.progressBar6).setVisibility(View.GONE);
                                    DetailedResultModel detailedResultModel;
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String sub_name = jsonObject1.getString("subjectdesc");
                                        String sub_code = jsonObject1.getString("subjectcode");
                                        String grade = jsonObject1.getString("grade");
                                        String credit_earned = jsonObject1.getString("earnedcredit");
                                        detailedResultModel = new DetailedResultModel(sub_name, sub_code, grade, credit_earned);
                                        al.add(detailedResultModel);

                                    }
                                    detailedResultAdapter.notifyDataSetChanged();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Call<Object> data = API_COLLEGE.getPostService().getDetailedResult(sem);
                            data.enqueue(new Callback<Object>() {
                                @Override
                                public void onResponse(Call<Object> call, Response<Object> response) {
                                    String str = new Gson().toJson(response.body());
                                    try {
                                        JSONObject jsonObject = new JSONObject(str);
                                        JSONArray jsonArray = jsonObject.getJSONArray("Semdata");
                                        viewsheet.findViewById(R.id.progressBar6).setVisibility(View.GONE);
                                        DetailedResultModel detailedResultModel;
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            String sub_name = jsonObject1.getString("subjectdesc");
                                            String sub_code = jsonObject1.getString("subjectcode");
                                            String grade = jsonObject1.getString("grade");
                                            String credit_earned = jsonObject1.getString("earnedcredit");
                                            detailedResultModel = new DetailedResultModel(sub_name, sub_code, grade, credit_earned);
                                            al.add(detailedResultModel);

                                        }
                                        detailedResultAdapter.notifyDataSetChanged();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Object> call, Throwable t) {

                                }
                            });
                        }
                        recyclerView.setAdapter(detailedResultAdapter);
                        bottomSheetDialog.setContentView(viewsheet);
                        bottomSheetDialog.show();

                    }


                });
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    class ViewHolders extends RecyclerView.ViewHolder {
        TextView semester_name, sgpa, total_credit;
        Button download_button;

        public ViewHolders(View itemView) {
            super(itemView);
            semester_name = itemView.findViewById(R.id.semester_name);
            sgpa = itemView.findViewById(R.id.sgpa);
            total_credit = itemView.findViewById(R.id.total_credit);
            download_button = itemView.findViewById(R.id.download_result);
        }
    }


}
