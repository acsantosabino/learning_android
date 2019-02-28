package com.aluno.arthur.leiturama;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.aluno.arthur.leiturama.models.Book;
import com.aluno.arthur.leiturama.models.User;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class LibraryDialogs {

    private Activity context;
    private User user;
    private Book book;
    private int title;
    private int msg;
    private boolean action;

    public LibraryDialogs(Activity context, Book book, User user) {
        this.context = context;
        this.user = user;
        this.book = book;
        this.action = false;
        getContentText();
    }

    public void showAlert(){

        if(action) {
            AlertDialog.Builder alertDialogBilder = new AlertDialog.Builder(context);
            alertDialogBilder.setTitle(title);
            alertDialogBilder.setMessage(msg);
            alertDialogBilder.setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertAction();
                }
            });
            alertDialogBilder.show();
        }
        else {
            Toast.makeText(context,R.string.no_action_available,Toast.LENGTH_SHORT).show();
        }
    }

    public void getContentText(){

        Book.BookStatus bookStatus = Book.BookStatus.valueOf(book.getStatus());

        if(user.getId().equals(book.getOwner().getId())){

            switch (bookStatus) {

                case BORROW_ASKED:
                    title = R.string.confirm_borrowing_title;
                    msg = R.string.confirm_borrowing_msg;
                    action = true;
                    break;

                case DEVOLUTION_ASKED:
                    title = R.string.confirm_devolution_title;
                    msg = R.string.confirm_devolution_msg;
                    action = true;
                    break;
            }
        }
        else {

            switch (bookStatus) {

                case AVAILABLE:
                    title = R.string.request_borrowing_title;
                    msg = R.string.request_borrowing_msg;
                    action = true;
                    break;

                case LENT:
                    title = R.string.assign_devolution_title;
                    msg = R.string.assign_devolution_msg;
                    action = true;
                    break;
            }
        }
    }

    public void alertAction(){

        Book.BookStatus bookStatus = Book.BookStatus.valueOf(book.getStatus());

        if(user.getId().equals(book.getOwner().getId())) {
            switch (bookStatus) {

                case BORROW_ASKED:
                    book.setStatus(Book.BookStatus.LENT.toString());
                    break;

                case DEVOLUTION_ASKED:
                    book.setStatus(Book.BookStatus.AVAILABLE.toString());
                    break;
            }
        }
        else {
            switch (bookStatus) {

                case AVAILABLE:
                    book.setStatus(Book.BookStatus.BORROW_ASKED.toString());
                    break;

                case LENT:
                    book.setStatus(Book.BookStatus.BORROW_ASKED.toString());
                    break;
            }
        }
        updateBookStatus();

    }

    private void updateBookStatus(){
        Map<String, Object> update = new HashMap<>();
        update.put("status", book.getStatus());

        FBLoader.fbFirestore.collection("books").document(book.getId())
            .set(update, SetOptions.merge());
    }
}
