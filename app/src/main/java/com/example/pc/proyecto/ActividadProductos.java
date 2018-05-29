package com.example.pc.proyecto;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.pc.proyecto.entities.Producto;
import com.example.pc.proyecto.entities.Usuario;
import com.example.pc.proyecto.entities.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;




public class ActividadProductos extends AppCompatActivity {
    ArrayList<Producto> productosList = new ArrayList<Producto>();

    ProgressDialog progress;
    BaseDeDatos basedatos;

    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;


    private Producto prod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_productos);
        llenar_spinner();


        initializeList();
        basedatos=new BaseDeDatos(this);
        Button calcular= (Button) findViewById(R.id.bt_gen_list);
        calcular.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
            guardar();
            }
        });

    }
    public void initializeList() {

        webServiceLlenarLista();
    }
    @SuppressLint("NewApi")
    private void mostrarProductos(){ //se muestran los productos al usuario tomados del server
        LinearLayout panel= (LinearLayout) findViewById(R.id.linear_producs);

        for(int i=0; i<productosList.size();i++)
        {
            final CheckBox ch= new CheckBox(this);
            ch.setId(i);
            ch.setText(productosList.get(i).getNombre());

            ch.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mostrarDialogOpciones(ch.getId());
                    return true;

                }
            });

            ch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ch.setSelected(true);
                    MensajeCantidad(ch.getId());
                }
            });


            panel.addView(ch);


        }


    }
    private void guardar(){ // se guardan los productos seleccionados por el usuario en la bd local
        boolean bandera=false;

        LinearLayout panel= (LinearLayout) findViewById(R.id.linear_producs);


        CheckBox ch;
        basedatos.droptable(basedatos.getReadableDatabase());

        for(int i=0;i<productosList.size();i++)
        {
            ch= (CheckBox) panel.getChildAt(i);

            if(ch.isChecked())
            {
                basedatos.getWritableDatabase();
                bandera=basedatos.agregarProducto(productosList.get(i));

            }

        }
        if(bandera)
        {
            MensajeOK("Lista de compras generada satisfactoriamente");
        }
        else
        {
            MensajeOK("No se genero la lista de compras");
        }



    }



    public void MensajeOK(String msg){ // confirmacion de salir y elimina la cola de la aplicacion
        View v1 = getWindow().getDecorView().getRootView();
        AlertDialog.Builder builder1 = new AlertDialog.Builder( v1.getContext());
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { startActivity(new Intent(getBaseContext(), PrincipalActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                        finish();} });
        AlertDialog alert11 = builder1.create();
        alert11.show();
        ;}

    public void llenar_spinner() // anteriormente explicado
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
                        panel.getChildAt(i).setY(i*60);
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

    private void webServiceLlenarLista(){ // se obtienen todos los productos pertenecioentes a un usuario de la bd localizada en el server
        progress=new ProgressDialog(this);
        progress.setMessage("Cargando...");
        progress.show();

        final String ip=getString(R.string.ip2);

        String url=ip+"/webserver/producto/listaProductosUsuario.php?usuario="+ Usuario.USUARIO.getNombre();

        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progress.hide();

                Producto producto = null;

                JSONArray json=response.optJSONArray("producto");
                JSONObject jsonObject;

                try {
                    for (int i=0;i<json.length();i++){
                        producto = new Producto();
                        jsonObject = null;
                        jsonObject=json.getJSONObject(i);

                        producto.setNombre(jsonObject.optString("nombre"));
                        producto.setPrecio(jsonObject.optInt("precio"));
                        producto.setCategoria(jsonObject.optString("categoria"));
                        producto.setFoto(jsonObject.optString("imagen"));
                        productosList.add(producto);
                    }
                    mostrarProductos();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "No se ha podido establecer conexión con el servidor" +
                            " "+response, Toast.LENGTH_LONG).show();
                    progress.hide();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "no existen productos para este usuario", Toast.LENGTH_LONG).show();
                System.out.println();
                progress.hide();
                Log.d("ERROR: ", error.toString());
            }
        });

        // request.add(jsonObjectRequest);
        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(jsonObjectRequest);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) { //se setea la ccion del boton atras
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            MensajeSalir("Seguro que desea salir a la pantalla principal si no presiono generar perdera el avance en la seleccion ");
        }
        return super.onKeyDown(keyCode, event);
    }

    public void MensajeSalir(String msg) { // mensaaje de confirmacion para salir
        View v1 = getWindow().getDecorView().getRootView();
        AlertDialog.Builder builder1 = new AlertDialog.Builder( v1.getContext());
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {} });
        builder1.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(getBaseContext(), PrincipalActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                        finish();
                    } });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void MensajeCantidad(final int pos){ // pop up creada para tomar la cantidad de productos que desea comprar el usuario
        View view = (LayoutInflater.from(ActividadProductos.this)).inflate(R.layout.popup_cantidad, null);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ActividadProductos.this);
        alertBuilder.setView(view);
        //final EditText userInput = (EditText) view.findViewById(R.id.);
       final EditText edit= (EditText) view.findViewById(R.id.edit_num_prod);
        alertBuilder.setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                       // Producto produc= Producto.getInstance();
                        prod= productosList.get(pos);
                        prod.setCantidad(Integer.parseInt(edit.getText().toString()));

                    }
                });
        Dialog dialog = alertBuilder.create();
        dialog.show();
        ;}

    private void mostrarDialogOpciones(final int chec) {
        final CharSequence[] opciones={"Descripcion","Eliminar","Cancelar"};
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Elige una Opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Descripcion")){
                    Producto produc= Producto.getInstance();
                    prod= productosList.get(chec);
                    produc.setNombre(prod.getNombre());
                    produc.setCategoria(prod.getCategoria());
                    produc.setPrecio(prod.getPrecio());
                    produc.setFoto(prod.getFoto());
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    Detalles_Productos  mifrag = new Detalles_Productos ();
                    fragmentTransaction.replace(R.id.contenedor, mifrag, "Identificador1");
                    fragmentTransaction.commit();

                }else

                    if (opciones[i].equals("Eliminar")){
                            MensajeEliminar("seguro que desea eliminar este producto",chec);

                    }else{
                        dialogInterface.dismiss();
                    }

            }
        });
        builder.show();
    }

    private void webServiceDelete(final int chec) {
        progress.setMessage("Cargando...");
        progress.show();
        Producto produc= productosList.get(chec);
        String ip=getString(R.string.ip2);

        String url=ip+"/webserver/producto/borrarProducto.php?nombre="+produc.getNombre()+"&usuario="+Usuario.USUARIO.getNombre();//Cambiar aqui por el nombre y y el usuario del producto

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.hide();

                if (response.trim().equalsIgnoreCase("elimina")){
                  Toast.makeText(getBaseContext(),"Se ha Eliminado con exito",Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(getBaseContext(), ActividadProductos.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));


                }else{
                    if (response.trim().equalsIgnoreCase("noExiste")){
                        Toast.makeText(getBaseContext(),"No se encuentra la persona ",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }else{
                        Toast.makeText(getBaseContext(),"No se ha Eliminado ",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();
                progress.hide();
            }
        });
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(stringRequest);
    }

    public void MensajeEliminar(String msg,final int pos) { // mensaaje de confirmacion para salir
        View v1 = getWindow().getDecorView().getRootView();
        AlertDialog.Builder builder1 = new AlertDialog.Builder( v1.getContext());
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {} });
        builder1.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        webServiceDelete(pos);
                    } });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }



}
