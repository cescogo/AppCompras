package com.example.pc.proyecto;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.pc.proyecto.R;
import com.example.pc.proyecto.entities.Usuario;
import com.example.pc.proyecto.entities.VolleySingleton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserActivity extends AppCompatActivity{

    EditText edtUsername, edtPassword;
    Button btnReg, btnLog;
    ProgressDialog progress;

    StringRequest stringRequest;
    JsonObjectRequest jsonObjectRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnReg = (Button) findViewById(R.id.btnReg);
        btnLog = (Button) findViewById(R.id.btnLog);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadWebServiceRegister();
            }
        });
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadWebServiceLogin();
            }
        });
    }

    private void loadWebServiceRegister() {
        progress = new ProgressDialog(this);
        progress.setMessage("Cargando....");
        progress.show();

        String ip = getString(R.string.ip2);
        String url=ip+"/WebServer/wsJSONRegistroMovil.php?";
        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progress.hide();

                        if (response.trim().equalsIgnoreCase("registra")){
                            edtUsername.setText("");
                            edtPassword.setText("");
                            Toast.makeText(getBaseContext(),"Se ha registrado con exito",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getBaseContext(),"No se ha registrado ",Toast.LENGTH_SHORT).show();
                            Log.i("RESPUESTA: ",""+response);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();
                progress.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String nombre = edtUsername.getText().toString();
                String pass = edtPassword.getText().toString();

                Map<String, String> parametros = new HashMap<>();
                parametros.put("nombre", nombre);
                parametros.put("pass", pass);

                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getBaseContext()).addToRequestQueue(stringRequest);
    }

    private void loadWebServiceLogin() {
        progress=new ProgressDialog(this);
        progress.setMessage("Cargando...");
        progress.show();

        final String ip=getString(R.string.ip2);
        String url=ip+"/WebServer/wsJSONConsultarUsuario.php?nombre="+edtUsername.getText().toString()+
                "&pass="+edtPassword.getText().toString();

        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progress.hide();

                Usuario miUsuario=new Usuario();

                JSONArray json=response.optJSONArray("usuario");
                JSONObject jsonObject=null;

                try {
                    jsonObject=json.getJSONObject(0);
                    miUsuario.setNombre(jsonObject.optString("nombre"));
                    miUsuario.setPass(jsonObject.optString("pass"));
                    if(!miUsuario.getNombre().equals("no registrado") && !miUsuario.getPass().equals("no registrado")) {
                        Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        MensajeOK("Usuario o contraseña incorrecta");
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
                System.out.println();
                progress.hide();
                Log.d("ERROR: ", error.toString());
            }
        });

        // request.add(jsonObjectRequest);
        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(jsonObjectRequest);
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
