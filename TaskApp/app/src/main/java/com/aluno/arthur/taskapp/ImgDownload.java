package com.aluno.arthur.taskapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImgDownload extends AsyncTask <Object, Void, Bitmap>{

    private String imageURL;
    private Context context;
    private ImageView imageView;

    public ImgDownload(String imageURL, Context context, ImageView imageView) {
        this.imageURL = imageURL;
        this.context = context;
        this.imageView = imageView;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if(bitmap!=null) {
            imageView.setImageBitmap(bitmap);
        }else{
            Log.i("AsyncTask", "Erro ao baixar a imagem " +
                Thread.currentThread().getName());
        }
        Log.i("AsyncTask", "Tirando ProgressDialog da tela Thread: " +
            Thread.currentThread().getName());
    }

    @Override
    protected Bitmap doInBackground(Object[] objects) {
        if (isOnline(this.context)) {
            try {
                Bitmap bitmap1 = this.getImageFromURL();
                return bitmap1;
            } catch (IOException e) {
                Log.e("IMG", e.getMessage());
            }
        }
        return null;
    }

    Bitmap getImageFromURL() throws IOException {
        Bitmap bitmap = null;

        URL url = new URL(this.imageURL);
        InputStream is = url.openStream();
        bitmap = BitmapFactory.decodeStream(is);

        is.close();

        return bitmap;
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm =
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
