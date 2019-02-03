package com.aluno.arthur.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    protected String email = "eu@mesmo.com";
    protected String senha = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public boolean validateEmail(String input){
        return (input.equals(this.email));
    }
    public boolean validateSenha(String input){
        return (input.equals(this.senha));
    }
    public void loginInvalido(){
        Toast toast = Toast.makeText(this, "Email ou senha invalid", Toast.LENGTH_LONG);
        toast.show();
    }

    public void goToMainAvtivity2(String msg){
        Intent i = new Intent(this, Main2Activity.class);
        i.putExtra(Intent.EXTRA_EMAIL, msg);
        startActivity(i);
    }

    public void onLogin(View view){
        EditText inpitEmail = findViewById(R.id.input_email);
        EditText inpitSenha = findViewById(R.id.input_pass);

        if(!validateEmail(inpitEmail.getText().toString())){
            loginInvalido();
            Log.d("EMAIL",inpitEmail.getText().toString());
        }
        else if(!validateSenha(inpitSenha.getText().toString())){
            loginInvalido();
            Log.d("SENHA",inpitSenha.getText().toString());
        }
        else {
            goToMainAvtivity2(inpitEmail.getText().toString());
        }

    }

}
