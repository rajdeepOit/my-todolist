package com.bawp.mytodolist.Utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import com.bawp.mytodolist.R;
import com.bawp.mytodolist.UpdateRecordActivity;
import com.bawp.mytodolist.tasks.Tasks;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class TasksApapter extends RecyclerView.Adapter<TasksApapter.ViewHolder>{
    private List<Tasks> mTasksList;
    private Context mContext;
    private RecyclerView mRecyclerV;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;


    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();

    private long fileSize = 0;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView taskTitleTxt;
        public TextView taskDetailsTxt;

        public View layout;

        public ViewHolder(@NonNull View v) {
            super(v);
            layout = v;
            taskTitleTxt =  v.findViewById(R.id.taskTitle);
            taskDetailsTxt =  v.findViewById(R.id.textDetails);


        }

        @Override
        public void onClick(View view) {
            int position;
            position = getAdapterPosition();
            Tasks tasks = mTasksList.get(position);
        }


    }

    public TasksApapter(List<Tasks> mTasksList, Context mContext, RecyclerView mRecyclerV) {
        this.mTasksList = mTasksList;
        this.mContext = mContext;
        this.mRecyclerV = mRecyclerV;
    }

    @NonNull
    @Override


    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v = inflater.inflate(R.layout.single_row, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final Tasks tasks = mTasksList.get(position);
        holder.taskTitleTxt.setText(MessageFormat.format("Task: {0}", tasks.getTaskTitle()));
        holder.taskDetailsTxt.setText(MessageFormat.format("Details: {0}", tasks.getTaskDetails()));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progress = new ProgressDialog(mContext);
                progress.setTitle("Connecting");
                progress.setCancelable(false);
                progress.setMessage("View Tasks....");
                progress.show();

                Runnable progressRunnable = new Runnable() {

                    @Override
                    public void run() {
                        goToUpdateActivity(tasks.getId());
                        progress.cancel();
                    }
                };

                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable, 3000);



            }
        });


    }

    private void goToUpdateActivity(long taskid) {
        Intent goToUpdate = new Intent(mContext, UpdateRecordActivity.class);
        goToUpdate.putExtra("USER_ID", taskid);
        mContext.startActivity(goToUpdate);
    }

    @Override
    public int getItemCount() {
        return mTasksList.size();
    }
    public void filterList(ArrayList<Tasks> filterdNames) {
        mTasksList = filterdNames;
        notifyDataSetChanged();
    }
}
