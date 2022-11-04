package com.example.cidades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    String[] cidades = {"barcelona", "brasilia", "curitiba", "moscou", "sidney", "buenosaires", "washington", "roma", "paris", "atenas"};
    String[] nomesCorretosCidade = {"Barcelona", "Brasília", "Curitiba", "Moscou", "Sidney", "Buenos Aires", "Washington", "Roma", "Paris", "Atenas"};
    String[] cidadesUrl = {"01_barcelona", "02_brasilia", "03_curitiba", "04_moscou", "05_sidney", "06_buenosaires", "07_washington", "08_roma", "09_paris", "10_atenas"};
    ArrayList<Integer> sorteados = new ArrayList<>();
    TextView textViewProgresso, textViewResultado;
    EditText editTextCidade;
    ImageView imageViewCidade;

    int sorter;
    String cidadeSorteada;
    Button button;
    int pontuacao = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewProgresso = findViewById(R.id.textViewProgresso);
        textViewResultado = findViewById(R.id.textViewResultado);
        editTextCidade = findViewById(R.id.editTextCidade);
        imageViewCidade = findViewById(R.id.imageViewCidade);

        textViewResultado.setText("");
        button = findViewById(R.id.button);
        realizarSorteio();

    }

    public void guess(View view) {
        String resposta = editTextCidade.getText().toString();
        if (resposta.length() == 0) {
            Toast.makeText(this, "Digite uma resposta", Toast.LENGTH_SHORT).show();
        } else {
            resposta = resposta.toLowerCase();
            resposta = resposta.trim().replace(" ", "");
            resposta = Normalizer.normalize(resposta, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
            if (resposta.equals(cidadeSorteada)) {
                textViewResultado.setText("Você acertou!");
                pontuacao += 20;
            } else {
                textViewResultado.setText("Você errou, a resposta certa era: " + nomesCorretosCidade[sorter]);
            }
        }

        if (sorteados.size() == 5) {
            editTextCidade.setFocusable(false);
            button.setText("Ver resultado");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, ResultadoActivity.class);
                    i.putExtra("resultado", pontuacao);
                    i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(i);
                }
            });
        } else {
            editTextCidade.setText("");
            realizarSorteio();
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
         ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private void realizarSorteio(){
        Random gerador = new Random();
        sorter = gerador.nextInt(10);
        while(sorteados.contains(sorter)){
            sorter = gerador.nextInt(10);
        }
        sorteados.add(sorter);
        cidadeSorteada = cidades[sorter];

        new DownloadImageTask((ImageView) imageViewCidade)
                .execute("http://200.236.3.202/android/cidades/"+ cidadesUrl[sorter] + ".jpg");

        textViewProgresso.setText(sorteados.size()+"/5");
    }

}