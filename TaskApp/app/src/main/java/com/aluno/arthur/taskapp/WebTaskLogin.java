package com.aluno.arthur.taskapp;

import android.content.Context;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

public class WebTaskLogin extends WebTaskBase {

    private static String SERVICE_URL = "login";
    private String email;
    private String senha;

    public WebTaskLogin(Context context, String email, String senha) {
        super(context, SERVICE_URL);
        this.email = email;
        this.senha = senha;
    }

    @Override
    public String getRequestBody() {

        JSONObject json = new JSONObject();
        try {
            json.put("email", this.email);
            json.put("senha", this.senha);
        } catch (JSONException e){
            Log.e("JSON ERROR", e.getMessage());
            return null;
        }

        Log.d("JSON",json.toString());
        return json.toString();
    }

    @Override
    public void handleResponse(String response) {
        Usuario user = new Usuario();

        try {
            JSONObject json = new JSONObject(response);
            user.setNome(json.getString("name"));
            user.setToken(json.getString("token"));
            user.setFotoUrl(json.getString("photo_url"));
            Log.d("JSON",json.toString());
            EventBus.getDefault().post(user);
        } catch (JSONException e) {
            Log.e("JSON ERROR", e.getMessage());
            EventBus.getDefault().post(getContext().getString(R.string.label_error_invalid_response));
        }

    }

}
