package com.example.notecraft.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notecraft.R;
import com.example.notecraft.models.Note;
import com.example.notecraft.models.UserCred;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Page extends Fragment {

    private EditText descriptEt;
    private Button buttonBack, buttonCheck;
    private DatabaseReference reference;
    private Note note = null;
    private String key = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_page, container, false);
        init(root);
        Bundle args = getArguments();
        if (args != null) {
            note = (Note) args.getSerializable("data");
            descriptEt.setText(note.getDescription());
            key = note.getId();
            if(key==null) Log.i("Key is : "," null");
        }
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager()
                        .popBackStack();
            }
        });
        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

;                final String description = descriptEt.getText().toString();
                if (description.isEmpty()) return;

                if(key==null)
                    key = reference.push().getKey();
                Log.i("Key : ",key);
                Note note = new Note(description, key,System.currentTimeMillis() / 1000);
                reference.child(key).setValue(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.e("Page ", "Clicked java");
                            requireActivity().getSupportFragmentManager()
                                    .popBackStack();
                        }

                        else {

                            Toast.makeText(requireActivity(), "Something error happened", Toast.LENGTH_SHORT).show();
                             }
                        }
                });
            }
        });
        return root;
    }

    private void init(final View root) {
        reference = FirebaseDatabase.getInstance().getReference
                (FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ""));
        descriptEt = root.findViewById(R.id.description_et);
        buttonBack = root.findViewById(R.id.button_back);
        buttonCheck = root.findViewById(R.id.button_check);
    }

}