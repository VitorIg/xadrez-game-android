package com.example.trabalhojogo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class GameVelha extends AppCompatActivity implements View.OnClickListener {

    //   private ImageView botaoCentro;
    private Button []btn = new Button[9];
    private Button resetGame;
    private TextView placarPrimeiroJogador, placarSegundoJogador;
    private boolean jogador;

    private int numJogadas;

    private int [] jogadas = {0,0,0,0,0,0,0,0,0};

    private  int [][] vitoria = {
            {0,1,2},{3,4,5},{6,7,8},
            {0,3,6},{1,4,7},{2,5,8},
            {0,4,8},{2,4,6}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_velha);

        placarPrimeiroJogador = findViewById(R.id.placarPrimeiroJogador);
        placarSegundoJogador = findViewById(R.id.placarSegundoJogador);
        resetGame = findViewById(R.id.resetGame);


        for (int i = 0; i < btn.length; i++) {
            int resId = getResources().getIdentifier("btn_"+i, "id", getPackageName());
            btn[i] = findViewById(resId);
            btn[i].setOnClickListener(this);
        }

        jogador = true;
        placarPrimeiroJogador.setText("0");
        placarSegundoJogador.setText("0");
        numJogadas = 1;

        resetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placarPrimeiroJogador.setText("0");
                placarSegundoJogador.setText("0");
                resetGame();


            }
        });


    }

    @Override
    public void onClick(View view) {


        if (!((Button)view).getText().toString().equals("")){
            return;
        }
        int numBtn = Integer.parseInt(view.getResources().getResourceEntryName(view.getId()).substring(4,5));

        if (jogador){
            ((Button)view).setText("X");
            ((Button)view).setTextSize(50);
            jogadas[numBtn] = 1;
            ((Button)view).setTextColor(Color.RED);

        }else {
            ((Button)view).setText("O");
            ((Button)view).setTextSize(50);
            ((Button)view).setTextColor(Color.BLUE);
            jogadas[numBtn] = 2;
        }
        if (venceu()){
            if (jogador){
                int placar = Integer.parseInt(placarPrimeiroJogador.getText().toString()) + 1;
                placarPrimeiroJogador.setText(Integer.toString(placar));
                Toast.makeText(this, "Primeiro jogador venceu", Toast.LENGTH_SHORT).show();
            } else {
                int placar = Integer.parseInt(placarSegundoJogador.getText().toString()) + 1;
                placarSegundoJogador.setText(Integer.toString(placar));
                Toast.makeText(this, "Segundo jogador venceu", Toast.LENGTH_SHORT).show();
            }
            resetGame();

        }else if (numJogadas == 9){
            Toast.makeText(view.getContext(),"deu velha", Toast.LENGTH_SHORT).show();
        resetGame();

        }else {
            numJogadas++;
            jogador = !jogador;
        }
    }

    private void resetGame() {
        numJogadas = 1;
        jogador = true;
        for (int i = 0; i < btn.length; i++){
            btn[i].setText("");
            jogadas[i] = 0;
        }
    }

    private boolean venceu() {
        boolean resuldado = false;
        for (int [] jogasefetuadas: vitoria){
            if (jogadas[jogasefetuadas[0]]==jogadas[jogasefetuadas[1]] &&
                    jogadas[jogasefetuadas[1]]==jogadas[jogasefetuadas[2]]&&
                    jogadas[jogasefetuadas[0]]!=0) {
                resuldado = true;
            }
        }

        return  resuldado;
    }
}
