package com.example.pm2e134;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LlamarActivity extends AppCompatActivity {

    TextView tvNombre, tvTelefono;
    Button btnCortar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamar);

        tvNombre = findViewById(R.id.tvNombreLlamada);
        tvTelefono = findViewById(R.id.tvTelefonoLlamada);
        btnCortar = findViewById(R.id.btnCortar);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("nombre") && intent.hasExtra("telefono")) {
            String nombre = intent.getStringExtra("nombre");
            String telefono = intent.getStringExtra("telefono");

            tvNombre.setText(nombre);
            tvTelefono.setText(telefono);
        } else {
            tvNombre.setText("Desconocido");
            tvTelefono.setText("Sin número");
        }

        btnCortar.setOnClickListener(v -> {
            Intent volver = new Intent(LlamarActivity.this, ListaActivity.class);
            volver.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(volver);
            finish();
        });
    }
}
