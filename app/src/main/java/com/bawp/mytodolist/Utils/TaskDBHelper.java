package com.bawp.mytodolist.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.widget.Toast;


import com.bawp.mytodolist.tasks.Tasks;

import java.util.LinkedList;
import java.util.List;

public class TaskDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "todo.db";
    private static final int DATABASE_VERSION = 3 ;
    public static final String TABLE_NAME = "Task";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TASK_NAME = "taskTitle";
    public static final String COLUMN_TASK_DESCRIPTION = "taskDescription";


    public TaskDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(" CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TASK_NAME + " TEXT NOT NULL, " +
                COLUMN_TASK_DESCRIPTION + " TEXT NOT NULL);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
    }
    public void saveNewTask(SQLiteDatabase db,Tasks tasks) {

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, tasks.getTaskTitle());
        values.put(COLUMN_TASK_DESCRIPTION, tasks.getTaskDetails());
        db.insert(TABLE_NAME,null, values);
        db.close();
    }

    /**Query records, give options to filter results**/
    public List<Tasks> tasksList(String filter) {
        String query;
        if(filter.equals("")){
            query = "SELECT  * FROM " + TABLE_NAME;
        }else{
            query = "SELECT  * FROM " + TABLE_NAME + " ORDER BY "+ filter;
        }

        List<Tasks> tasksLinkedList = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Tasks tasks;

        if (cursor.moveToFirst()) {
            do {
                tasks = new Tasks();

                tasks.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                tasks.setTaskTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TASK_NAME)));
                tasks.setTaskDetails(cursor.getString(cursor.getColumnIndex(COLUMN_TASK_DESCRIPTION)));
                tasksLinkedList.add(tasks);
            } while (cursor.moveToNext());
        }


        return tasksLinkedList;
    }

    public Tasks getTask(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_NAME + " WHERE _id="+ id;
        Cursor cursor = db.rawQuery(query, null);

        Tasks recievedTasks = new Tasks();
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            recievedTasks.setTaskTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TASK_NAME)));
            recievedTasks.setTaskDetails(cursor.getString(cursor.getColumnIndex(COLUMN_TASK_DESCRIPTION)));
        }



        return recievedTasks;


    }


    /*delete record*/
    public void deleteTaskRecord(SQLiteDatabase db,long id) {
         db = this.getWritableDatabase();

        db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE _id='"+id+"'");

    }

    /*update record*/
    public void updateTaskRecord(SQLiteDatabase db,long taskId, Tasks updatedTasks) {
        db = this.getWritableDatabase();
        db.execSQL("UPDATE  "+TABLE_NAME+" SET taskTitle ='"+ updatedTasks.getTaskTitle() + "', taskDescription ='" + updatedTasks.getTaskDetails()+ "'  WHERE _id='" + taskId+ "'");
        //Toast.makeText(context, "Updated successfully.", Toast.LENGTH_SHORT).show();
    }

}
