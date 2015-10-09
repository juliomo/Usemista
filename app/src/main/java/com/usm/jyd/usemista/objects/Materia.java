package com.usm.jyd.usemista.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.usm.jyd.usemista.logs.L;

import java.util.Date;

/**
 * Created by der_w on 10/8/2015.
 */
public class Materia implements Parcelable {
    public static final Parcelable.Creator<Materia> CREATOR
            = new Parcelable.Creator<Materia>() {
        public Materia createFromParcel(Parcel in) {
            L.m("create from parcel :Materia");
            return new Materia(in);
        }

        public Materia[] newArray(int size) {
            return new Materia[size];
        }
    };
    private long id;
    private String titulo;
    private int semestre;
    private String objetivo;
    private String contenido;
    private String modulo;



    public Materia() {

    }

    public Materia(Parcel input) {
        id = input.readLong();
        titulo = input.readString();
        objetivo = input.readString();
        contenido = input.readString();
        modulo = input.readString();
    }

    public Materia(long id,
                 String title,
                   int semestre,
                 String objetivo,
                 String contenido,
                 String modulo) {
        this.id = id;
        this.titulo = title;
        this.semestre = semestre;
        this.objetivo = objetivo;
        this.contenido = contenido;
        this.modulo = modulo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String title) {
        this.titulo = title;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    @Override
    public String toString() {
        return "\nID: " + id +
                "\nTitulo " + titulo +
                "\nSemestre " + semestre +
                "\nObjetivo " + objetivo +
                "\nContenido " + contenido +
                "\nModulo " + modulo +
                "\n";
    }

    @Override
    public int describeContents() {
//        L.m("describe Contents Movie");
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        L.m("writeToParcel Movie");
        dest.writeLong(id);
        dest.writeString(titulo);
        dest.writeInt(semestre);
        dest.writeString(objetivo);
        dest.writeString(contenido);
        dest.writeString(modulo);

    }
}

