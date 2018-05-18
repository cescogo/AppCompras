package com.example.pc.proyecto.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by carmc_000 on 22/04/2018.
 */
// esta clase es la encargada de convertir la imagen en string y viseversa
public class Utilities {

    // este método en específico es el que convierte de imagen a String
    public static String getByte(Bitmap bitmap){
        ByteArrayOutputStream stream= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,30,stream);
        byte [] b=stream.toByteArray();
        String imgString = Base64.encodeToString(b,Base64.DEFAULT);
        return imgString;
    }


//Este método convierte de String a imagen para luego colocarla en el image view
    public static Bitmap getImage(String image){
        try {

            byte[] theByteArray = Base64.decode(image, Base64.NO_WRAP);
            Bitmap bitimage = BitmapFactory.decodeByteArray(theByteArray, 0, theByteArray.length);
            return bitimage;
        }catch (Exception e) {
            e.getMessage();
            return null;
        }
    }




}
