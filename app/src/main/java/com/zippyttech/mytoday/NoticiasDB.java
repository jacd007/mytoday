package com.zippyttech.mytoday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zippyttech.mytoday.models.Noticia;
import com.zippyttech.mytoday.models.NoticiasSQLiteHelper;

import java.util.ArrayList;

/**
 * Created by zippyttech on 03/04/18.
 */

public class NoticiasDB {

    private Context context;
    private SQLiteDatabase db;
    private NoticiasSQLiteHelper helper;
    private NoticiasSQLiteHelper nsdbh;

    public NoticiasDB(Context context){
        this.context=context;
        helper =
                new NoticiasSQLiteHelper(context, "DBNoticias", null, 1);
        nsdbh =helper;
    }

    public void fillDB(){
        /**Base de datos
         * */

        SQLiteDatabase db = nsdbh.getWritableDatabase();

        //Si hemos abierto correctamente la base de datos
        if(db != null)
        {
            //Insertamos 5 usuarios de ejemplo
            for(int i=1; i<=5; i++)
            {
                //Generamos los datos
                int codigo = i;
                String tituloNoticia = "Noticia" + i;

                //Insertamos los datos en la tabla Usuarios
                db.execSQL("INSERT INTO Usuarios (codigo, nombre) " +
                        "VALUES (" + codigo + ", '" + tituloNoticia +"')");
            }

            //Cerramos la base de datos
            db.close();
        }

        //Creamos el registro a insertar como objeto ContentValues
        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put("codigo", "6");
        nuevoRegistro.put("tituloNoticia","usuariopru");

        //Insertamos el registro en la base de datos
    //    db.insert("Noticia", null, nuevoRegistro);
        /**
         * Cierre Base de datos*/
    }

    public ArrayList<Noticia> getList(){

        try {
            db = helper.getWritableDatabase();
            Cursor c = db.rawQuery(" SELECT * FROM Usuarios ", null);

            ArrayList<Noticia> listNoticia = new ArrayList<>();

            if (c.moveToFirst()) {

                do {

                    String codigo = c.getString(0);
                    String nombre = c.getString(1);
                    Noticia noticia = new Noticia(0, codigo, nombre, "");
                    listNoticia.add(noticia);

                } while (c.moveToNext());
                return listNoticia;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
