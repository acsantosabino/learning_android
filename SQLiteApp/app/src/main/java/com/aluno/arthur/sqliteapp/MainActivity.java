package com.aluno.arthur.sqliteapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DbControler dbc;
    private ListView lista;
    private ArrayList<Livro> livros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbc = new DbControler(this);

        lista = (ListView) findViewById(R.id.livros_list);
        updateList();

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
                        dbc.delete(livros.get(position).getId());
                        updateList();
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

    @Override
    protected void onResume() {
        updateList();
        super.onResume();
    }

    private void updateList(){
        livros = dbc.getAll();
        LivroAdapter adaptador = new LivroAdapter(this, livros);
        lista.setAdapter(adaptador);
    }

    public void goToCadastroActivity(View view){
        Intent i = new Intent(this, CadastroActivity.class);
        startActivity(i);
    }
    public void goToCadastroActivity(int id){
        Intent i = new Intent(this, CadastroActivity.class);
        i.putExtra("LIVRO_ID",id);
        startActivity(i);
    }
}
