package com.aluno.arthur.leiturama;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aluno.arthur.leiturama.models.Book;
import com.google.firebase.firestore.Query;

import org.w3c.dom.Text;

import java.io.File;

public class BookDetailsActivity extends AppCompatActivity implements LibraryDialogs.LibaryDialogListener{
    private Book book = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        this.book =
                (Book) getIntent().getSerializableExtra(LibraryAdapter.INTENT_EXTRA_BOOK_DETAIL);

        TextView tvTitulo = findViewById(R.id.bookDetailsTitle);
        tvTitulo.setText(book.getTitle().toUpperCase());

        TextView tvDescricao = findViewById(R.id.bookDetailsDescricao);
        tvDescricao.setText(book.getDescription());

        TextView tvCategorias = findViewById(R.id.bookDetailsCategories);
        tvCategorias.setText("Categorias: " + book.getCategories());

        TextView tvAutor = findViewById(R.id.bookDetailsAuthor);
        tvAutor.setText("Autor: " + book.getAuthor());

        TextView tvIsbn = findViewById(R.id.bookDetailsIsbn);
        tvIsbn.setText("ISBN: " + book.getIsbn());

        TextView tvNroPaginas = findViewById(R.id.bookDetailsNroPaginas);
        tvNroPaginas.setText("Páginas: " + book.getPageCount());

        TextView tvDataPublicacao = findViewById(R.id.bookDetailsDataPublicacao);
        tvDataPublicacao.setText("Data de publicação: " + book.getPublishedDate());

        TextView tvEditora = findViewById(R.id.bookDetailsPublisher);
        tvEditora.setText("Editora: " + book.getPublisher());

        TextView tvStatus = findViewById(R.id.bookDetailsStatus);
        tvStatus.setText("Status: " + book.getStatus());

        File imgFile = new  File(getFilesDir(), (book.getImagePath()==null)?"":book.getImagePath());
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView ivCoverBook = (ImageView) findViewById(R.id.bookDetailsCover);
            ivCoverBook.setImageBitmap(myBitmap);
        }

        //Tratamento da parte do proprietário
        TextView tvOwnerName = findViewById(R.id.bookDetailsOwnerName);
        tvOwnerName.setText("Nome: " + book.getOwner().name);
        TextView tvOwnerMail = findViewById(R.id.bookDetailsOwnerMail);
        tvOwnerMail.setText("E-mail: " + book.getOwner().email);

//        if(book.getStatus().equals( Book.BookStatus.LENT ) ||
//                book.getOwner().equals(FBLoader.usuarioLogado)){
//            Button btn = findViewById(R.id.bookDetailBtnSolicitarEmprestimo);
//            btn.setEnabled(false);
//        }

    }

    public void solicitarEmprestimo(View view){
        new LibraryDialogs(this, this.book, FBLoader.usuarioLogado).show();
    }

    @Override
    public void onActionConfim() {
        finish();
    }
}
