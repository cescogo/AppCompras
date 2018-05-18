package com.example.pc.proyecto;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.pc.proyecto.entities.Producto;
import com.example.pc.proyecto.entities.Utilities;

import java.util.ArrayList;




public class ActividadProductos extends AppCompatActivity {
    ArrayList<Producto> productosList = new ArrayList<Producto>();
   // RecyclerView estudiantesRecycler;
    BaseDeDatos basedatos;

    ArrayList<View> chs= new ArrayList<>();
    private Producto prod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_productos);
        llenar_spinner();
        basedatos=BaseDeDatos.getInstance(this);

        initializeList();

        Button calcular= (Button) findViewById(R.id.bt_calc);
        calcular.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

            calcular();
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

            final CheckBox ch= new CheckBox(this);
            ch.setId(i);
            ch.setText(productosList.get(i).getNombre());

            ch.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Producto produc= Producto.getInstance();
                    prod= productosList.get(ch.getId());
                    produc.setNombre(prod.getNombre());
                    produc.setCategoria(prod.getCategoria());
                    produc.setPrecio(prod.getPrecio());
                    produc.setFoto(prod.getFoto());
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    Detalles_Productos  mifrag = new Detalles_Productos ();
                    fragmentTransaction.replace(R.id.contenedor, mifrag, "Identificador1");
                    fragmentTransaction.commit();

                    return true;
                }
            });


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
                aux+=productosList.get(i).getPrecio();
            }

        }
        MensajeOK("total a cancelar :"+aux);
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

    public void llenar_spinner()
    {
        Spinner s1;
        final String[] presidents = {
                "Todas",
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
        s1 = (Spinner) findViewById(R.id.sp_categorias2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, presidents);




        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                LinearLayout panel= (LinearLayout) findViewById(R.id.linear_producs);
               Spinner s1 = (Spinner) findViewById(R.id.sp_categorias2);

                String aux= s1.getSelectedItem().toString();
                if(aux=="Todas")
                {
                    for(int i=0;i<productosList.size();i++)
                    {
                        panel.getChildAt(i).setVisibility(View.VISIBLE);
                        panel.getChildAt(i).setY(i*80);
                    }

                }
                else
                {
                    int cont=0;
                    for(int i=0;i<productosList.size();i++)
                    {
                        if(!aux.toString().equals(productosList.get(i).getCategoria()))
                        {
                            panel.getChildAt(i).setVisibility(View.INVISIBLE);
                            panel.getChildAt(i).setY(0);

                            // panel.getChildAt(i).setEnabled(false);
                        }
                        else {
                            panel.getChildAt(i).setVisibility(View.VISIBLE);
                            panel.getChildAt(i).setY(cont);
                            cont+=80;
                        }

                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        s1.setAdapter(adapter);


    }



}
