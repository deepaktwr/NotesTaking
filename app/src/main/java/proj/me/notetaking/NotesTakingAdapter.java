package proj.me.notetaking;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import proj.me.entity.Note;

/**
 * Created by root on 23/1/18.
 */

public class NotesTakingAdapter extends RecyclerView.Adapter<NotesTakingAdapter.ViewHolder>{

    private List<Note> noteList;
    private LayoutInflater inflater;
    public NotesTakingAdapter(Context context, List<Note> noteList){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.noteList = noteList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.note_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.itemView.setTag(note.getId());
        holder.title.setText(note.getTitle());
        holder.text.setText(note.getText());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title;
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            text = (TextView) itemView.findViewById(R.id.text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int noteId = (int) view.getTag();
            Intent intent = new Intent(view.getContext(), NoteActivity.class);
            intent.putExtra(NoteActivity.NOTE_ID_BUNDLE_KEY, noteId);
            view.getContext().startActivity(intent);
        }
    }

}
