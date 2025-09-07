package com.janba.personalnotes;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {

    private EditText etTitle, etContent;
    private ImageButton btnBack, btnMenu, btnText, btnCheck, btnMic, btnImage, btnSave;
    private NotesDatabaseHelper dbHelper;
    private int noteId = -1; // default - new note

    // Launcher for speech recognition
    private final ActivityResultLauncher<Intent> speechLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    ArrayList<String> matches = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (matches != null && !matches.isEmpty()) {
                        etContent.append(" " + matches.get(0));
                        etContent.setSelection(etContent.getText().length());
                    }
                }
            });

    // Launcher for image picker
    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    etContent.append("\n[Image: " + uri.toString() + "]\n");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // Initialize views safely
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);

        btnBack = findViewById(R.id.btnBack);
        btnMenu = findViewById(R.id.btnMenu);
        btnMic = findViewById(R.id.btnMic);
        btnSave = findViewById(R.id.btnSave);

        dbHelper = new NotesDatabaseHelper(this);

        // Check if editing existing note
        noteId = getIntent().getIntExtra("noteId", -1);
        if (noteId != -1) {
            Note note = dbHelper.getNoteById(noteId);
            if (note != null) {
                etTitle.setText(note.getTitle());
                etContent.setText(note.getContent());
            }
        }

        // Back button → finish
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Menu button → delete
        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> showDeleteDialog());
        }

        // Bottom bar buttons
        if (btnText != null) {
            btnText.setOnClickListener(v -> etContent.append(" ✍ "));
        }

        if (btnCheck != null) {
            btnCheck.setOnClickListener(v -> {
                etContent.append("\n☑ ");
                etContent.setSelection(etContent.getText().length());
            });
        }

        if (btnMic != null) {
            btnMic.setOnClickListener(v -> startSpeechToText());
        }


        if (btnSave != null) {
            btnSave.setOnClickListener(v -> {
                saveOrUpdateNote();
                finish();
            });
        }
    }

    private void saveOrUpdateNote() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        if (title.isEmpty() && content.isEmpty()) {
            Toast.makeText(this, "Cannot save empty note", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success;
        if (noteId == -1) {
            success = dbHelper.insertNote(title, content);
            if (success) Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        } else {
            success = dbHelper.updateNote(noteId, title, content);
            if (success) Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteDialog() {
        if (noteId == -1) {
            Toast.makeText(this, "Nothing to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    boolean deleted = dbHelper.deleteNote(noteId);
                    if (deleted) {
                        Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");
        speechLauncher.launch(intent);
    }
}
