package com.example.pc.proyecto;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.pc.proyecto.entities.Producto;
import com.example.pc.proyecto.entities.Usuario;
import com.example.pc.proyecto.entities.Utilities;
import com.example.pc.proyecto.entities.VolleySingleton;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Actividad1 extends AppCompatActivity {

    private int PICK_IMAGE = 1;
    Producto c;
    Utilities imageAction = new Utilities();
Boolean aux;
    EditText nombreProducto;
    Spinner categoriaProducto;
    EditText precioProducto;
    ImageView imagen;

    StringRequest stringRequest;
    JsonObjectRequest jsonObjectRequest;

    ProgressDialog progress;

    protected void onCreate(Bundle savedInstanceState){ //Se crean todas las instancias que se utilizan en la actividad
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad1);

        nombreProducto = (EditText) findViewById(R.id.text_nombre);
        categoriaProducto = (Spinner) findViewById(R.id.sp_categoria);
        precioProducto = (EditText) findViewById(R.id.text_categoria);
        imagen = (ImageView) findViewById(R.id.imagen_agregar);

        llenar_spinner();

        Button agreg= (Button) findViewById(R.id.btn_agregar);
        c = new Producto();
        agreg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                    loadWebServiceRegister();

            }
        });
        Button pros= (Button) findViewById(R.id.but_cancel);
        pros.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                nombreProducto.setText("");
                categoriaProducto.setSelection(0);
                precioProducto.setText("");
            }
        });
        imagen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Seleccione la foto.."),PICK_IMAGE);

            }
        });



    }



    public void llenar_spinner() // se llena el spinner de las categorias
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

    private boolean Vacio() { //se validan los campos para que no vayan datos incorectos hacia la bd

        EditText nombreProducto = (EditText) findViewById(R.id.text_nombre);
        Spinner categoriaProducto = (Spinner) findViewById(R.id.sp_categoria);
        EditText precioProducto = (EditText) findViewById(R.id.text_categoria);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//se convierte de imagen a string para ser almacenada en la bd
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            if (data != null) {
                Uri imageUri = data.getData();
                try {
                    ImageView imagen = (ImageView) findViewById(R.id.imagen_agregar);
                    InputStream stream = getContentResolver().openInputStream(imageUri);
                    Bitmap option = BitmapFactory.decodeStream(stream);
                    imagen.setImageBitmap(option);

                    c.setFoto(imageAction.getByte(option));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
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


    private void loadWebServiceRegister() { //se envia la peticion al sefvidor para guardar el objeto producto en la bd del server
        if (!Vacio()) {
            progress = new ProgressDialog(this);
            progress.setMessage("Cargando....");
            progress.show();

            String ip = getString(R.string.ip2);
            String url = ip + "/webserver/producto/registroProducto.php?";
            stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progress.hide();

                            if (response.trim().equalsIgnoreCase("registra")) {
                                nombreProducto.setText("");
                                categoriaProducto.setSelection(0);
                                precioProducto.setText("");

                                imagen.setImageResource(R.drawable.ic_menu_gallery);
                                Toast.makeText(getBaseContext(), "Se ha registrado con exito", Toast.LENGTH_SHORT).show();
                                aux=true;
                            } else {
                                Toast.makeText(getBaseContext(), "No se ha registrado ", Toast.LENGTH_SHORT).show();
                                Log.i("RESPUESTA: ", "" + response);
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getBaseContext(), "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                    progress.hide();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    if(c.getFoto()== null)
                    {
                        c.setFoto(" ");
                    }
                    String nombre = nombreProducto.getText().toString();
                    String categoria = categoriaProducto.getSelectedItem().toString();
                    String precio = precioProducto.getText().toString();
                    String imagen = c.getFoto();
                    String usuario = Usuario.USUARIO.getNombre();

                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("nombre", nombre);
                    parametros.put("categoria", categoria);
                    parametros.put("precio", precio);
                    parametros.put("imagen", imagen);
                    parametros.put("usuario", usuario);

                    return parametros;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getIntanciaVolley(getBaseContext()).addToRequestQueue(stringRequest);
        }

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) { //se modifica la accion al presionar el botton de atras del celular
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            MensajeSalir("Seguro que desea salir de la aplicación");
        }
        return super.onKeyDown(keyCode, event);
    }

    public void MensajeSalir(String msg) { //pop up de confirmacion para salir de esta actividad
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
}