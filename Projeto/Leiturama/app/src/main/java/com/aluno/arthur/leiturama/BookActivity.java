package com.aluno.arthur.leiturama;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.aluno.arthur.leiturama.models.Book;
import com.aluno.arthur.leiturama.services.BookReqTask;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

public class BookActivity extends AppCompatActivity {

    private View mProgressView;
    private View mBookFormView;
    private TextInputEditText mTilte;
    private TextInputEditText mAuthos;
    private TextInputEditText mPublisher;
    private TextInputEditText mPublishedDate;
    private TextInputEditText mDescription;
    private TextInputEditText mPageCount;
    private TextInputEditText mCategories;
    private ImageView mCover;
    private StorageReference imgRef;
    private BookReqTask bookReqTask;
    private FirebaseFirestore db;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        mTilte = findViewById(R.id.book_title);
        mAuthos = findViewById(R.id.book_authors);
        mPublisher = findViewById(R.id.book_publisher);
        mCategories = findViewById(R.id.book_categories);
        mPublishedDate = findViewById(R.id.book_publishedDate);
        mPageCount = findViewById(R.id.book_pageCount);
        mDescription = findViewById(R.id.book_description);
        mCover = (ImageView) findViewById(R.id.book_cover);

        mBookFormView = findViewById(R.id.book_form);
        mProgressView = findViewById(R.id.load_book_progress);

        db = FirebaseFirestore.getInstance();

        Intent i = getIntent();
        String isbn = i.getStringExtra("ISBN");

        this.book = new Book(isbn);
        bookReqTask = new BookReqTask(this, isbn);

        imgRef = FirebaseStorage.getInstance().getReference().child("covers/" + isbn + ".jpg");
        showProgress(true);
        bookReqTask.execute();
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mBookFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mBookFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mBookFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mBookFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private boolean isValid() {
        boolean result = true;

        result &= !mTilte.getText().toString().isEmpty();
        result &= !mAuthos.getText().toString().isEmpty();
        result &= !mPageCount.getText().toString().isEmpty();

        return result;
    }

    @Subscribe
    public void onEvent(Book book) {
        bookReqTask = null;

        this.book = book;
        mTilte.setText(book.getTitle());
        mAuthos.setText(book.getAuthor());
        mPublisher.setText(book.getPublisher());
        mCategories.setText(book.getCategories());
        mPublishedDate.setText(book.getPublishedDate());
        mPageCount.setText(String.valueOf(book.getPageCount()));
        mDescription.setText(book.getDescription());
        showProgress(false);
    }

    @Subscribe
    public void onEvent(String imgPath) {
        showProgress(false);
        this.book.setImagePath(imgPath);

        setBookCover();
    }

    @Subscribe
    public void onEvent(Error error) {
        showProgress(false);
        Toast.makeText(this, "Erro", Toast.LENGTH_LONG).show();
        Log.e("BOOK ERROR", error.getMessage());
    }

    public void setBookCover() {
        File imgFile = new File(this.book.getImagePath());

        if (imgFile.exists()) {

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            ImageView myImage = (ImageView) findViewById(R.id.book_cover);

            myImage.setImageBitmap(myBitmap);

        }
    }

    private void uploadFB() {
        File imgFile = new File(this.getFilesDir(), book.getIsbn() + ".jpg");

        if (imgFile.exists()) {
            imgRef.putFile(Uri.fromFile(imgFile))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("IMG upload", exception.getMessage());
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("IMG upload", taskSnapshot.getMetadata().getPath());
                }
            });
        }
    }

    public void saveBook(View view) {

        if (this.isValid()) {

            db.collection("books").add(this.book);

            BitmapDrawable bitmapDrawable = (BitmapDrawable) mCover.getDrawable();
            uploadFB();

            finish();
        }
    }
}
