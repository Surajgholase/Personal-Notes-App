package com.janba.personalnotes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnNewNote, btnViewNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNewNote = findViewById(R.id.btnNewNote);
        btnViewNotes = findViewById(R.id.btnViewNotes);

        // Open Add Note page
        btnNewNote.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this,  AddNoteActivity.class)));

        // Open Notes List page
        btnViewNotes.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, NotesListActivity.class)));
    }
}
