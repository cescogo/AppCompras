package com.example.pc.proyecto;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.proyecto.entities.Producto;
import com.example.pc.proyecto.entities.Utilities;


public class Detalles_Productos extends Fragment {

    Utilities imageAction = new Utilities();

    public Detalles_Productos() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, //fragment para mostrar la informacion del producto mas la imagen
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_detalles__productos, container, false);
        Producto producto=Producto.getInstance();

        TextView nom= (TextView) view.findViewById(R.id.text_nombre);
        TextView categoria= (TextView) view.findViewById(R.id.text_categoria);
        TextView precio= (TextView) view.findViewById(R.id.text_precio);
        nom.setText(producto.getNombre());
        categoria.setText(producto.getCategoria());
        precio.setText(String.valueOf(producto.getPrecio()));
        ImageView imagen = (ImageView) view.findViewById(R.id.ima_fragment);
        if(producto.getFoto().equals(" "))
        {

        }
        else
        {
            Bitmap load = imageAction.getImage(producto.getFoto()); // seteo de la imagen convirtiendo de string a imagen
            imagen.setImageBitmap(load);
        }


        //return view;
        return view;
        // return inflater.inflate(R.layout.fragment_blank_fragment01, container, false);

    }






}
