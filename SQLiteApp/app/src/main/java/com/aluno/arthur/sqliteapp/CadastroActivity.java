package com.aluno.arthur.sqliteapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CadastroActivity extends AppCompatActivity {

    private EditText titulo;
    private EditText autor;
    private EditText editora;
    private DbControler dbc;
    private int livro_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        dbc = new DbControler(this);

        titulo = findViewById(R.id.input_titulo);
        autor = findViewById(R.id.input_autor);
        editora = findViewById(R.id.input_editora);

        Intent i = getIntent();
        livro_id = i.getIntExtra("LIVRO_ID", -1);

        if(livro_id >= 0){
            Livro livro = dbc.getLivro(livro_id);
            titulo.setText(livro.getTitulo());
            autor.setText(livro.getAutor());
            editora.setText(livro.getEditora());
        }
    }

    public void registrar(View view){
        if(livro_id >= 0){
            Livro livro = new Livro();
            livro.setId(livro_id);
            livro.setTitulo(titulo.getText().toString());
            livro.setAutor(autor.getText().toString());
            livro.setEditora( editora.getText().toString());
            Toast.makeText(this,dbc.update(livro), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, dbc.insereDados(
                titulo.getText().toString(),
                autor.getText().toString(),
                editora.getText().toString()
            ), Toast.LENGTH_LONG).show();
        }

        finish();
    }
}
