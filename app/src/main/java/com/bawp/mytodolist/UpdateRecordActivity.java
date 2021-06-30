package com.bawp.mytodolist;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bawp.mytodolist.Utils.TaskDBHelper;
import com.bawp.mytodolist.tasks.Tasks;


public class UpdateRecordActivity extends AppCompatActivity {

    private EditText mTitleEditText;
    private EditText mDetailsEditText;
    private Button mUpdateBtn;

    private TaskDBHelper dbHelper;
    private long receivedPersonId;

    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;

    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();

    private long fileSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_record);

        //init
        mTitleEditText = (EditText)findViewById(R.id.taskstitleupdate);
        mDetailsEditText = (EditText)findViewById(R.id.taskDetailsUpdate);
        mUpdateBtn = (Button)findViewById(R.id.updateUserButton);

        dbHelper = new TaskDBHelper(this);

        try {
            //get intent to get person id
            receivedPersonId = getIntent().getLongExtra("USER_ID", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /***populate user data before update***/
        Tasks queriedTasks = dbHelper.getTask(receivedPersonId);
        //set field to this user data
        mTitleEditText.setText(queriedTasks.getTaskTitle());
        mDetailsEditText.setText(queriedTasks.getTaskDetails());



        //listen to add button click to update
        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progress = new ProgressDialog(UpdateRecordActivity.this);
                progress.setTitle("Connecting");
                progress.setCancelable(false);
                progress.setMessage("Task Updating....");
                progress.show();

                Runnable progressRunnable = new Runnable() {

                    @Override
                    public void run() {
                        updatePerson();
                        progress.cancel();
                    }
                };

                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable, 3000);

                //call the save person method
               // updatePerson();
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.delete_back:
                deletedetails();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deletedetails() {

        builder = new AlertDialog.Builder(this);
        inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.confirmation_pop,null);

        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        Button noButton = view.findViewById(R.id.conf_no_button);
        Button yesButton = view.findViewById(R.id.conf_yes_button);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Tasks tasks = new Tasks();
                final ProgressDialog progress = new ProgressDialog(UpdateRecordActivity.this);
                progress.setTitle("Connecting");
                progress.setCancelable(false);
                progress.setMessage("Task Delete Successfully Done....");
                progress.show();

                Runnable progressRunnable = new Runnable() {

                    @Override
                    public void run() {
                        TasksAsync tasksAsync = new TasksAsync(UpdateRecordActivity.this);
                        tasksAsync.execute("delete" , String.valueOf(receivedPersonId));
                        //progressBar.setProgress(10);
                        finish();
                        // dbHelper.deleteTaskRecord(receivedPersonId);
                        dialog.dismiss();
                        goBackHome();
                        progress.cancel();
                    }
                };

                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable, 3000);


            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void updatePerson(){
        String task = mTitleEditText.getText().toString().trim();
        String details = mDetailsEditText.getText().toString().trim();


        if(task.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter a name", Toast.LENGTH_SHORT).show();
        }



        if(details.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter an occupation", Toast.LENGTH_SHORT).show();
        }


        TasksAsync tasksAsync = new TasksAsync(this);
        tasksAsync.execute("edit" , String.valueOf(receivedPersonId), task, details);
        //progressBar.setProgress(10);
        finish();

        //create updated person
        //Tasks updateTask = new Tasks(name, details);

        //call dbhelper update
        //dbHelper.updateTaskRecord(receivedPersonId, this, updateTask);

        //finally redirect back home
        // NOTE you can implement an sqlite callback then redirect on success delete
        goBackHome();

    }

    private void goBackHome(){
        startActivity(new Intent(this, MainActivity.class));
    }
}
