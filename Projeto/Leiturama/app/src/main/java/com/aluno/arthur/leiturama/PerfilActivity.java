package com.aluno.arthur.leiturama;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aluno.arthur.leiturama.models.Book;
import com.aluno.arthur.leiturama.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PerfilActivity extends AppCompatActivity implements BarCodeDialog.BarCodeDialogListener {

    private TextView mPerfilName;
    private TextView mPhone;
    private TextView mStats;
    private ListView mBooks;
    private ArrayList<String> books;
    private ArrayList<Book> booksComplete;
    private static final int REQUEST_CAMERA = 1;
    private BarCodeDialog barCodeDialog;
    private User user;
    private int ownedBooks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        mPerfilName = findViewById(R.id.perfil_name);
        mPhone = findViewById(R.id.perfil_phone);
        mStats = findViewById(R.id.perfil_stats);
        mBooks = findViewById(R.id.perfil_books);

        barCodeDialog = new BarCodeDialog(this);
        barCodeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        readUser();

        mBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LibraryDialogs libraryDialogs = new LibraryDialogs(PerfilActivity.this, booksComplete.get(position), user);
                libraryDialogs.showAlert();
            }
        });
    }

    @Override
    protected void onResume() {
        readBooks();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_perfil, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_perfil_edit:
                Intent i = new Intent(PerfilActivity.this, NewUserActivity.class);
                startActivity(i);
                break;

            case R.id.menu_perfil_add:
                checkCamera();
                break;

            default:
                break;
        }

        return true;
    }

    public void onLibraryBtnClick(View view){
        Intent i = new Intent(this, LibraryActivity.class);
        this.startActivity(i);
    }

    private void readUser(){
        FirebaseUser fbUser = FBLoader.fbAuth.getCurrentUser();
        FBLoader.fbFirestore.collection("user").document(fbUser.getUid())
            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                FBLoader.usuarioLogado = user;
                mPerfilName.setText(user.getName());
                mPhone.setText(user.getPhone());
                String stats = new String();
                stats += user.getN_lent() + " lent";
                stats += " | ";
                stats += ownedBooks + " owned";
                stats += " | ";
                stats += user.getN_borrowed() + " read";
                mStats.setText(stats);

            }
        });
    }

    private void updateList(){
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,books);
        mBooks.setAdapter(adaptador);
        String stats = new String();
        stats += user.getN_lent() + " lent";
        stats += " | ";
        stats += ownedBooks + " owned";
        stats += " | ";
        stats += user.getN_borrowed() + " read";
        mStats.setText(stats);
    }

    private void readBooks(){
        books = new ArrayList();
        booksComplete = new ArrayList();
        ownedBooks = 0;
        CollectionReference booksRef= FBLoader.fbFirestore.collection("books");
        booksRef.orderBy("title", Query.Direction.ASCENDING).get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Book book = document.toObject(Book.class);
                            if( book.getOwner().getId().equals(user.getId())) {
                                book.setId(document.getId());
                                books.add(book.getTitle());
                                booksComplete.add(book);
                                ownedBooks += 1;
                            }
                        }
                        updateList();
                    } else {
                        Toast.makeText(PerfilActivity.this,
                            "Error getting documents: "+task.getException().getMessage(),
                            Toast.LENGTH_LONG).show();
                    }
                }
            });
    }


    private void checkCamera() {
        if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED){
            Log.d("CAMERA","camera ok");
            barCodeDialog.show();
        }else{
            ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.CAMERA},
                REQUEST_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if(requestCode == REQUEST_CAMERA){
            if(grantResults.length > 0){
                if(grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //TODO ABRO A CAMERA
                    Log.d("CAMERA","camera ok");
                    barCodeDialog.show();
                }else{
                    showError(R.string.error_permission_not_granted);
                }
            } else{
                showError(R.string.permission_request_canceled);
            }
        }
    }

    public void showError(int idStringDescription){
        new AlertDialog.Builder(this)
            .setTitle(R.string.title_error)
            .setMessage(idStringDescription)
            .setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            })
            .setCancelable(false)
            .show();
    }

    @Override
    public void onSucssesRead(String isbn) {
        Intent i = new Intent(PerfilActivity.this, BookActivity.class);
        i.putExtra("ISBN",isbn);
        i.putExtra("USER",user);
        startActivity(i);
    }
}
