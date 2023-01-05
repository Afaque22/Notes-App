package com.example.yourscheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Editable;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NotesDatabase";
    private static final int VERSION = 6;
    private static final String TABLE_NAME = "tbl_notes";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_NOTE = "note";




    public DatabaseManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " TEXT,"
                + COL_NOTE + " TEXT)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    public String addNotes(String title, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, title);
        cv.put(COL_NOTE, note);
        float res = db.insert(TABLE_NAME, null, cv);
        if (res == -1)
            return "Failed";
        else
            return "Added Sucessfully";
    }


    public ArrayList<ModelConstructors> fetchData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        ArrayList<ModelConstructors> arrModel = new ArrayList<>();

        while (cursor.moveToNext()) {
            ModelConstructors model = new ModelConstructors(cursor.getString(1), cursor.getString(2));
            arrModel.add(model);

        }
        return arrModel;
    }

    public String updateData(String originaltitle, String title, String note) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, title);
        cv.put(COL_NOTE, note);
        long res = db.update(TABLE_NAME, cv, "title=?", new String[]{originaltitle});
        if (res == -1) {
            return "Failed";
        } else
            return "Update Successfully";
    }

   public String deleteData(String title){
        SQLiteDatabase db = this.getWritableDatabase();
        long res = db.delete(TABLE_NAME, "title=?", new String[]{title});
        db.close();
        if (res == -1){
            return "Failed";
        }else
            return "Item Deleted";

   }


}