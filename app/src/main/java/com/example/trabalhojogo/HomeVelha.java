package com.example.trabalhojogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeVelha extends AppCompatActivity {

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
    }
}