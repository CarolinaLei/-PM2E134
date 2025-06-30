package com.example.pm2e134;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListaActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Contacto> contactos;
    DBHelper db;
    ArrayAdapter<String> adapter;
    ArrayList<String> listaNombres;
    EditText etBuscar;
    Button btnListar, btnEliminar, btnActualizar, btnCompartir, btnVerImagen;
    int contactoSeleccionado = -1;
    long ultimoClick = 0;
    final long DOBLE_CLICK_TIEMPO = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        listView = findViewById(R.id.lvContactos);
        etBuscar = findViewById(R.id.etBuscar);
        btnListar = findViewById(R.id.btnListar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnCompartir = findViewById(R.id.btnCompartir);
        btnVerImagen = findViewById(R.id.btnVerImagen);

        db = new DBHelper(this);
        cargarContactos("");

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            long tiempoActual = System.currentTimeMillis();
            if (tiempoActual - ultimoClick < DOBLE_CLICK_TIEMPO) {
                Contacto contacto = contactos.get(position);
                Intent intent = new Intent(this, LlamarActivity.class);
                intent.putExtra("nombre", contacto.getNombre());
                intent.putExtra("telefono", contacto.getTelefono());
                startActivity(intent);
            } else {
                contactoSeleccionado = position;
                etBuscar.setText(contactos.get(position).getNombre());
            }
            ultimoClick = tiempoActual;
        });

        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cargarContactos(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnListar.setOnClickListener(v -> cargarContactos(etBuscar.getText().toString()));

        btnEliminar.setOnClickListener(v -> {
            if (contactoSeleccionado >= 0) {
                db.eliminarContacto(contactos.get(contactoSeleccionado));
                cargarContactos(etBuscar.getText().toString());
                Toast.makeText(this, "Contacto eliminado", Toast.LENGTH_SHORT).show();
                contactoSeleccionado = -1;
            } else {
                Toast.makeText(this, "Seleccione un contacto", Toast.LENGTH_SHORT).show();
            }
        });

        btnActualizar.setOnClickListener(v -> {
            if (contactoSeleccionado >= 0) {
                Contacto c = contactos.get(contactoSeleccionado);
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("modo_edicion", true);
                intent.putExtra("id", c.getId());
                intent.putExtra("nombre", c.getNombre());
                intent.putExtra("telefono", c.getTelefono());
                intent.putExtra("pais", c.getPais());
                intent.putExtra("nota", c.getNota());
                intent.putExtra("imagenUri", c.getImagenUri());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Seleccione un contacto", Toast.LENGTH_SHORT).show();
            }
        });

        btnCompartir.setOnClickListener(v -> {
            if (contactoSeleccionado >= 0) {
                Contacto c = contactos.get(contactoSeleccionado);
                Intent compartir = new Intent(Intent.ACTION_SEND);
                compartir.setType("text/plain");
                compartir.putExtra(Intent.EXTRA_TEXT,
                        "Nombre: " + c.getNombre() +
                                "\nTel: " + c.getTelefono() +
                                "\nPaís: " + c.getPais() +
                                "\nNota: " + c.getNota());
                startActivity(Intent.createChooser(compartir, "Compartir contacto"));
            } else {
                Toast.makeText(this, "Seleccione un contacto", Toast.LENGTH_SHORT).show();
            }
        });

        btnVerImagen.setOnClickListener(v -> {
            if (contactoSeleccionado >= 0) {
                String uri = contactos.get(contactoSeleccionado).getImagenUri();
                if (!uri.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(uri), "image/*");
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "No hay imagen", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Seleccione un contacto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarContactos(String filtro) {
        contactos = db.obtenerContactos();
        listaNombres = new ArrayList<>();
        for (Contacto c : contactos) {
            if (c.getNombre().toLowerCase().contains(filtro.toLowerCase())) {
                listaNombres.add(c.getNombre() + " - " + c.getTelefono());
            }
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaNombres);
        listView.setAdapter(adapter);
    }
}
