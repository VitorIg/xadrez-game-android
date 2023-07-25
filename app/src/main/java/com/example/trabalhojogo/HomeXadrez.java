package com.example.trabalhojogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeXadrez extends AppCompatActivity {

    private Button buttonGameXadrez;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_xadrez);

        findViewById(R.id.buttonHomeVelha).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), GameXadrez.class));
                overridePendingTransition(R.anim.slide_xadrez_velha, R.anim.slide_velha_xadrez);
            }
        });

        buttonGameXadrez = findViewById(R.id.button_jogar);
        buttonGameXadrez.setOnClickListener(view -> {
            startActivity(new Intent(this, GameXadrez.class));
        });
    }
}