package com.zippyttech.mytoday.models;

/**
 * Created by zippyttech on 28/03/18.
 */

public class Noticia {

    private int id;
    private String titulo;
   private String contenido;
   private  String fecha;

    public Noticia( ) {

    }

    public Noticia(int id, String titulo, String contenido,String fecha) {
        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
         this.fecha = fecha;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public  String getfecha(){return  fecha;}

    public void setFecha(String fecha) { this.fecha = fecha;}
}
