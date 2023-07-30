package com.example.notecraft.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notecraft.R;
import com.example.notecraft.fragments.MainPage;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
    public interface rvListener {
        void onClick(Note note);

        void onDelete(Note note);
    }

    private rvListener listener;
    private ArrayList<Note> list;

    public NoteAdapter(ArrayList<Note> list, Fragment context) {
        this.list = list;
        listener = (rvListener) context;
    }

    @NonNull
    @Override
    public NoteAdapter.NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_note, parent, false);
        return new NoteHolder(view, listener);

    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteHolder holder, int position) {

        String description = list.get(position).getDescription();
        long time = list.get(position).getTimeStamp();
        String date = DateExtraction.generateDateString(time);
        holder.descriptionView.setText(description);
        holder.timeView.setText(date);
    }

    public void setDataSet(ArrayList<Note> dataSet) {
        this.list = dataSet;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        public TextView descriptionView, timeView;
        public rvListener listener;

        public NoteHolder(@NonNull View itemView, rvListener listener) {
            super(itemView);
            descriptionView = itemView.findViewById(R.id.descript_tv);
            timeView = itemView.findViewById(R.id.time_tv);
            this.listener = listener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(list.get(getAdapterPosition()));
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onDelete(list.get(getAdapterPosition()));
            return true;
        }
    }
}