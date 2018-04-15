package com.example.pc.proyecto;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
            public void onClick(View v)
            {
                EditText nombreProducto = (EditText) findViewById(R.id.text_nombre);
                Spinner categoriaProducto = (Spinner) findViewById(R.id.sp_categoria);
                EditText precioProducto = (EditText) findViewById(R.id.text_precio);
                String nom=nombreProducto.getText().toString();
                String cat=categoriaProducto.getSelectedItem().toString();
                String pre=precioProducto.getText().toString();

                Producto c=new Producto();
                c.setNombre(nom);
                c.setCategoria(cat);
                c.setPrecio(Integer.parseInt(pre));
                basedatos.getWritableDatabase();
                aux=basedatos.agregarProducto(c);

                if(aux)
                {
                    Mensaje("se agrego con exito");
                    nombreProducto.setText("");
                    categoriaProducto.setSelection(0);
                    precioProducto.setText("");
                }
                else
                {
                    Mensaje("fallo al ingresar el producto");
                }


            }
        });
        Button pros= (Button) findViewById(R.id.btn_ver);
        pros.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intento = new Intent(getApplicationContext(), ActividadProductos.class);
                startActivity(intento);
            }
        });


        Button sup= (Button) findViewById(R.id.btn_maps);
        sup.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Log.i("onclick","si");
                Intent intento = new Intent(getApplicationContext(), MapsActivity.class);
                Log.i("creo el intento","si");
                startActivity(intento);
                Log.i("inicio el intento","si");

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


}
