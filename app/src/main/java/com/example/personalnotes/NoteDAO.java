package com.example.personalnotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class NoteDAO {
    private DatabaseHelper dbHelper;

    public NoteDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public boolean insertNote(String title, String content) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_TITLE, title);
        values.put(DatabaseHelper.COL_CONTENT, content);
        long result = db.insert(DatabaseHelper.TABLE_NAME, null, values);
        return result != -1;
    }

    public ArrayList<String> getAllNotes() {
        ArrayList<String> notes = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TITLE));
                notes.add(title);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return notes;
    }
}

