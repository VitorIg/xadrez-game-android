package com.example.trabalhojogo;

import static com.example.trabalhojogo.R.id.buttonFalar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeVelha extends AppCompatActivity {

    private Button buttonGameVelha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_velha);

        findViewById(R.id.buttonHomeXadrez).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HomeXadrez.class));
                overridePendingTransition(R.anim.slide_velha_xadrez, R.anim.slide_xadrez_velha);
            }
        });


        buttonGameVelha = findViewById(R.id.button_jogar_velha);
        buttonGameVelha.setOnClickListener(view -> {
            startActivity(new Intent(this, GameVelha.class));
        });
    }
}