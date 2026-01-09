package com.example.appdietas.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "comidas")
public class Comida {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private String nombre;

    @NonNull
    private String tipo;

    private String descripcion;
    private Integer calorias;
    private String imagenUri;

    public Comida(long id,
                  @NonNull String nombre,
                  @NonNull String tipo,
                  String descripcion,
                  Integer calorias,
                  String imagenUri) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.calorias = calorias;
        this.imagenUri = imagenUri;
    }

    @Ignore
    public Comida(@NonNull String nombre,
                  @NonNull String tipo,
                  String descripcion,
                  Integer calorias,
                  String imagenUri) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.calorias = calorias;
        this.imagenUri = imagenUri;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getNombre() {
        return nombre;
    }

    public void setNombre(@NonNull String nombre) {
        this.nombre = nombre;
    }

    @NonNull
    public String getTipo() {
        return tipo;
    }

    public void setTipo(@NonNull String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCalorias() {
        return calorias;
    }

    public void setCalorias(Integer calorias) {
        this.calorias = calorias;
    }

    public String getImagenUri() {
        return imagenUri;
    }

    public void setImagenUri(String imagenUri) {
        this.imagenUri = imagenUri;
    }
}
