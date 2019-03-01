package com.aluno.arthur.leiturama;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aluno.arthur.leiturama.models.Book;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LibraryActivity extends AppCompatActivity {



    OnSuccessListener<QuerySnapshot> listener = new OnSuccessListener<QuerySnapshot>() {
        @Override
        public void onSuccess(QuerySnapshot collection) {
            List<Book> books = new ArrayList<>();
            for ( DocumentSnapshot snapShot: collection) {
                Book b = snapShot.toObject(Book.class);
                books.add(b);
            }
            atualizarListViewBiblioteca(books);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        FBLoader.fbFirestore.collection("books").get().addOnSuccessListener(listener);

    }

    public void atualizarListViewBiblioteca(List<Book> books){
        ListView listView = findViewById(R.id.libraryLvBooks);
        LibraryAdapter adapter = new LibraryAdapter(books, this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(adapter.getOnItemClick());
    }
}

class LibraryAdapter extends BaseAdapter{
    private final List<Book> books;
    private final Activity activity;

    private AdapterView.OnItemClickListener onClick =
            new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            Toast.makeText(activity, books.get(position).getTitle(), Toast.LENGTH_SHORT).show();
        }
    };

    public AdapterView.OnItemClickListener getOnItemClick(){
        return onClick;
    }

    public LibraryAdapter(List<Book> books, Activity activity){
        this.books = books;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public String getBookId(int position) {
        return ((Book)books.get(position)).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = activity.getLayoutInflater().inflate(R.layout.list_view_library, parent, false);
        Book book = this.books.get(position);

        TextView tvBookTitle = (TextView)v.findViewById(R.id.libraryBookTitle);
        tvBookTitle.setText(book.getTitle());

        TextView tvBookAuthor = (TextView)v.findViewById(R.id.libraryBookAuthor);
        tvBookAuthor.setText(book.getAuthor());

        TextView tvBookPublisher = (TextView)v.findViewById(R.id.libraryBookPublisher);
        tvBookPublisher.setText(book.getPublisher());

        TextView tvBookCategories = (TextView)v.findViewById(R.id.libraryBookCategories);
        tvBookCategories.setText(book.getCategories());

        TextView tvBookIsbn = (TextView)v.findViewById(R.id.libraryBookIsbn);
        tvBookIsbn.setText(book.getIsbn());

        TextView tvBookStatus = (TextView)v.findViewById(R.id.libraryBookStatus);
        tvBookStatus.setText(book.getStatus());


        return v;
    }
}
