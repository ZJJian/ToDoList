package com.example.zhijia_jian.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;


import org.greenrobot.greendao.query.Query;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class AddNote extends AppCompatActivity {

    private EditText editText;
    private EditText texteditText;
    private Button addNoteButton;

    private Long noteId;

    private NoteDao noteDao;
    private Query<Note> notesQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);


        addNoteButton = (Button)findViewById(R.id.buttonAdd);
        editText = (EditText) findViewById(R.id.titleET);
        texteditText = (EditText) findViewById(R.id.textET);


        // get the note DAO


        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        noteDao = daoSession.getNoteDao();

        // query all notes, sorted a-z by their text
        notesQuery = noteDao.queryBuilder().orderAsc(NoteDao.Properties.Text).build();


        Bundle bun = this.getIntent().getExtras();
        noteId=bun.getLong("note");
        if(noteId!=-1) {
            List<Note> noteL = noteDao.queryBuilder().where(NoteDao.Properties.Id.eq(noteId)).list();
            Note note = noteL.get(0);
            editText.setText(note.getTitle());
            texteditText.setText(note.getText());
            addNoteButton.setText("Update");
        }

    }



    public void onAddButtonClick(View view) {
        if(noteId==-1)
            addNote();
        else
            editNote();
        Intent intent = new Intent();
        intent.setClass(AddNote.this , MainActivity.class);
        startActivity(intent);
    }

    private void addNote() {
        String noteText = editText.getText().toString();
        editText.setText("");
        String textnoteText = texteditText.getText().toString();
        texteditText.setText("");

        final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "Added on " + df.format(new Date());

        Note note = new Note();
        note.setTitle(noteText);
        note.setComment(comment);
        note.setDate(new Date());
        note.setText(textnoteText);
        noteDao.insert(note);
        Log.d("DaoExample", "Inserted new note, ID: " + note.getId());

    }
    private void editNote() {
        String noteText = editText.getText().toString();
        editText.setText("");
        String textnoteText = texteditText.getText().toString();
        texteditText.setText("");


        List<Note> noteL = noteDao.queryBuilder().where(NoteDao.Properties.Id.eq(noteId)).list();
        Note note = noteL.get(0);
        note.setTitle(noteText);
        final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "Edited on " + df.format(new Date());
        note.setComment(comment);
        note.setDate(new Date());
        note.setText(textnoteText);
        noteDao.update(note);
    }

}
