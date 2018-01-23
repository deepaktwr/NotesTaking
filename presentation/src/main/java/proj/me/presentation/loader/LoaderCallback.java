package proj.me.presentation.loader;

import java.util.List;

import proj.me.entity.Note;

/**
 * Created by root on 23/1/18.
 */

public interface LoaderCallback {

    interface NoteDetails extends LoaderInitializer.Callback {
        void noteDetailsChanged(Note note);
    }

    interface Notes extends LoaderInitializer.Callback {
        void notesDetailsChanged(List<Note> notes);
    }

}
