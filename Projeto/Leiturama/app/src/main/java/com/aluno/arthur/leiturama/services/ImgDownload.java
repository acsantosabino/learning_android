package com.aluno.arthur.leiturama.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;

import com.aluno.arthur.leiturama.R;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ImgDownload extends AsyncTask {


    public static final int RESPONSE_OK = 200;
    public static final int RESPONSE_INVALID_REQUEST = 403;
    private static int TIMEOUT = 15;

    private Context context;
    private String imgUrl;
    private String imgName;
    private int responseCode;
    private Bitmap bitmap;
    private Error error;


    public ImgDownload(Context context, String imgUrl, String imgName) {
        this.context = context;
        this.imgUrl = imgUrl;
        this.imgName = imgName;
    }

    @Override
    protected Void doInBackground(Object[] objects) {
        if(!isOnline(context)){
            error = new Error(context.getString(R.string.error_connection));
            return null;
        }

        if(imgUrl != null){
            doRegularCall();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(error!= null){
            EventBus.getDefault().post(error);
        }else{

            switch (getResponseCode()){
                case RESPONSE_OK:
                    try {
                        String imgPath = saveBitmap(bitmap);
                        EventBus.getDefault().post(imgPath);
                    } catch (IOException e) {
                        EventBus.getDefault().post(new Error(e.getMessage()));
                    }
                    break;

                case RESPONSE_INVALID_REQUEST:
                    EventBus.getDefault().post(new Error(context.getString(R.string.error_request)));
                    break;
            }
        }
    }

    private void doRegularCall() {

        OkHttpClient client = new OkHttpClient();

        client.setConnectTimeout(TIMEOUT, TimeUnit.SECONDS);
        client.setReadTimeout(TIMEOUT, TimeUnit.SECONDS);

        Request request = new Request.Builder()
            .url(imgUrl)
            .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            responseCode = response.code();
            InputStream is =  response.body().byteStream();
            bitmap = BitmapFactory.decodeStream(is);

        } catch (IOException e) {
            error = new Error(context.getString(R.string.error_connection));
            EventBus.getDefault().post(new Error(e.getMessage()));
        }
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm =
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private String saveBitmap(Bitmap bitmap) throws IOException{

        File file = new File(context.getFilesDir(), "covers/" + imgName+".jpg");
        OutputStream outputStream = new FileOutputStream(file);

        bitmap.compress(Bitmap.CompressFormat.JPEG,90,outputStream);
        outputStream.flush();
        outputStream.close();

        List<String> paths = Uri.fromFile(file).getPathSegments();
        return paths.get(paths.size()-2) + File.separator + paths.get(paths.size()-1);
    }

    public int getResponseCode() {
        return responseCode;
    }

}
