package com.aluno.arthur.leiturama;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class BarCodeDialog extends Dialog {

    private static final int REQUEST_CAMERA_PERMISSION = 201;
    public Activity context;
    public SurfaceView cameraView;
    public CameraSource cameraSource;
    public BarCodeDialogListener barCodeDialogListener;

    public BarCodeDialog(Activity context) {
        super(context);
        this.context = context;
        checkListener();
    }

    public interface BarCodeDialogListener {
        void onSucssesRead(String isbn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_barcode);

        cameraView = findViewById(R.id.camera_view);

        BarcodeDetector barcodeDetector =
            new BarcodeDetector.Builder(this.context)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource
            .Builder(this.context, barcodeDetector)
            .setRequestedPreviewSize(720, 480)
            .setAutoFocusEnabled(true)
            .build();
        setCancelable(false);

        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                try {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        cameraSource.start(cameraView.getHolder());
                    }
                    else {
                        ActivityCompat.requestPermissions(context, new
                            String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    Log.d("BARCODE:","format " + barcodes.valueAt(0).valueFormat);
                    Log.d("BARCODE:","display " + barcodes.valueAt(0).displayValue);
                    if (barcodes.valueAt(0).valueFormat == Barcode.ISBN){
                        barCodeDialogListener.onSucssesRead(barcodes.valueAt(0).rawValue);
                        dismiss();
                    }
                }
            }
        });
    }

    public void checkListener() {
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            barCodeDialogListener = (BarCodeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                + " must implement BarCodeDialogListener");
        }
    }
}