package com.example.sqlite;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseManager gestorBD;
    private EditText campoNombre, campoEdad;
    private Button botonAgregar, botonEditar, botonEliminar;
    private ListView listaRegistros;
    private ArrayAdapter<String> adaptador;
    private ArrayList<String> listaDeRegistros;
    private long registroSeleccionadoId = -1;  // Para rastrear el registro seleccionado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gestorBD = new DatabaseManager(this);
        gestorBD.open();

        campoNombre = findViewById(R.id.editTextNombre);
        campoEdad = findViewById(R.id.editTextEdad);
        botonAgregar = findViewById(R.id.botonAgregar);
        botonEditar = findViewById(R.id.botonEditar);
        botonEliminar = findViewById(R.id.botonEliminar);
        listaRegistros = findViewById(R.id.listaRegistros);

        listaDeRegistros = new ArrayList<>();
        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaDeRegistros);
        listaRegistros.setAdapter(adaptador);

        cargarRegistros();

        botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = campoNombre.getText().toString();
                int edad = Integer.parseInt(campoEdad.getText().toString());
                gestorBD.agregarRegistro(nombre, edad);
                cargarRegistros();
                limpiarCampos();
            }
        });


        botonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (registroSeleccionadoId != -1) {
                    String nombre = campoNombre.getText().toString();
                    int edad = Integer.parseInt(campoEdad.getText().toString());
                    gestorBD.actualizarRegistro(registroSeleccionadoId, nombre, edad);
                    cargarRegistros();
                    limpiarCampos();
                    registroSeleccionadoId = -1;
                    Toast.makeText(MainActivity.this, "Registro actualizado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Selecciona un registro primero", Toast.LENGTH_SHORT).show();
                }
            }
        });


        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (registroSeleccionadoId != -1) {
                    gestorBD.eliminarRegistro(registroSeleccionadoId);
                    cargarRegistros();
                    limpiarCampos();
                    registroSeleccionadoId = -1;
                    Toast.makeText(MainActivity.this, "Registro eliminado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Selecciona un registro primero", Toast.LENGTH_SHORT).show();
                }
            }
        });


        listaRegistros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = gestorBD.obtenerTodosRegistros();
                cursor.moveToPosition(position);
                registroSeleccionadoId = cursor.getLong(cursor.getColumnIndexOrThrow(BaseDatoEjemplo.COLUMN_ID));
                campoNombre.setText(cursor.getString(cursor.getColumnIndexOrThrow(BaseDatoEjemplo.COLUMN_NAME)));
                campoEdad.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(BaseDatoEjemplo.COLUMN_AGE))));
                cursor.close();
            }
        });
    }

    private void cargarRegistros() {
        listaDeRegistros.clear();
        Cursor cursor = gestorBD.obtenerTodosRegistros();

        if (cursor.moveToFirst()) {
            do {
                String registro = "ID: " + cursor.getInt(cursor.getColumnIndexOrThrow(BaseDatoEjemplo.COLUMN_ID)) +
                        ", Nombre: " + cursor.getString(cursor.getColumnIndexOrThrow(BaseDatoEjemplo.COLUMN_NAME)) +
                        ", Edad: " + cursor.getInt(cursor.getColumnIndexOrThrow(BaseDatoEjemplo.COLUMN_AGE));
                listaDeRegistros.add(registro);
            } while (cursor.moveToNext());
        }

        cursor.close();
        adaptador.notifyDataSetChanged();
    }

    private void limpiarCampos() {
        campoNombre.setText("");
        campoEdad.setText("");
    }

    @Override
    protected void onDestroy() {
        gestorBD.close();
        super.onDestroy();
    }
}
