package com.janba.personalnotes;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditNoteActivity extends AppCompatActivity {
    EditText editTitle, editContent;
    Button btnUpdate, btnDelete;
    NoteDAO noteDAO;
    int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        noteDAO = new NoteDAO(this);

        noteId = getIntent().getIntExtra("id", -1);
        editTitle.setText(getIntent().getStringExtra("title"));
        editContent.setText(getIntent().getStringExtra("content"));

        btnUpdate.setOnClickListener(v -> {
            if (noteDAO.updateNote(noteId, editTitle.getText().toString(), editContent.getText().toString())) {
                Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error updating note", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(v -> {
            if (noteDAO.deleteNote(noteId)) {
                Toast.makeText(this, "Note Deleted", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error deleting note", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

