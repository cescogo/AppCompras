package com.example.pc.proyecto;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.pc.proyecto.entities.Producto;

import java.util.ArrayList;

/**
 * Created by pc on 28/3/2018.
 */

public class BaseDeDatos extends SQLiteOpenHelper {
    public static final String DB_NAME="MercadoDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLA_Producto = "Producto";
    private static BaseDeDatos sInstance;

    public BaseDeDatos(Context context)
    {
        super(context,DB_NAME,null,DATABASE_VERSION);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
       // droptable(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String productos = "create table " + TABLA_Producto +
                "(" +// Define a primary key
                "nombre text, " +
                "categoria text, " +
                "precio integer," +
                "imagen text,"+
                "cantidad integer"+
                ");";
        db.execSQL(productos.toString());
        Log.i("Base de Datos", "Tabla Producto");


    }
    public void droptable(SQLiteDatabase db)
    {

        String productos = "drop table  Producto;";
        db.execSQL(productos.toString());
        onCreate(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLA_Producto);
            onCreate(db);
        }
    }


    //singleton para usar la misma instancia
    public static synchronized BaseDeDatos getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new BaseDeDatos(context.getApplicationContext());
        }
        return sInstance;
    }




// agregar producto

    public boolean agregarProducto(Producto e){
        try{
            SQLiteDatabase db=this.getWritableDatabase();
            db.execSQL("insert into Producto(nombre,categoria,precio,imagen,cantidad) values ('"+e.getNombre()+"', '"+e.getCategoria()+"', '"+e.getPrecio()+"','"+e.getFoto()+"','"+ e.getCantidad()+"');");
            return true;
        }catch (SQLiteException ex){
            Log.e("Base de Datos", "Excepcion en agregar Producto", ex);
            return false;
        }
    }

    //buscar producto

    public ArrayList<Producto> getListaProductos(){
        try{
            SQLiteDatabase db=this.getReadableDatabase();
            String query= "select * from Producto;";
            Cursor cursor = db.rawQuery(query,null);
            ArrayList<Producto> lista=new ArrayList<>();
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Producto aux=new Producto();
                    aux.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("nombre")));
                    aux.setCategoria(cursor.getString(cursor.getColumnIndexOrThrow("categoria")));
                    aux.setPrecio(cursor.getInt(cursor.getColumnIndexOrThrow("precio")));
                    aux.setFoto(cursor.getString(cursor.getColumnIndex("imagen")));
                    aux.setCantidad(cursor.getInt(cursor.getColumnIndex("cantidad")));
                    lista.add(aux);
                    cursor.moveToNext();
                }
            }

            return lista;

        }catch (SQLiteException ex){
            Log.e("Base de Datos", "Excepcion en getListaEstudiantes", ex);
            return null;
        }
    }
}
