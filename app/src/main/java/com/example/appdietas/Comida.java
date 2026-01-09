package com.example.appdietas;

public class Comida {
    private final int diaId;
    private final String tipo;
    private final String nombre;
    private final String descripcion;
    private final int calorias;
    private final int carbohidratos;
    private final int proteinas;
    private final int lipidos;
    private final int imagenResId;

    public Comida(
            int diaId,
            String tipo,
            String nombre,
            String descripcion,
            int calorias,
            int carbohidratos,
            int proteinas,
            int lipidos,
            int imagenResId
    ) {
        this.diaId = diaId;
        this.tipo = tipo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.calorias = calorias;
        this.carbohidratos = carbohidratos;
        this.proteinas = proteinas;
        this.lipidos = lipidos;
        this.imagenResId = imagenResId;
    }

    public int getDiaId() {
        return diaId;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCalorias() {
        return calorias;
    }

    public int getCarbohidratos() {
        return carbohidratos;
    }

    public int getProteinas() {
        return proteinas;
    }

    public int getLipidos() {
        return lipidos;
    }

    public int getImagenResId() {
        return imagenResId;
    }
}
