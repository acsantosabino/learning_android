package com.aluno.arthur.camapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitEventListenerAdapter;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int MYAPP_REQUEST_PERMITION = 1;

    protected static final String[] permitionRequest = {
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    CameraView mCameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCameraView = findViewById(R.id.camera);
        mCameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {
                Log.e("CameraError:",cameraKitError.getMessage());
            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                saveImage(cameraKitImage);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });

        checkPermissios();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(checkPermissios()) {
            mCameraView.start();
        }
    }

    @Override
    protected void onPause() {
        mCameraView.stop();
        super.onPause();
    }

    private boolean checkPermissios() {
        boolean allowed = true;
        for(String permission : permitionRequest) {
            allowed &= (ContextCompat.checkSelfPermission(this,
                permission) == PackageManager.PERMISSION_GRANTED);
            Log.d("ALLOW", String.valueOf(allowed));
        }
        if (!allowed) {
            ActivityCompat.requestPermissions(
                this,
                permitionRequest,
                MYAPP_REQUEST_PERMITION);
        }
        return allowed;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if (requestCode == MYAPP_REQUEST_PERMITION) {
            for(int i=0; i < grantResults.length; i++) {
                if (grantResults[i] ==
                    PackageManager.PERMISSION_GRANTED) {
                    //TODO ABRO A CAMERA
                } else {
                    showError(R.string.error_permission_not_granted, permissions[i]);
                }
            };
            if (grantResults.length == 0){
                showError(R.string.permission_request_canceled);
            }
        }
    }

    public void showError(int idStringDescription, String permission) {
        String msg = getResources().getString(idStringDescription) + permission;
        new AlertDialog.Builder(this)
            .setTitle(R.string.title_error)
            .setMessage(msg)
            .setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            })
            .setCancelable(false)
            .show();
    }

    public void showError(int idStringDescription) {
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
    private String saveImage(CameraKitImage cameraKitImage) {
        File file = null;
        OutputStream fOut = null;
        try {
            file = createImageFile();
            fOut =  new FileOutputStream(file);
            fOut.write(cameraKitImage.getJpeg());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fOut.close();
            } catch (IOException e) {
                Log.d("CAMERA_ERROR", e.getMessage());
            }
        }

        return file.getAbsolutePath();
    }

    private File createImageFile() throws IOException {
        String timeStamp =
            new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
            getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
            imageFileName,  /* prefix */
            ".mysecret",         /* suffix */
            storageDir      /* directory */
        );

        return imageFile;
    }

    private void shareImage(String path) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ContentValues values = new ContentValues();
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
        startActivity(share);
    }

    public void btnTakePic(View view) {
        mCameraView.captureImage();
    }
}