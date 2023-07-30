package com.example.notecraft.models;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserCred {
    private static UserCred cred = null;
    private FirebaseUser user;
    private DatabaseReference reference;
    private UserCred() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String userEmail = user.getEmail();
        String emailKey = userEmail.replace(".", "");
        reference = FirebaseDatabase.getInstance().getReference().child("Users/" + emailKey);
    }
    public static UserCred getInstance() {
        if (cred == null) cred = new UserCred();
        return cred;
    }
    public FirebaseUser getUser() {
        return user;
    }
    public DatabaseReference getReference() {
        return reference;
    }
}
