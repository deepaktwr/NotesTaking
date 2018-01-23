package proj.me.notetaking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import proj.me.data.executor.SingleJobExecutor;
import proj.me.data.repository.NoteRepositoryImpl;
import proj.me.entity.Note;
import proj.me.presentation.usecase.NotePresentor;
import proj.me.presentation.usecase.NotePresentorImpl;
import proj.me.usecase.exception.ExceptionBundle;
import proj.me.usecase.executor.BackgroundExecutor;
import proj.me.usecase.executor.ForegroundExecutor;
import proj.me.usecase.repository.NoteInteractor;
import proj.me.usecase.repository.NoteInteractorImpl;
import proj.me.usecase.repository.NoteRepository;

/**
 * Created by root on 23/1/18.
 */

public class NoteActivity extends BaseActivityWithLoader implements View.OnClickListener, NotePresentor.Callback {
    public static final String NOTE_ID_BUNDLE_KEY = "note_id";

    private Button saveOrUpdate;
    private TextView createdTime, updatedTime;
    private EditText title, text;
    private int noteId = -1;
    private NotePresentor notePresentor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_details);

        saveOrUpdate = (Button) findViewById(R.id.save_or_update);
        createdTime = (TextView) findViewById(R.id.create_time);
        updatedTime = (TextView) findViewById(R.id.update_time);
        title = (EditText) findViewById(R.id.title);
        text = (EditText) findViewById(R.id.text);

        if(getIntent().getExtras() != null && getIntent().getExtras().containsKey(NOTE_ID_BUNDLE_KEY)) noteId = getIntent().getExtras().getInt(NOTE_ID_BUNDLE_KEY);

        if(noteId != -1){
            saveOrUpdate.setText("UPDATE");
            notePresentor.fetchNote(noteId);
        } else{
            createdTime.setVisibility(View.GONE);
            updatedTime.setVisibility(View.GONE);
            saveOrUpdate.setOnClickListener(this);
            saveOrUpdate.setText("SAVE");
        }

    }

    @Override
    protected void initializeLoader() {
        //do nothing
    }

    @Override
    protected void initializePresentor() {
        BackgroundExecutor backgroundExecutor = SingleJobExecutor.getInstance();
        ForegroundExecutor foregroundExecutor = UIThread.getInstance();
        NoteRepository noteRepository = new NoteRepositoryImpl(this);
        NoteInteractor noteInteractor = new NoteInteractorImpl(noteRepository, backgroundExecutor, foregroundExecutor);
        notePresentor = new NotePresentorImpl(noteInteractor, this);
    }

    @Override
    public void onClick(View view) {
        if(TextUtils.isEmpty(title.getText()) || TextUtils.isEmpty(text.getText())){
            Toast.makeText(view.getContext(), "Fields should not be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        Note note = new Note();
        note.setTitle(title.getText().toString());
        note.setText(text.getText().toString());

        if(noteId != -1){
            note.setId(noteId);
            notePresentor.updateNote(note);
        } else  notePresentor.createNote(note);

        view.setOnClickListener(null);
    }

    @Override
    public void noteResultSuccess(Note note, int type) {
        saveOrUpdate.setOnClickListener(this);

        switch(type){
            case NoteInteractor.EXECUTION_TYPE_FETCH:
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(note.getCreateTimestamp());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
                createdTime.setText(simpleDateFormat.format(calendar.getTime()));
                if(note.getUpdateTimestamp() != 0) {
                    calendar.setTimeInMillis(note.getUpdateTimestamp());
                    updatedTime.setText(simpleDateFormat.format(calendar.getTime()));
                }

                title.setText(note.getTitle());
                text.setText(note.getText());
                break;
            case NoteInteractor.EXECUTION_TYPE_CREATE:
            case NoteInteractor.EXECUTION_TYPE_DELETE:
            case NoteInteractor.EXECUTION_TYPE_UPDATE:
                showMessageAndExit("Done!!");
                finish();
                break;
        }

    }

    @Override
    public void noteResultFailed(ExceptionBundle exceptionBundle, int type) {
        showMessageAndExit("Error Occurred");
        saveOrUpdate.setOnClickListener(this);
    }

    private void showMessageAndExit(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startLoading() {}
    @Override
    public void finishLoading() {}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(noteId == -1) return false;
        getMenuInflater().inflate(R.menu.menu_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.delete:
                notePresentor.deleteNote(noteId);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
