package com.example.pc.proyecto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class ActividadProductos extends AppCompatActivity {
    ArrayList<Producto> productosList = new ArrayList<Producto>();
   // RecyclerView estudiantesRecycler;
    BaseDeDatos basedatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_productos);
        basedatos=BaseDeDatos.getInstance(this);
        initializeList();
        Button atras= (Button) findViewById(R.id.btn_atras);
        atras.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intento = new Intent(getApplicationContext(), Actividad1.class);
                startActivity(intento);
            }
        });

    }
    public void initializeList() {
        productosList.clear();
        productosList=basedatos.getListaProductos();
        mostrarProductos();

    }
    private void mostrarProductos()
    {
        LinearLayout panel= (LinearLayout) findViewById(R.id.linear_producs);

        Log.i("tamaño",String.valueOf(productosList.size()));
        for(int i=0; i<productosList.size();i++)
        {
            CheckBox ch= new CheckBox(this);
            Log.i("producto",productosList.get(i).getNombre());
            ch.setText(productosList.get(i).getNombre());
            panel.addView(ch);
        }

    }
}
