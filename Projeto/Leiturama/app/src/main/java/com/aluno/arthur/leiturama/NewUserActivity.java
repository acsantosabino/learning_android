package com.aluno.arthur.leiturama;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.aluno.arthur.leiturama.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewUserActivity extends AppCompatActivity {

    private static final String TAG = "NEW USER";
    private TextInputEditText mName, mPhone, mPassword;
    private AutoCompleteTextView mEmail;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        mName = findViewById(R.id.user_name);
        mEmail = findViewById(R.id.user_email);
        mPhone = findViewById(R.id.user_phone);
        mPassword = findViewById(R.id.user_password);

        Intent i = getIntent();
        mEmail.setText(i.getStringExtra(Intent.EXTRA_EMAIL));
        mAuth = FBLoader.fbAuth;
        db  = FBLoader.fbFirestore;

        readUser();
    }


    private void readUser(){
        final FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            Button btn = findViewById(R.id.user_btn);
            btn.setText(R.string.save_btn);
            findViewById(R.id.user_password_group).setVisibility(View.INVISIBLE);
            FBLoader.fbFirestore.collection("user").document(user.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User u = documentSnapshot.toObject(User.class);
                    mName.setText(u.getName());
                    mPhone.setText(u.getPhone());
                    mEmail.setText(u.getEmail());
                }
            });
        }
    }

    private void singUp(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        addUser(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(NewUserActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                        goToNext(null);
                    }

                }
            });
    }

    private void addUser(final FirebaseUser user) {
        User u = new User();
        u.setId(user.getUid());
        u.setEmail(user.getEmail());
        u.setName(mName.getText().toString());
        u.setPhone(mPhone.getText().toString());
        u.setN_borrowed(0);
        u.setN_lent(0);
        u.setN_read(0);

        db.collection("user").document(user.getUid()).set(u)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + user.getUid());
                    goToNext(user);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error adding document", e);
                }
            });
    }

    private void goToNext(FirebaseUser user){
        if(user == null){
            Toast.makeText(NewUserActivity.this, "não registrado",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(NewUserActivity.this, user.getEmail() + " registrado",Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, PerfilActivity.class);
            startActivity(i);
        }
    }

    public void save(View view){
        String email = mEmail.getText().toString();
        String pass = mPassword.getText().toString();


        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null) {
            this.singUp(email, pass);
        }
        else {
            this.addUser(user);
        }
    }

}
