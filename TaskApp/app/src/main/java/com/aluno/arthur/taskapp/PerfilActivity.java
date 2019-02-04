package com.aluno.arthur.taskapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Base64;

public class PerfilActivity extends AppCompatActivity {

    private Usuario user;
    private ImageView imageView;
    private TextView nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        Intent i = getIntent();
        this.user = (Usuario) i.getSerializableExtra("USER");

        imageView = findViewById(R.id.perfil_img);
        ImgDownload downloadTask = new ImgDownload(user.getFotoUrl(),this, imageView);
        downloadTask.execute();

        nome = findViewById(R.id.perfil_name);
        nome.setText(this.user.getNome());

    }

}
