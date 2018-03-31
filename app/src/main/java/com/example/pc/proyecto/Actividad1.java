package com.example.pc.proyecto;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static java.security.AccessController.getContext;

public class Actividad1 extends AppCompatActivity {
    BaseDeDatos basedatos;
    boolean aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad1);
        basedatos=new BaseDeDatos(this);
        Button agreg= (Button) findViewById(R.id.btn_agregar);
        agreg.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                EditText nombreProducto = (EditText) findViewById(R.id.text_nombre);
                EditText categoriaProducto = (EditText) findViewById(R.id.text_categoria);
                EditText precioProducto = (EditText) findViewById(R.id.text_precio);
                String nom=nombreProducto.getText().toString();
                String cat=categoriaProducto.getText().toString();
                String pre=precioProducto.getText().toString();
                Log.i("nombre",nom.toString());
                Log.i("catego",cat.toString());
                Log.i("precio",pre.toString());
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
                    categoriaProducto.setText("");
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
    }

    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};


}
