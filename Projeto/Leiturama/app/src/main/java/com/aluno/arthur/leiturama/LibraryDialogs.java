package com.aluno.arthur.leiturama;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.aluno.arthur.leiturama.models.Book;

public class LibraryDialogs {

    public enum AlertType {
        REQUEST_BORROWRING,
        CONFIRM_BORROWRING,
        ASSIGN_DEVOLUTION,
        CONFIRM_DEVOLUTION
    }

    private Activity context;
    private AlertType alertType;
    private Book book;
    private int title;
    private int msg;

    public LibraryDialogs(Activity context, AlertType alertType, Book book) {
        this.context = context;
        this.alertType = alertType;
        this.book = book;
        getContentText();
    }

    public void showAlert(){

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

    public void getContentText(){

        switch (alertType){

            case REQUEST_BORROWRING:
                title = R.string.request_borrowing_title;
                msg = R.string.request_borrowing_msg;
                break;

            case CONFIRM_BORROWRING:
                title = R.string.confirm_borrowing_title;
                msg = R.string.confirm_borrowing_msg;
                break;

            case ASSIGN_DEVOLUTION:
                title = R.string.assign_devolution_title;
                msg = R.string.assign_devolution_msg;
                break;

            case CONFIRM_DEVOLUTION:
                title = R.string.confirm_devolution_title;
                msg = R.string.confirm_devolution_msg;
                break;
        }
    }

    public void alertAction(){

        switch (alertType){

            case REQUEST_BORROWRING:
                book.setStatus("Book requested");
                break;

            case CONFIRM_BORROWRING:
                book.setStatus("Borrowing accept");
                break;

            case ASSIGN_DEVOLUTION:
                book.setStatus("Devolution assigned");
                break;

            case CONFIRM_DEVOLUTION:
                book.setStatus("Book available");
                break;
        }

    }

}
