package com.zippyttech.mytoday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.zippyttech.mytoday.models.Noticia;
import com.zippyttech.mytoday.models.NoticiasSQLiteHelper;

import java.util.ArrayList;
import java.util.List;

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
             //   int codigo = i;
               // String titulo= "titulo" + i;
                String contenido= "contenido" + i;
                String fecha = "fecha" + i;

                //Insertamos los datos en la tabla Usuarios
       //         db.execSQL("INSERT INTO Usuarios (codigo, nombre) " +
       //                 "VALUES (" + codigo + ", '" + titulo +"')");
                  //  insertContenido(contenido);
            }




            //Cerramos la base de datos
            db.close();
        }

  /*      //Creamos el registro a insertar como objeto ContentValues
        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put("codigo", "6");
        nuevoRegistro.put("tituloNoticia","usuariopru");
*/
        //Insertamos el registro en la base de datos
    //    db.insert("Noticia", null, nuevoRegistro);
        /**
         * Cierre Base de datos*/
    }

    /**
     * Update NoticiasDB */
    public void UpdateNoticiasDB( ){
            try {
                    //Establecemos los campos-valores a actualizar
                    ContentValues valores = new ContentValues();
                    valores.put("titulo", "EL_Update");

                    //Actualizamos el registro en la base de datos
                    db.update("Noticia", valores, "codigo='28507'", null);
            }
            catch (Exception e){
                e.printStackTrace();
            }

        return;
    }
    /**
     * end update*/
    /**
     * Delete NoticiasDB*/
    public void DeleteNoticiasDB (){
        try {

           // db.execSQL("DROP TABLE IF EXISTS Noticia");
            db.execSQL("DELETE FROM Noticia");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return;
    }
    /**
     * end Delete*/

    /**
     * Insert NoticiasDB*/
    public void insertContenido (Noticia noticia){
        db.execSQL("INSERT INTO Noticia (codigo, titulo, contenido, fecha, imagen) "
                + "VALUES (" + noticia.getCodigo() + ", '" + noticia.getTitulo() +"', '" + noticia.getContenido() +"', '" + noticia.getFecha() +"', '" + noticia.getImagen() +"')");
        //   return;
    }

    public void insertarNoticias( List<Noticia> lista){
        try {
            if (lista != null) {

                for (Noticia noti:
                     lista) {
                    insertContenido(noti);
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * end Insert*/

    /**
     * Get-Select NoticiasDB*/

    /**
     * end */
    public ArrayList<Noticia> getList(){

        try {
            db = helper.getWritableDatabase();
            Cursor c = db.rawQuery(" SELECT * FROM Noticia ", null);

            ArrayList<Noticia> listNoticia = new ArrayList<>();

            if (c.moveToFirst()) {

                do {

                    String codigo = c.getString(0);
                    String titulo = c.getString(1);
                    String contenido = c.getString(2);
                    String fecha = c.getString(3);
                    String imagen = c.getString(4);
                    Noticia noticia = new Noticia(0, codigo, titulo, contenido, fecha, imagen);
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
