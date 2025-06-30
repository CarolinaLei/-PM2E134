package com.example.pm2e134;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    EditText etNombre, etTelefono, etNota;
    Spinner spPais;
    Button btnGuardar, btnVer;
    ImageView imageView;
    Uri imagenUri;
    DBHelper db;
    final int CODIGO_IMAGEN = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spPais = findViewById(R.id.spPais);
        etNombre = findViewById(R.id.etNombre);
        etTelefono = findViewById(R.id.etTelefono);
        etNota = findViewById(R.id.etNota);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnVer = findViewById(R.id.btnVerContactos);
        imageView = findViewById(R.id.imageView);
        db = new DBHelper(this);

        String[] paises = {"Honduras (504)", "Costa Rica", "Guatemala (502)", "El Salvador"};
        spPais.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, paises));

        imageView.setOnClickListener(v -> {
            Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galeria, CODIGO_IMAGEN);
        });

        btnGuardar.setOnClickListener(v -> {
            if (validar()) {
                db.insertarContacto(
                        spPais.getSelectedItem().toString(),
                        etNombre.getText().toString(),
                        etTelefono.getText().toString(),
                        etNota.getText().toString(),
                        imagenUri != null ? imagenUri.toString() : ""
                );
                Toast.makeText(this, "Contacto guardado", Toast.LENGTH_SHORT).show();
                limpiarCampos();
            }
        });

        btnVer.setOnClickListener(v -> startActivity(new Intent(this, ListaActivity.class)));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODIGO_IMAGEN && resultCode == RESULT_OK && data != null) {
            imagenUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagenUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validar() {
        if (etNombre.getText().toString().isEmpty()) {
            etNombre.setError("debe escribir un nombre");
            return false;
        }
        if (!etTelefono.getText().toString().matches("[0-9]{8}")) {
            etTelefono.setError("debe escribir un teléfono válido");
            return false;
        }
        if (etNota.getText().toString().isEmpty()) {
            etNota.setError("debe escribir una nota");
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        etNombre.setText("");
        etTelefono.setText("");
        etNota.setText("");
        imageView.setImageResource(android.R.drawable.ic_menu_gallery);
        imagenUri = null;
    }
}