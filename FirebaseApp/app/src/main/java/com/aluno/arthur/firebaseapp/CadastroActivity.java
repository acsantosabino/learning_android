package com.aluno.arthur.firebaseapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class CadastroActivity extends AppCompatActivity {

    private EditText titulo;
    private EditText autor;
    private EditText editora;
    private String livro_id;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        titulo = findViewById(R.id.input_titulo);
        autor = findViewById(R.id.input_autor);
        editora = findViewById(R.id.input_editora);

        Intent i = getIntent();
        livro_id = i.getStringExtra("LIVRO_ID");

        databaseReference = FirebaseDatabase.getInstance().getReference();

        if(livro_id != null){
            readBook();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.edit_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.action_menu_save: {
                registrar();
                finish();
                break;
            }
            default:break;
        }
        return true;
    }

    public void readBook(){
        databaseReference.child("Livros").child(livro_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Livro livro = dataSnapshot.getValue(Livro.class);
                titulo.setText(livro.getTitulo());
                autor.setText(livro.getAutor());
                editora.setText(livro.getEditora());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void registrar(){
        Livro livro = new Livro();
        livro.setTitulo(titulo.getText().toString());
        livro.setAutor(autor.getText().toString());
        livro.setEditora( editora.getText().toString());

        String msgToast = "";

        if(livro_id != null){
            livro.setId(livro_id);
            msgToast = "Alterado";
        } else {
            livro.setId(UUID.randomUUID().toString());
            msgToast = "Adicionado";
        }
        databaseReference.child("Livros").child(livro.getId()).setValue(livro);
        Toast.makeText(CadastroActivity.this,msgToast,Toast.LENGTH_LONG).show();
        finish();
    }
}
