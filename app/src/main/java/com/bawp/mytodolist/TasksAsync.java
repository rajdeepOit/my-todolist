package com.bawp.mytodolist;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import com.bawp.mytodolist.Utils.TaskDBHelper;
import com.bawp.mytodolist.tasks.Tasks;

public class TasksAsync extends AsyncTask<String , Void , String> {

    Context context;
    //private ProgressDialog progressDialog;


    public TasksAsync(Context context ) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        //progressDialog= new ProgressDialog(context);
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        String method = params[0];

        TaskDBHelper dbHelper = new TaskDBHelper(context);

        if (method.equals("add_data")){
            String task = params[1];
            String task_details = params[2];

            Tasks tasks = new Tasks(task, task_details);

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            dbHelper.saveNewTask(db,tasks);
            return "one row inserted";
        }
        if (method.equals("edit")){

            String task_id;
            task_id = params[1];
            String task = params[2];
            String task_details = params[3];

            Tasks tasks = new Tasks(task, task_details);

            //Tasks tasks = new Tasks();

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            dbHelper.updateTaskRecord(db, Long.parseLong(task_id),tasks);
            return "one row updated";
        }

        if (method.equals("delete")){

            String task_id;
            task_id = params[1];

            //Tasks tasks = new Tasks();

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            dbHelper.deleteTaskRecord(db, Long.parseLong((task_id)));
            return "one row deleted";
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {

        Toast.makeText(context,result,Toast.LENGTH_SHORT).show();

    }
}
