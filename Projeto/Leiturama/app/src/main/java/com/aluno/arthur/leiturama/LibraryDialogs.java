package com.aluno.arthur.leiturama;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.aluno.arthur.leiturama.models.Book;
import com.aluno.arthur.leiturama.models.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;

public class LibraryDialogs extends Dialog {

    private Activity context;
    private User user;
    private Book book;
    private int title;
    private int msg;
    private boolean action, negativeAction;
    public LibaryDialogListener mListener;

    public LibraryDialogs(Activity context, Book book, User user) {
        super(context);
        this.context = context;
        this.user = user;
        this.book = book;
        this.action = false;
        this.negativeAction = false;
        checkListener();
        getContentText();
    }

    public interface LibaryDialogListener {
        public void onActionConfim();
    }

    public void checkListener() {
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (LibaryDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                + " must implement LibaryDialogListener");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(action) {
            AlertDialog.Builder alertDialogBilder = new AlertDialog.Builder(context);
            alertDialogBilder.setTitle(title);
            alertDialogBilder.setMessage(msg);
            alertDialogBilder.setPositiveButton(R.string.lable_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertAction();
                    mListener.onActionConfim();
                }
            });
            if(negativeAction){
                alertDialogBilder.setNegativeButton(R.string.lable_no, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        borrowDenied();
                        mListener.onActionConfim();
                    }
                });
            }
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
                    negativeAction = true;
                    break;

                case DEVOLUTION_ASKED:
                    title = R.string.confirm_devolution_title;
                    msg = R.string.confirm_devolution_msg;
                    action = true;
                    break;
            }
        }
        else if(book.getBorrower() != null) {
            if ((user.getId().equals(book.getBorrower().getId()))){
                if (bookStatus == Book.BookStatus.BORROW_ASKED){
                    title = R.string.cancel_borrowing_title;
                    msg = R.string.cancel_borrowing_msg;
                    action = true;
                }
                else if (bookStatus == Book.BookStatus.LENT) {
                    title = R.string.assign_devolution_title;
                    msg = R.string.assign_devolution_msg;
                    action = true;
                }
            }
        }
        else if(bookStatus == Book.BookStatus.AVAILABLE){
            title = R.string.request_borrowing_title;
            msg = R.string.request_borrowing_msg;
            action = true;
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
                    book.setBorrower(null);
                    break;
            }
        }
        else if(book.getBorrower() != null) {
            if ((user.getId().equals(book.getBorrower().getId()))) {
                if (bookStatus == Book.BookStatus.LENT) {
                    book.setStatus(Book.BookStatus.DEVOLUTION_ASKED.toString());
                } else if (bookStatus == Book.BookStatus.BORROW_ASKED) {
                    book.setStatus(Book.BookStatus.AVAILABLE.toString());
                    book.setBorrower(null);
                }
            }
        }
        else if (bookStatus == Book.BookStatus.AVAILABLE) {
            book.setStatus(Book.BookStatus.BORROW_ASKED.toString());
            book.setBorrower(user);
        }

        updateBookStatus();

    }
    public void borrowDenied(){
        book.setStatus(Book.BookStatus.AVAILABLE.toString());
        book.setBorrower(null);
        updateBookStatus();
    }

    private void updateBookStatus(){
        DocumentReference bookRef = FBLoader.fbFirestore.collection("books").document(book.getId());
        bookRef.set(book, SetOptions.merge());
    }
}
