package com.aluno.arthur.lista2;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PokemonAdapter extends BaseAdapter {

    private final List<Pokemon> pokemoms;
    private final Activity activity;

    public PokemonAdapter(List<Pokemon> pokemoms, Activity activity) {
        this.pokemoms = pokemoms;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return pokemoms.size();
    }

    @Override
    public Object getItem(int position) {
        return pokemoms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = activity.getLayoutInflater()
                .inflate(R.layout.item_pokemon_list,parent,false);
        Pokemon pokemom = pokemoms.get(position);

        TextView nome = (TextView) view.findViewById(R.id.nome);
        TextView numero = (TextView) view.findViewById(R.id.numero);
        TextView tipo = (TextView) view.findViewById(R.id.tipo);
        ImageView imagem = (ImageView) view.findViewById(R.id.image_lista);

        nome.setText(pokemom.getNome());
        numero.setText("Nº: " + pokemom.getNum());
        tipo.setText(pokemom.getTipo());
        imagem.setImageResource(pokemom.getImagem());
        return view;
    }
}
