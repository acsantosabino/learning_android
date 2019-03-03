package com.aluno.arthur.leiturama;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aluno.arthur.leiturama.models.Book;

import java.io.File;
import java.util.List;

public class LibraryAdapter extends BaseAdapter {
    private final List<Book> books;
    private final Activity activity;
    public final static String INTENT_EXTRA_BOOK_DETAIL = "LibraryAdapter.bookSelected";

    private AdapterView.OnItemClickListener onClick =
            new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    Book b = books.get(position);
                    Intent i = new Intent(activity, BookDetailsActivity.class);
                    i.putExtra(INTENT_EXTRA_BOOK_DETAIL, b);
                    activity.startActivity(i);
                }
            };

    private AdapterView.OnItemLongClickListener onItemLongClick =
            new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Book b = books.get(position);
                    new LibraryDialogs(activity, b, FBLoader.usuarioLogado).show();
                    return true;
                }
            };

    public AdapterView.OnItemLongClickListener getOnItemLongClick(){
        return onItemLongClick;
    }

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
        tvBookTitle.setText(book.getTitle().toUpperCase());

        TextView tvBookAuthor = (TextView)v.findViewById(R.id.libraryBookAuthor);
        tvBookAuthor.setText("Autor: " + book.getAuthor());

        TextView tvBookPublisher = (TextView)v.findViewById(R.id.libraryBookPublisher);
        tvBookPublisher.setText("Editora: " + book.getPublisher());

        TextView tvBookCategories = (TextView)v.findViewById(R.id.libraryBookCategories);
        tvBookCategories.setText("Categoria: " + book.getCategories());

        TextView tvBookIsbn = (TextView)v.findViewById(R.id.libraryBookIsbn);
        tvBookIsbn.setText("ISBN: " + book.getIsbn());

        TextView tvBookStatus = (TextView)v.findViewById(R.id.libraryBookStatus);
        tvBookStatus.setText("Status: " + book.getStatus());

        if(book.getImagePath()!=null) {
            File imgFile = new File(activity.getFilesDir(), book.getImagePath());
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ImageView ivCoverBook = (ImageView) v.findViewById(R.id.libraryBookCover);
                ivCoverBook.setImageBitmap(myBitmap);
            }
        }

        return v;
    }
}