package com.example.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {
    private BaseDatoEjemplo dbHelper;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {
        dbHelper = new BaseDatoEjemplo(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long agregarRegistro(String nombre, int edad) {
        ContentValues values = new ContentValues();
        values.put(BaseDatoEjemplo.COLUMN_NAME, nombre);
        values.put(BaseDatoEjemplo.COLUMN_AGE, edad);
        return database.insert(BaseDatoEjemplo.TABLE_NAME, null, values);
    }

    public Cursor obtenerTodosRegistros() {
        return database.query(BaseDatoEjemplo.TABLE_NAME, null, null, null, null, null, null);
    }

    public int actualizarRegistro(long id, String nombre, int edad) {
        ContentValues values = new ContentValues();
        values.put(BaseDatoEjemplo.COLUMN_NAME, nombre);
        values.put(BaseDatoEjemplo.COLUMN_AGE, edad);
        return database.update(BaseDatoEjemplo.TABLE_NAME, values, BaseDatoEjemplo.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }


    public int eliminarRegistro(long id) {
        return database.delete(BaseDatoEjemplo.TABLE_NAME, BaseDatoEjemplo.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }
}
