package com.aluno.arthur.taskapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    public void logout(View view) {
        SessionHandler.saveToken("",this);
        finish();
    }

}
