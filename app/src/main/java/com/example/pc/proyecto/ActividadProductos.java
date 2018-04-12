package com.example.pc.proyecto;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

import static java.security.AccessController.getContext;

public class ActividadProductos extends AppCompatActivity {
    ArrayList<Producto> productosList = new ArrayList<Producto>();
   // RecyclerView estudiantesRecycler;
    BaseDeDatos basedatos;
    ArrayList<View> chs= new ArrayList<>();
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
            { Log.i("onclick","antes");
                calcular();
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
    @SuppressLint("NewApi")
    private void mostrarProductos()
    {


        LinearLayout panel= (LinearLayout) findViewById(R.id.linear_producs);

        for(int i=0; i<productosList.size();i++)
        {
            CheckBox ch= new CheckBox(this);
            ch.setId(i);
            ch.setText(productosList.get(i).getNombre());
            chs.add(ch);
            panel.addView(ch);

        }


    }
    private void calcular()
    {
        int aux=0;

        LinearLayout panel= (LinearLayout) findViewById(R.id.linear_producs);
        //Iterator <CheckBox> it= (Iterator<CheckBox>) panel.getChildAt(0);

        CheckBox ch;


        for(int i=0;i<productosList.size();i++)
        {
            ch= (CheckBox) panel.getChildAt(i);

            if(ch.isChecked())
            {
                aux+=productosList.get(i).precio;
            }

        }
        Mensaje("total :"+aux);
    }

    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();}

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
