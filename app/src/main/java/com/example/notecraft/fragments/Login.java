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
import android.widget.TextView;
import android.widget.Toast;

import com.example.notecraft.MainActivity;
import com.example.notecraft.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends Fragment {
    private static final String NETWORK_ISSUE = "Could not be signed in.Try again later";
    private static final String EMAIL_NOT_VERIFIED_MSG = "Email not verified.Please Verify Email!!";
    private EditText emailEt, passwordEt;
    private Button loginButton;
    private FirebaseAuth auth;
    private MainActivity activity;
    private TextView dhasu;
    private static final String EMAIL_EMPTY_MSG = "Email field cannot empty";
    private static final String PASSWORD_EMPTY_MSG = "Password field cannot be empty";
    private static final String INVALID_EMAIL_PASS_MSG = "Invalid Email/Password";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        init(root);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEt.getText().toString();
                String password = passwordEt.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(getContext(), EMAIL_EMPTY_MSG, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty()) {
                    Toast.makeText(getContext(), PASSWORD_EMPTY_MSG, Toast.LENGTH_SHORT).show();
                    return;
                }

                loginUser(email, password);
            }
        });
        dhasu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.addFragment(new SignUp());
            }
        });
        return root;
    }

    private void loginUser(final String email, final String password) {
        Log.i("Login User", "Called");
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null && user.isEmailVerified())
                        activity.addFragment(new MainPage());
                    else if (user != null)
                        Toast.makeText(getContext(), EMAIL_NOT_VERIFIED_MSG, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), INVALID_EMAIL_PASS_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init(View root) {
        emailEt = root.findViewById(R.id.email_et);
        passwordEt = root.findViewById(R.id.password_et);
        loginButton = root.findViewById(R.id.login_button);
        dhasu = root.findViewById(R.id.dhasu);
        auth = FirebaseAuth.getInstance();
    }
}
