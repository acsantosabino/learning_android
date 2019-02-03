package com.aluno.arthur.lista1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        TextView nome = findViewById(R.id.text2);
        Intent i = getIntent();
        String pokemom = i.getStringExtra("POKEMON");
        nome.setText(pokemom);
    }
}
