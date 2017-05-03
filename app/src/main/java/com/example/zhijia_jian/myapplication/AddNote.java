package com.example.zhijia_jian.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

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
    private NotesAdapter notesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        //setUpViews();
        addNoteButton = (Button)findViewById(R.id.buttonAdd);
        editText = (EditText) findViewById(R.id.titleET);
        texteditText = (EditText) findViewById(R.id.textET);
//noteId=-1;

        //ISedit=(noteId==-1)?false:true;

        // get the note DAO


        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        noteDao = daoSession.getNoteDao();

        // query all notes, sorted a-z by their text
        notesQuery = noteDao.queryBuilder().orderAsc(NoteDao.Properties.Text).build();
        //updateNotes();

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
    private void updateNotes() {
        List<Note> notes = notesQuery.list();
        notesAdapter.setNotes(notes);
    }

//    protected void setUpViews() {
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewNotes);
//        //noinspection ConstantConditions
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        notesAdapter = new NotesAdapter(noteClickListener);
//        recyclerView.setAdapter(notesAdapter);
//
//        addNoteButton = findViewById(R.id.buttonAdd);
//        //noinspection ConstantConditions
//        addNoteButton.setEnabled(false);
//
//        editText = (EditText) findViewById(R.id.titleET);
//        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    addNote();
//                    return true;
//                }
//                return false;
//            }
//        });
//        editText.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                boolean enable = s.length() != 0;
//                addNoteButton.setEnabled(enable);
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//        texteditText = (EditText) findViewById(R.id.textET);
//    }

    public void onAddButtonClick(View view) {
        if(noteId==-1)
            addNote();
        else
            editNote();
        Intent intent = new Intent();
        intent.setClass(AddNote.this , MainActivity.class);
        //Bundle bun=new Bundle();
        //bun.putFloat("BMI",fresult);
        //intent.putExtras(bun);
        //intent.putExtra("BMI",fw/fh);
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

        //updateNotes();
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
        String comment = "Edited on " + df.format(new Date())+"\n"+note.getComment();
        note.setComment(comment);
        note.setDate(new Date());
        note.setText(textnoteText);
        noteDao.update(note);
    }

//    NotesAdapter.NoteClickListener noteClickListener = new NotesAdapter.NoteClickListener() {
//        @Override
//        public void onNoteClick(int position) {
//            Note note = notesAdapter.getNote(position);
//            Long noteId = note.getId();
//
//            noteDao.deleteByKey(noteId);
//            Log.d("DaoExample", "Deleted note, ID: " + noteId);
//
//            updateNotes();
//        }
//    };
}
