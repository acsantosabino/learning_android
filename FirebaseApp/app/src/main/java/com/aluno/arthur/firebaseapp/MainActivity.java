package com.aluno.arthur.firebaseapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private ListView lista;
    private ArrayList<Livro> livros;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializaFirebase();
        livros = new ArrayList<>();

        lista = (ListView) findViewById(R.id.livros_list);
        listarDados();


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Você clicou em: " + livros.get(position).getTitulo(), Toast.LENGTH_LONG).show();
                goToCadastroActivity(livros.get(position).getId());

            }
        });

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("DELETE");
                alert.setMessage("Você deseja deletar o item?");
                alert.setPositiveButton("sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Livro l = livros.get(position);
                        databaseReference.child("Livros").child(l.getId()).removeValue();
                        Toast.makeText(MainActivity.this,"Registro Excluído",Toast.LENGTH_LONG).show();
                        listarDados();
                    }
                });
                alert.setNegativeButton("não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();
                return true;
            }
        });


    }



    private void listarDados() {
        databaseReference.child("Livros").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                livros.clear();
                for(DataSnapshot objSnapshot: dataSnapshot.getChildren()){
                    Livro p = objSnapshot.getValue(Livro.class);
                    livros.add(p);
                }

                LivroAdapter adaptador = new LivroAdapter(MainActivity.this, livros);
                lista.setAdapter(adaptador);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void inicializaFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseDatabase.setPersistenceEnabled(true); //persistencia
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.list_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.action_menu_add: {
                Intent i = new Intent(MainActivity.this, CadastroActivity.class);
                startActivity(i);
                break;
            }
            default:break;
        }
        return true;
    }

    public void goToCadastroActivity(String id){
        Intent i = new Intent(this, CadastroActivity.class);
        i.putExtra("LIVRO_ID",id);
        startActivity(i);
    }
}
