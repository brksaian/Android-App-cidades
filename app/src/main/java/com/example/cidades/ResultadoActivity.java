package com.example.cidades;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class ResultadoActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);
        int resultado = getIntent().getIntExtra("resultado",0);
        textView = findViewById(R.id.textViewPontuacao);
        textView.setText(resultado + " pontos.");
    }
}