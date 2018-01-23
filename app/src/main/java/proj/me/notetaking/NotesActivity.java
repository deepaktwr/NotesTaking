package proj.me.notetaking;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import proj.me.data.executor.SingleJobExecutor;
import proj.me.data.loader.NotesLoaderImpl;
import proj.me.data.repository.NotesRepositoryImpl;
import proj.me.entity.Note;
import proj.me.presentation.loader.LoaderCallback;
import proj.me.presentation.loader.LoaderInitializer;
import proj.me.presentation.loader.LoaderPresentor;
import proj.me.presentation.loader.LoaderPresentorImpl;
import proj.me.presentation.usecase.NotesPresentor;
import proj.me.presentation.usecase.NotesPresentorImpl;
import proj.me.usecase.exception.ExceptionBundle;
import proj.me.usecase.executor.BackgroundExecutor;
import proj.me.usecase.executor.ForegroundExecutor;
import proj.me.usecase.repository.NotesInteractor;
import proj.me.usecase.repository.NotesInteractorImpl;
import proj.me.usecase.repository.NotesRepository;

public class NotesActivity extends BaseActivityWithLoader implements LoaderCallback.Notes{

    private List<Note> noteList;
    private NotesTakingAdapter notesTakingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NoteActivity.class);
                view.getContext().startActivity(intent);
            }
        });


        notesTakingAdapter = new NotesTakingAdapter(this, noteList);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.notes_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(notesTakingAdapter);
    }

    @Override
    protected void initializeLoader() {
        noteList = new ArrayList<>();
        LoaderInitializer loaderInitializer = new NotesLoaderImpl(context, this);
        loaderPresentor = new LoaderPresentorImpl();
        loaderPresentor.initializeTypeLoader(loaderInitializer);
    }

    @Override
    public void initLoaderWith(int loaderId) {
        getLoaderManager().initLoader(loaderId, null, this);
    }

    @Override
    protected void initializePresentor() {
        // do nothing
    }

    @Override
    public void notesDetailsChanged(List<Note> notes) {
        noteList.clear();
        noteList.addAll(notes);
        notesTakingAdapter.notifyDataSetChanged();

        findViewById(R.id.empty).setVisibility(notes.size() == 0 ? View.VISIBLE : View.GONE);
    }
}
