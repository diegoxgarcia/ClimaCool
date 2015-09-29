package com.example.diegox101.DBAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.diegox101.sqlitedb.DBHelper;

import java.sql.SQLException;

/**
 * Created by diegoxgarcia on 24/9/15.
 */
public class ClimaDBAdapter {

    // Definimos constante con el nombre de la tabla
    //
    public static final String C_TABLA = "CLIMAS";

    //
    // Definimos constantes con el nombre de las columnas de la tabla
    //
    public static final String C_COLUMNA_ID	= "id";
    public static final String C_COLUMNA_NOMBRE = "nombre";
    public static final String C_COLUMNA_TEMPERATURA = "temp";
    public static final String C_COLUMNA_TEMP_MINIMA = "tempmin";
    public static final String C_COLUMNA_TEMP_MAXIMA = "tempmax";
    public static final String C_COLUMNA_HUMEDAD = "humidity";
    public static final String C_COLUMNA_PRESION = "press";
    public static final String C_COLUMNA_VIENTO = "wind";
    public static final String C_COLUMNA_IMAGECODE = "image_code";

    private Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    private String[] columnas = new String[]{
            C_COLUMNA_ID,
            C_COLUMNA_NOMBRE,
            C_COLUMNA_TEMPERATURA,
            C_COLUMNA_TEMP_MINIMA,
            C_COLUMNA_TEMP_MAXIMA,
            C_COLUMNA_HUMEDAD,
            C_COLUMNA_PRESION,
            C_COLUMNA_VIENTO,
            C_COLUMNA_IMAGECODE} ;

    public ClimaDBAdapter(Context context) {
        this.context = context;
    }

    public ClimaDBAdapter abrir() throws SQLException{
        dbHelper = new DBHelper(this.context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void cerrar(){
        dbHelper.close();
    }

    public Cursor getRegistro(long  id) throws SQLException{
        Cursor c = db.query(true, C_TABLA, columnas, C_COLUMNA_ID + "=" + id,null,null,null,null,null);
        if(c != null){
            c.moveToFirst();
        }
        return c;
    }

    public long insert(ContentValues reg)
    {
        if(db == null)
            try {
                abrir();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return db.insert(C_TABLA,null,reg);
    }

    public Cursor getAll() throws  SQLException{
        Cursor c = db.query(true,C_TABLA,columnas,null,null,null,null,C_COLUMNA_NOMBRE,null);
        if(c!=null){
            c.moveToFirst();
        }
        return c;
    }

    public long delete(long id)
    {
        if(db == null)
            try {
                abrir();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return db.delete(C_TABLA, "id=" + id,null);
    }

    public long update(ContentValues reg)
    {
        long result = 0;

        if(db == null)
            try {
                abrir();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        if(reg.containsKey(C_COLUMNA_ID))
        {
            //
            // Obtenemos el id y lo borramos de los valores
            //
            long id = reg.getAsLong(C_COLUMNA_ID);
            reg.remove(C_COLUMNA_ID);
            //
            // Actualizamos el registro con el identificador que hemos extraido
            //
            result = db.update(C_TABLA, reg, "id=" + id, null);
        }
        return result;
    }
}
