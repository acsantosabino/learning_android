package com.aluno.arthur.lista2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        TextView nome = findViewById(R.id.nome_detalhe);
        TextView num = findViewById(R.id.numero_detalhe);
        TextView descricao = findViewById(R.id.descricao_detalhe);
        TextView tipo = findViewById(R.id.tipo_detalhe);
        ImageView img = findViewById(R.id.image_detalhe);
        Intent i = getIntent();
        Pokemon pokemom = (Pokemon) i.getSerializableExtra("POKEMON");
        nome.setText(pokemom.getNome());
        num.setText("Nº "+pokemom.getNum());
        descricao.setText(pokemom.getDescricao());
        tipo.setText(pokemom.getTipo());
        img.setImageResource(pokemom.getImagem());
    }
}
