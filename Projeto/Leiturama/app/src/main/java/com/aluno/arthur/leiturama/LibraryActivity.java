package com.aluno.arthur.leiturama;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.aluno.arthur.leiturama.models.Book;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LibraryActivity extends AppCompatActivity {
    private List<Book> books = null;
    private ListView lvLibrary = null;
    private LibraryAdapter adapter = null;

    private View.OnCreateContextMenuListener contextMenuLibraryListener =
            new View.OnCreateContextMenuListener(){
                @Override
                public void onCreateContextMenu(ContextMenu contextMenu, View view,
                                                ContextMenu.ContextMenuInfo contextMenuInfo) {
                    contextMenu.add(Menu.NONE, 1, Menu.NONE, "opcao 1");
                    contextMenu.add(Menu.NONE, 2, Menu.NONE, "opcao 2");
                }
            };

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo menuInfo =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = menuInfo.position;
        Book bookSelected = books.get(position);

        switch (item.getItemId()) {
            case 1:
                Toast.makeText(this, "Primeira opcao " + bookSelected.getTitle(), Toast.LENGTH_SHORT).show();
                books.remove(position);
            case 2:
                Toast.makeText(this, "Segunda opcao " + bookSelected.getTitle(), Toast.LENGTH_SHORT).show();
        }
        this.adapter.notifyDataSetChanged();
        return super.onContextItemSelected(item);
    }

    OnSuccessListener<QuerySnapshot> listener = new OnSuccessListener<QuerySnapshot>() {
        @Override
        public void onSuccess(QuerySnapshot collection) {
            books = new ArrayList<>();

            List<Book> lentToMe = new ArrayList<>();
            List<Book> available = new ArrayList<>();
            List<Book> unavailable = new ArrayList<>();


            for ( DocumentSnapshot snapShot: collection) {
                Book b = snapShot.toObject(Book.class);
                if(b.getStatus().equals( Book.BookStatus.LENT )){
                    // Como saber informações do empréstimo ?
                    unavailable.add(b);
                } else {
                    available.add(b);
                }
            }
            books.addAll(lentToMe);
            books.addAll(available);
            books.addAll(unavailable);
            atualizarListViewBiblioteca();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        lvLibrary = findViewById(R.id.libraryLvBooks);
        FBLoader.fbFirestore.collection("books").get().addOnSuccessListener(listener);

    }

    public void atualizarListViewBiblioteca(){
        adapter = new LibraryAdapter(books, this);
        lvLibrary.setAdapter(adapter);
        lvLibrary.setOnCreateContextMenuListener(contextMenuLibraryListener);
        lvLibrary.setOnItemClickListener(adapter.getOnItemClick());
        lvLibrary.setOnItemLongClickListener(adapter.getOnItemLongClick());
    }
}
