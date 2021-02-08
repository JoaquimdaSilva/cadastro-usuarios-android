package com.example.cadastrousuarios.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cadastrousuarios.R;

public class SplashController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_controller);
        getSupportActionBar().hide();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                abreListagemUsuarios();
            }
        }, 3000);
    }

    private void abreListagemUsuarios() {
        Intent i = new Intent(this, ListaUsuarios.class);
        startActivity(i);
        finish();
    }
}