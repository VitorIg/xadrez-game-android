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
        buttonGameXadrez = findViewById(R.id.button_jogar);
        buttonGameXadrez.setOnClickListener(view -> {
            startActivity(new Intent(this, GameXadrez.class));
        });
    }
}