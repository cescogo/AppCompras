package com.example.pc.proyecto;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.security.AccessControlContext;
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
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String productos = "create table " + TABLA_Producto +
                "(" +// Define a primary key
                "nombre text, " +
                "categoria text, " +
                "precio integer" +
                ");";
        db.execSQL(productos.toString());
        Log.i("Base de Datos", "Tabla Producto");


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
            db.execSQL("insert into Producto(nombre,categoria,precio) values ('"+e.getNombre()+"', '"+e.getCategoria()+"', '"+e.getPrecio()+"');");
            return true;
        }catch (SQLiteException ex){
            Log.e("Base de Datos", "Excepcion en agregar Producto", ex);
            return false;
        }
    }

   // buscar producto
   /*
    public Estudiante buscarEstudiante(String nombre, String apellido1, String apellido2, int edad){
        try{
            SQLiteDatabase db=this.getReadableDatabase();
            String query= "select * from Estudiante where nombre='"+nombre+"' and apellido1='"+apellido1+"' and apellido2='"+apellido2+"' and edad='"+edad+"';";
            Cursor cursor = db.rawQuery(query,null);
            Estudiante aux=new Estudiante();
            aux.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            aux.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("nombre")));
            aux.setApellido1(cursor.getString(cursor.getColumnIndexOrThrow("apellido1")));
            aux.setApellido2(cursor.getString(cursor.getColumnIndexOrThrow("apellido2")));
            aux.setEdad(cursor.getInt(cursor.getColumnIndexOrThrow("edad")));


            return aux;

        }catch (SQLiteException ex){
            Log.e("Base de Datos", "Excepcion en bucarEstudiante", ex);
            return null;
        }
    }*/

// producto por di
    /*
    public Estudiante estudianteById(int id){
        try{
            SQLiteDatabase db=this.getReadableDatabase();
            String query= "select * from Estudiante where id='"+id+"';";
            Cursor cursor = db.rawQuery(query,null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Estudiante aux=new Estudiante();
                    aux.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    aux.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("nombre")));
                    aux.setApellido1(cursor.getString(cursor.getColumnIndexOrThrow("apellido1")));
                    aux.setApellido2(cursor.getString(cursor.getColumnIndexOrThrow("apellido2")));
                    aux.setEdad(cursor.getInt(cursor.getColumnIndexOrThrow("edad")));
                    return aux;
                }
            }
            return null;

        }catch (SQLiteException ex){
            Log.e("Base de Datos", "Excepcion en estudianteById", ex);
            return null;
        }
    }*/

    /*
    public boolean updateEstudiante(Estudiante e){
        try{
            SQLiteDatabase db=this.getWritableDatabase();
            db.execSQL("update Estudiante set nombre='"+e.getNombre()+"', apellido1='"+e.getApellido1()+"', apellido2='"+e.getApellido2()+"', edad='"+e.getEdad()+"' where id="+e.getId()+";");
            return true;
        }catch (SQLiteException ex){
            Log.e("Base de Datos", "Excepcion en updateEstudiante", ex);
            return false;
        }
    }*/

    /*

    public boolean deleteEstudiante(Estudiante e){
        try{
            SQLiteDatabase db=this.getWritableDatabase();
            db.execSQL("delete from Estudiante where id="+e.getId()+";");
            return true;
        }catch (SQLiteException ex){
            Log.e("Base de Datos", "Excepcion en deleteEstudiante", ex);
            return false;
        }
    }*/



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

    /*
    public ArrayList<Estudiante> getEstudiantesLike(String busqueda){
        try{
            SQLiteDatabase db=this.getReadableDatabase();
            String query= "select * from Estudiante where id like '%%%"+busqueda+"%%%' or nombre like '%%%"+busqueda+"%%%' or apellido1 like '%%%"+busqueda+"%%%' or apellido2 like '%%%"+busqueda+"%%%' or edad like '%%%"+busqueda+"%%%';";

            Cursor cursor = db.rawQuery(query,null);
            ArrayList<Estudiante> lista=new ArrayList<>();
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Estudiante aux=new Estudiante();
                    aux.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    aux.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("nombre")));
                    aux.setApellido1(cursor.getString(cursor.getColumnIndexOrThrow("apellido1")));
                    aux.setApellido2(cursor.getString(cursor.getColumnIndexOrThrow("apellido2")));
                    aux.setEdad(cursor.getInt(cursor.getColumnIndexOrThrow("edad")));
                    lista.add(aux);
                    cursor.moveToNext();
                }
            }

            return lista;

        }catch (SQLiteException ex){
            Log.e("Base de Datos", "Excepcion en getListaEstudiantes", ex);
            return null;
        }
    }*/




}
