package com.example.pc.proyecto;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.proyecto.entities.Producto;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class Actividad1 extends AppCompatActivity {
    BaseDeDatos basedatos;
    boolean aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad1);
        llenar_spinner();
        basedatos=new BaseDeDatos(this);
        Button agreg= (Button) findViewById(R.id.btn_agregar);

        agreg.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                if (!Vacio()) {
                    EditText nombreProducto = (EditText) findViewById(R.id.text_nombre);
                    Spinner categoriaProducto = (Spinner) findViewById(R.id.sp_categoria);
                    EditText precioProducto = (EditText) findViewById(R.id.text_precio);
                    String nom = nombreProducto.getText().toString();
                    String cat = categoriaProducto.getSelectedItem().toString();
                    String pre = precioProducto.getText().toString();

                    Producto c = new Producto();
                    c.setNombre(nom);
                    c.setCategoria(cat);
                    c.setPrecio(Integer.parseInt(pre));
                    basedatos.getWritableDatabase();
                    aux = basedatos.agregarProducto(c);

                    if (aux) {
                        Mensaje("se agrego con exito");
                        nombreProducto.setText("");
                        categoriaProducto.setSelection(0);
                        precioProducto.setText("");
                    } else {
                        Mensaje("fallo al ingresar el producto");
                    }


                }
            }
        });
        Button pros= (Button) findViewById(R.id.but_cancel);
        pros.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                EditText nombreProducto = (EditText) findViewById(R.id.text_nombre);
                Spinner categoriaProducto = (Spinner) findViewById(R.id.sp_categoria);
                EditText precioProducto = (EditText) findViewById(R.id.text_precio);
                nombreProducto.setText("");
                categoriaProducto.setSelection(0);
                precioProducto.setText("");
            }
        });



    }

    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();}

    public void llenar_spinner()
    {
        Spinner s1;
        final String[] presidents = {
                "Seleccione una categoria",
                "Papelería y Limpieza",
                "Bebidas",
                "Carnes",
                "Higiene personal",
                "Platillos congelados preparados",
                "Lácteos",
                "Frutas",
                "Verduras",
                "Huevos",
                "Abarrotes",
                "Panadería y postres",
                "Salsas"
        };

        //---Spinner View---
        s1 = (Spinner) findViewById(R.id.sp_categoria);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, presidents);




        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        s1.setAdapter(adapter);


    }

    private boolean Vacio()
    {
        EditText nombreProducto = (EditText) findViewById(R.id.text_nombre);
        Spinner categoriaProducto = (Spinner) findViewById(R.id.sp_categoria);
        EditText precioProducto = (EditText) findViewById(R.id.text_precio);
        if(nombreProducto.length()==0 || precioProducto.length()==0)
        {
            MensajeOK("Campos de precio o productos vacio ");
            return true;
        }
        else if(categoriaProducto.getSelectedItem().toString().equals("Seleccione una categoria"))
        {
            MensajeOK("Seleccione una categoria");
            return true;
        }
        else {return false;}
    }
    public void MensajeOK(String msg){
        View v1 = getWindow().getDecorView().getRootView();
        AlertDialog.Builder builder1 = new AlertDialog.Builder( v1.getContext());
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {} });
        AlertDialog alert11 = builder1.create();
        alert11.show();
        ;}


}