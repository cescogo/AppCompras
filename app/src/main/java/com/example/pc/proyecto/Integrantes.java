package com.example.pc.proyecto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Integrantes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integrantes);
        Button atras= (Button) findViewById(R.id.bt_atras_in);
        atras.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intento = new Intent(getApplicationContext(), PrincipalActivity.class);
                startActivity(intento);
            }
        });

    }
}
