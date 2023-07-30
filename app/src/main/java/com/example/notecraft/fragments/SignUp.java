package com.example.notecraft.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notecraft.MainActivity;
import com.example.notecraft.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends Fragment {

    private EditText emailEt, passwordEt, confPassEt;
    private Button signUpButton;
    private static final String EMAIL_MSG_EMPTY = "Email field cannot be empty";
    private static final String PASSWORD_MSG_EMPTY = "Password field cannot be empty";
    private static final String PASS_NOT_MATCH_MSG = "Passwords do not match";
    private static final String USER_EXIST_MSG = "User already exist";
    private FirebaseAuth auth;
    private static final String VERIFICATION_PROBLEM_MSG = "Problem with verification mail";
    private MainActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_sign_up, container, false);
        init(root);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEt.getText().toString();
                String password = passwordEt.getText().toString();
                String confPassword = confPassEt.getText().toString();

                if (email.isEmpty()) {
                    Toast.makeText(getContext(), EMAIL_MSG_EMPTY, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty() || confPassword.isEmpty()) {
                    Toast.makeText(getContext(), PASSWORD_MSG_EMPTY, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(confPassword)) {
                    Toast.makeText(getContext(), PASS_NOT_MATCH_MSG, Toast.LENGTH_SHORT).show();
                    return;
                }
                registerUser(email, password);
            }
        });
        return root;
    }

    private void registerUser(final String email, final String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    Log.i("Signup Fragment","User Created");
                    if (user != null) sendVerificationMail(user);
                } else {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) user.delete();
                    Toast.makeText(getContext(), USER_EXIST_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendVerificationMail(FirebaseUser user) {
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    activity.addFragment(new Login());
                } else {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) user.delete();
                    Toast.makeText(getContext(), VERIFICATION_PROBLEM_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init(View root) {
        emailEt = root.findViewById(R.id.email_et);
        passwordEt = root.findViewById(R.id.password_et);
        confPassEt = root.findViewById(R.id.password_confirm_et);
        signUpButton = root.findViewById(R.id.signUp_button);
        auth = FirebaseAuth.getInstance();
    }
}