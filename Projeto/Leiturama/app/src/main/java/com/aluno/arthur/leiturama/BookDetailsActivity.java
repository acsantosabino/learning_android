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

        if(book.getImagePath()!= null) {
            File imgFile = new File(getFilesDir(), book.getImagePath());
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ImageView ivCoverBook = (ImageView) findViewById(R.id.bookDetailsCover);
                ivCoverBook.setImageBitmap(myBitmap);
            }
        }

        //Tratamento da parte do proprietário
        TextView tvOwnerName = findViewById(R.id.bookDetailsOwnerName);
        tvOwnerName.setText("Nome: " + book.getOwner().getName());
        TextView tvOwnerMail = findViewById(R.id.bookDetailsOwnerMail);
        tvOwnerMail.setText("E-mail: " + book.getOwner().getEmail());
        TextView tvOwnerTelephone = findViewById(R.id.bookDetailsOwnerTelephone);
        tvOwnerTelephone.setText("Telefone: " + book.getOwner().getPhone());

        Button btnBook = findViewById(R.id.bookDetailBtnSolicitarEmprestimo);
        switch (Book.BookStatus.valueOf(book.getStatus())){
            case AVAILABLE:
                btnBook.setText(R.string.request_borrowing_action);
                break;

            case LENT:
                btnBook.setText(R.string.assign_devolution_action);
                break;

            case BORROW_ASKED:
                if(book.getOwner().getId().equals(FBLoader.usuarioLogado.getId())){
                    btnBook.setText(R.string.confirm_borrowing_action);
                }
                else if (book.getBorrower().getId().equals(FBLoader.usuarioLogado.getId())){
                    btnBook.setText(R.string.cancel_borrowing_action);
                }
                break;

            case DEVOLUTION_ASKED:
                btnBook.setText(R.string.confirm_devolution_action);
        }

    }

    public void solicitarEmprestimo(View view){
        new LibraryDialogs(this, this.book, FBLoader.usuarioLogado).show();
    }

    @Override
    public void onActionConfim() {
        finish();
    }
}
