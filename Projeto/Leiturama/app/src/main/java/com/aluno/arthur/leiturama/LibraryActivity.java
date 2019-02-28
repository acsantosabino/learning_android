package com.aluno.arthur.leiturama;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.aluno.arthur.leiturama.models.Book;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LibraryActivity extends AppCompatActivity {

    OnSuccessListener<DocumentSnapshot> listener = new OnSuccessListener<DocumentSnapshot>() {
        @Override
        public void onSuccess(DocumentSnapshot o) {
            Log.w("","..............");
            Log.w("", o.getId());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        FBLoader.fbFirestore.collection("books").get().addOnSuccessListener(listener);
    }


}
