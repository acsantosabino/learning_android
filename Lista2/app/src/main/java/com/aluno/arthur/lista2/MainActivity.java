package com.aluno.arthur.lista2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView lista = (ListView) findViewById(R.id.lstEquipes);
        final ArrayList<Pokemon> pokemons = preencherPokemonCompleto();
        PokemonAdapter adapter = new PokemonAdapter(pokemons, this);
        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("TAG", "usuário clicou no item:" + lista.getItemAtPosition(position).toString());
                Toast.makeText(getApplicationContext(), "Você clicou em: " + pokemons.get(position).getNome(), Toast.LENGTH_LONG).show();
                goToMainAvtivity2(pokemons.get(position));

            }
        });
    }

    public void goToMainAvtivity2(Pokemon item) {
        Intent i = new Intent(this, Main2Activity.class);
        i.putExtra("POKEMON", item);
        startActivity(i);
    }

    ArrayList<Pokemon> preencherPokemonCompleto() {
        ArrayList<Pokemon> arrayList = new ArrayList<>();
        arrayList.add(new Pokemon(1, "Bulbasaur", R.drawable.p001));
        arrayList.get(arrayList.size() - 1).setTipo("Grass, Poison");
        arrayList.get(arrayList.size() - 1).setDescricao(
            "Bulbasaur can be seen napping in bright sunlight. There is a seed " +
                "on its back. By soaking up the sun's rays, the seed grows progressively larger."
        );
        arrayList.add(new Pokemon(2, "Ivysaur", R.drawable.p002));
        arrayList.get(arrayList.size() - 1).setTipo("Grass, Poison");
        arrayList.get(arrayList.size() - 1).setDescricao(
            "There is a bud on this Pokémon's back. To support its weight, Ivysaur's legs " +
                "and trunk grow thick and strong. If it starts spending more time lying " +
                "in the sunlight, it's a sign that the bud will bloom into a large flower soon.");
        arrayList.add(new Pokemon(3, "Venusaur", R.drawable.p003));
        arrayList.get(arrayList.size() - 1).setTipo("Grass, Poison");
        arrayList.get(arrayList.size() - 1).setDescricao(
            "There is a large flower on Venusaur's back. The flower is said to take on vivid " +
                "colors if it gets plenty of nutrition and sunlight. The flower's aroma soothes the emotions of people."
        );
        arrayList.add(new Pokemon(4, "Charmander", R.drawable.p004));
        arrayList.get(arrayList.size() - 1).setTipo("Fire");
        arrayList.get(arrayList.size() - 1).setDescricao(
            "The flame that burns at the tip of its tail is an indication of its emotions. " +
                "The flame wavers when Charmander is enjoying itself. If the Pokémon becomes enraged, the flame burns fiercely."
        );
        arrayList.add(new Pokemon(5, "Charmeleon", R.drawable.p005));
        arrayList.get(arrayList.size() - 1).setTipo("Fire");
        arrayList.get(arrayList.size() - 1).setDescricao(
            "Charmeleon mercilessly destroys its foes using its sharp claws. If it encounters a strong foe, " +
                "it turns aggressive. In this excited state, the flame at the tip of its tail flares with a bluish white color."
        );
        arrayList.add(new Pokemon(6, "Charizard", R.drawable.p006));
        arrayList.get(arrayList.size() - 1).setTipo("Grass, Poison");
        arrayList.get(arrayList.size() - 1).setDescricao(
            "Charizard flies around the sky in search of powerful opponents. It breathes fire of such " +
                "great heat that it melts anything. However, it never turns its fiery breath on any opponent weaker than itself."
        );
        arrayList.add(new Pokemon(7, "Squirtle", R.drawable.p007));
        arrayList.get(arrayList.size() - 1).setTipo("Water");
        arrayList.get(arrayList.size() - 1).setDescricao(
            "Squirtle's shell is not merely used for protection. The shell's rounded shape and the grooves " +
                "on its surface help minimize resistance in water, enabling this Pokémon to swim at high speeds."
        );
        arrayList.add(new Pokemon(8, "Wartortle", R.drawable.p008));
        arrayList.get(arrayList.size() - 1).setTipo("Water");
        arrayList.get(arrayList.size() - 1).setDescricao(
            "Its tail is large and covered with a rich, thick fur. The tail becomes increasingly deeper " +
                "in color as Wartortle ages. The scratches on its shell are evidence of this Pokémon's toughness as a battler."
        );
        arrayList.add(new Pokemon(9, "Blastoise", R.drawable.p009));
        arrayList.get(arrayList.size() - 1).setTipo("Water");
        arrayList.get(arrayList.size() - 1).setDescricao(
            "Blastoise has water spouts that protrude from its shell. The water spouts are very accurate. " +
                "They can shoot bullets of water with enough accuracy to strike empty cans from a distance of over 160 feet."
        );
        arrayList.add(new Pokemon(10, "Caterpie", R.drawable.p010));
        arrayList.get(arrayList.size() - 1).setTipo("Bug");
        arrayList.get(arrayList.size() - 1).setDescricao(
            "Caterpie has a voracious appetite. It can devour leaves bigger than its body right before your eyes. " +
                "From its antenna, this Pokémon releases a terrifically strong odor."
        );
        arrayList.add(new Pokemon(11, "Metapod", R.drawable.p011));
        arrayList.get(arrayList.size() - 1).setTipo("Bug");
        arrayList.get(arrayList.size() - 1).setDescricao(
            "The shell covering this Pokémon's body is as hard as an iron slab. Metapod does not move very much. " +
                "It stays still because it is preparing its soft innards for evolution inside the hard shell."
        );
        arrayList.add(new Pokemon(12, "Butterfree", R.drawable.p012));
        arrayList.get(arrayList.size() - 1).setTipo("Bug, Flying");
        arrayList.get(arrayList.size() - 1).setDescricao(
            "Butterfree has a superior ability to search for delicious honey from flowers. It can even search out, " +
                "extract, and carry honey from flowers that are blooming over six miles from its nest."
        );
        return arrayList;
    }

}
