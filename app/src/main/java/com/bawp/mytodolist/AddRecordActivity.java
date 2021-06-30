package com.bawp.mytodolist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bawp.mytodolist.Utils.TaskDBHelper;
import com.bawp.mytodolist.tasks.Tasks;


public class AddRecordActivity extends AppCompatActivity {
    private EditText mNameEditText;
    private EditText mOccupationEditText;
    private Button mAddBtn;
    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    private long fileSize = 0;

    private TaskDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        //init
        mNameEditText = (EditText)findViewById(R.id.taskName);
        mOccupationEditText = ( EditText)findViewById(R.id.taskDetails);
        mAddBtn = (Button)findViewById(R.id.addNewUserButton);
       // progressBar = findViewById(R.id.p_bar_add);

        //listen to add button click
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progress = new ProgressDialog(AddRecordActivity.this);
                progress.setTitle("Connecting");
                progress.setCancelable(false);
                progress.setMessage("Task Add Successfully Done....");
                progress.show();

                Runnable progressRunnable = new Runnable() {

                    @Override
                    public void run() {
                        savePerson();
                        progress.cancel();
                    }
                };

                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable, 3000);
                //call the save person method

            }
        });

    }


    private void savePerson(){
        String task = mNameEditText.getText().toString().trim();
        String details = mOccupationEditText.getText().toString().trim();
        dbHelper = new TaskDBHelper(this);

        if(task.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter a name", Toast.LENGTH_SHORT).show();
        }

        if(details.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter an occupation", Toast.LENGTH_SHORT).show();
        }

        TasksAsync tasksAsync = new TasksAsync(this);
        tasksAsync.execute("add_data" , task, details);
        //progressBar.setProgress(10);
        finish();

        //create new person
        //Tasks tasks = new Tasks(task, details);
        //dbHelper.saveNewTask(tasks);

        //finally redirect back home
        // NOTE you can implement an sqlite callback then redirect on success delete
        goBackHome();

    }

    private void goBackHome(){
        startActivity(new Intent(AddRecordActivity.this, MainActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
