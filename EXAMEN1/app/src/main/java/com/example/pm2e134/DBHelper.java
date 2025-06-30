package com.example.pm2e134;

import com.example.pm2e134.Contacto;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ContactosDB.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE contactos (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "pais TEXT, " +
                        "nombre TEXT, " +
                        "telefono TEXT, " +
                        "nota TEXT, " +
                        "imagenUri TEXT" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contactos");
        onCreate(db);
    }

    // Insertar contacto
    public void insertarContacto(String pais, String nombre, String telefono, String nota, String imagenUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pais", pais);
        values.put("nombre", nombre);
        values.put("telefono", telefono);
        values.put("nota", nota);
        values.put("imagenUri", imagenUri);
        db.insert("contactos", null, values);
        db.close();
    }

    // Obtener lista de contactos
    public ArrayList<Contacto> obtenerContactos() {
        ArrayList<Contacto> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM contactos", null);

        if (cursor.moveToFirst()) {
            do {
                Contacto c = new Contacto();
                c.setId(cursor.getInt(0));
                c.setPais(cursor.getString(1));
                c.setNombre(cursor.getString(2));
                c.setTelefono(cursor.getString(3));
                c.setNota(cursor.getString(4));
                c.setImagenUri(cursor.getString(5));
                lista.add(c);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return lista;
    }


    public void eliminarContacto(Contacto contacto) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("contactos", "id = ?", new String[]{String.valueOf(contacto.getId())});
        db.close();
    }


    public Contacto obtenerContactoPorId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM contactos WHERE id = ?", new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            Contacto c = new Contacto();
            c.setId(cursor.getInt(0));
            c.setPais(cursor.getString(1));
            c.setNombre(cursor.getString(2));
            c.setTelefono(cursor.getString(3));
            c.setNota(cursor.getString(4));
            c.setImagenUri(cursor.getString(5));
            cursor.close();
            db.close();
            return c;
        }

        cursor.close();
        db.close();
        return null;
    }


    public void actualizarContacto(Contacto c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pais", c.getPais());
        values.put("nombre", c.getNombre());
        values.put("telefono", c.getTelefono());
        values.put("nota", c.getNota());
        values.put("imagenUri", c.getImagenUri());
        db.update("contactos", values, "id = ?", new String[]{String.valueOf(c.getId())});
        db.close();
    }
}
