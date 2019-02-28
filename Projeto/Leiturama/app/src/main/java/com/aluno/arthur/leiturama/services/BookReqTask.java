package com.aluno.arthur.leiturama.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.aluno.arthur.leiturama.R;
import com.aluno.arthur.leiturama.models.Book;
import com.aluno.arthur.leiturama.support.JsonParer;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class BookReqTask extends AsyncTask {

    public static final int RESPONSE_OK = 200;
    public static final int RESPONSE_INVALID_REQUEST = 403;
    private static int TIMEOUT = 15;
    private static final String API_URL = "https://www.googleapis.com/books/v1/volumes?";//q=9788578274085+isbn";

    private Context context;
    private String isbn;
    private int responseCode;
    private String responseString;
    private Error error;


    public BookReqTask(Context context, String isbn) {
        this.context = context;
        this.isbn = isbn;
    }

    @Override
    protected Void doInBackground(Object[] objects) {
        if(!isOnline(context)){
            error = new Error(context.getString(R.string.error_connection));
            responseString = null;
            return null;
        }

        doRegularCall();

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
                        Book book = JsonParer.parseBook(responseString);
                        String path = JsonParer.parseImgUrl(responseString);
                        ImgDownload imgTask = new ImgDownload(context, path, book.getIsbn());
                        imgTask.execute();
                        EventBus.getDefault().post(book);
                    } catch (JSONException e) {
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
            .url(API_URL + "q=" + this.isbn + "+isbn")
            .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            responseCode = response.code();
            responseString =  response.body().string();
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

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

}
