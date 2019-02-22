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


public class BarCodeExampleActivity extends AppCompatActivity implements BarCodeDialog.BarCodeDialogListener {

    private static final int REQUEST_CAMERA = 1;
    private BarCodeDialog barCodeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_example);

        barCodeDialog = new BarCodeDialog(this);
        barCodeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        checkCamera();
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

        Intent i = new Intent(BarCodeExampleActivity.this, BookActivity.class);
        i.putExtra("ISBN",isbn);
        startActivity(i);
    }
}
