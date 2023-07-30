package com.example.notecraft.fragments;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.notecraft.R;
import com.example.notecraft.models.Note;
import com.example.notecraft.models.NoteAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainPage extends Fragment implements NoteAdapter.rvListener {

    private RecyclerView noteView;
    private NoteAdapter adapter;
    private ArrayList<Note> dataSet;
    private FloatingActionButton addNotes;
    private DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_page, container, false);
        init(root);
        addNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.container,
                                new Page()).addToBackStack("Note Add")
                        .commit();
            }
        });

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Note note = snapshot.getValue(Note.class);
                dataSet.add(0,note);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Note note = snapshot.getValue(Note.class);
                for (Note child : dataSet) {
                    if (child.getId().equals(note.getId())) {
                        dataSet.remove(child);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }

    private void init(View root) {
        dataSet = new ArrayList<>();
        noteView = root.findViewById(R.id.notes_view);
        addNotes = root.findViewById(R.id.add_notes);
        reference = FirebaseDatabase.getInstance().
                getReference(FirebaseAuth.getInstance().getCurrentUser().
                        getEmail().
                        replace(".", ""));
        adapter = new NoteAdapter(dataSet, this);
        noteView.setAdapter(adapter);
        noteView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onClick(Note note) {
        Page page = new Page();
        Bundle args = new Bundle();
        args.putSerializable("data", note);
        Log.i("key", note.getId());
        page.setArguments(args);
        requireActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.container,
                        page).addToBackStack("Note Add")
                .commit();
    }

    @Override
    public void onDelete(Note note) {
        Log.i("On Delete", "This note is deleted");
        reference.child(note.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}