package com.aluno.arthur.teste;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int placarA = 0;
    int placarB = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.displayTimeA();
        this.displayTimeB();
    }

    public void displayTimeA(){
        TextView scoreView = (TextView) findViewById(R.id.team_a_score);
        scoreView.setText(String.valueOf(this.placarA));
    }

    public void displayTimeB(){
        TextView scoreView = (TextView) findViewById(R.id.team_b_score);
        scoreView.setText(String.valueOf(this.placarB));
    }

    public void updateScore(View view){

        switch (view.getId()){
            case R.id.team_a_3plus_bnt:
                this.placarA += 3;
                break;
            case R.id.team_a_2plus_bnt:
                this.placarA += 2;
                break;
            case R.id.team_a_1plus_bnt:
                this.placarA += 1;
                break;
            case R.id.team_b_3plus_bnt:
                this.placarB += 3;
                break;
            case R.id.team_b_2plus_bnt:
                this.placarB += 2;
                break;
            case R.id.team_b_1plus_bnt:
                this.placarB += 1;
                break;
        }

        this.displayTimeA();
        this.displayTimeB();
    }

    public void resetScore(View view){
        this.placarA=0;
        this.placarB=0;
        this.displayTimeA();
        this.displayTimeB();
    }

}
