package com.aluno.arthur.leiturama;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FBLoader extends AppCompatActivity {

    public static int SPLASH_TIME_OUT = 4000;

    public static FirebaseAuth fbAuth;

    public static FirebaseFirestore fbFirestore;
    public static StorageReference  fbStorage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbloader);

        FirebaseApp.initializeApp(this);

        fbAuth = FirebaseAuth.getInstance();

        fbFirestore = FirebaseFirestore.getInstance();
        fbFirestore.setFirestoreSettings(new FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true).build());

        fbStorage = FirebaseStorage.getInstance().getReference();

        ImageView progress = findViewById(R.id.load_fb_progress);
        AnimationDrawable animationDrawable = (AnimationDrawable) progress.getBackground();
        progress.setVisibility(View.VISIBLE);
        animationDrawable.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashIntent = new Intent(FBLoader.this, LoginActivity.class);
                startActivity(splashIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
