package com.aluno.arthur.sqliteapp;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class LivroAdapter extends BaseAdapter {

    private ArrayList<Livro> livros;
    private Activity activity;

    public LivroAdapter(Activity activity, ArrayList<Livro> livros) {
        this.livros = livros;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return livros.size();
    }

    @Override
    public Object getItem(int position) {
        return livros.get(position);
    }

    @Override
    public long getItemId(int position) {
        return livros.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater()
            .inflate(R.layout.item_livro_layout,parent,false);
        Livro livro = livros.get(position);

        TextView titulo = (TextView) view.findViewById(R.id.item_titulo);
        TextView author = (TextView) view.findViewById(R.id.item_author);

        titulo.setText(livro.getTitulo());
        author.setText(livro.getAutor());

        return view;
    }
}
