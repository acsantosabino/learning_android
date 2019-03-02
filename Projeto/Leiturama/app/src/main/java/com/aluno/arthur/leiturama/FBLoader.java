package com.aluno.arthur.leiturama;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.audiofx.DynamicsProcessing;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Config;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aluno.arthur.leiturama.models.Book;
import com.aluno.arthur.leiturama.services.ImgDownload;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.protobuf.compiler.PluginProtos;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FBLoader extends AppCompatActivity {

    public static FirebaseAuth fbAuth;

    public static FirebaseFirestore fbFirestore;
    public static StorageReference  fbStorage;
    public static File coversPath;
    public int totalBooks = 0;
    public int countBooks = 0;

    private TextView info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbloader);

        info = findViewById(R.id.load_info);
        FirebaseApp.initializeApp(this);

        fbAuth = FirebaseAuth.getInstance();

        fbFirestore = FirebaseFirestore.getInstance();
        fbFirestore.setFirestoreSettings(new FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true).build());

        fbStorage = FirebaseStorage.getInstance().getReference();

        coversPath = new File(getFilesDir(),"covers");
        if(!coversPath.exists()){
            coversPath.mkdir();
        }

        ImageView progress = findViewById(R.id.load_fb_progress);
        AnimationDrawable animationDrawable = (AnimationDrawable) progress.getBackground();
        animationDrawable.start();

        fbFirestore.collection("books").get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        totalBooks = task.getResult().size();
                        Log.d("BOOKS","Result size "+task.getResult().size());
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Book book = document.toObject(Book.class);
                            downloadCover(book);
                        }
                    }
                }
            });

    }

    private void downloadCover(Book book){
        countBooks += 1;
        if(book.getImagePath() != null) {
            Log.d("BOOKS","downloading");
            File img = new File(getFilesDir(),book.getImagePath());
            if(!img.exists()) {
                StorageReference coverRef = fbStorage.child("covers/" + book.getIsbn() + ".jpg");
                coverRef.getFile(img).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        info.setText(taskSnapshot.getStorage().getPath());
                        Log.d("BOOKS", "downloaded " + taskSnapshot.getBytesTransferred());
                        goNext();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        StorageException storageException = (StorageException) e;
                        Log.d("BOOKS STORAGE ERROR", storageException.getMessage());
                        Log.d("BOOKS STORAGE ERROR", "HTTP " + storageException.getHttpResultCode());
                        Log.d("BOOKS STORAGE ERROR", "Error Code " + storageException.getErrorCode());
                        Log.d("BOOKS STORAGE ERROR", "Error Cause " + storageException.getCause());
                    }
                });
            }
            else {
                Log.d("BOOKS","Count no path "+countBooks);
                goNext();
            }
        }
        else {
            Log.d("BOOKS","Count no path "+countBooks);
            goNext();
        }

    }

    private void goNext(){
        if(countBooks >= totalBooks) {
            Intent splashIntent = new Intent(FBLoader.this, LoginActivity.class);
            startActivity(splashIntent);
        }
    }
}
