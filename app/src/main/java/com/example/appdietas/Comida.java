package com.example.appdietas;

public class Comida {
    private final int diaId;
    private final String tipo;
    private final String nombre;
    private final String descripcion;
    private final int gramaje;
    private final int calorias;
    private final int carbohidratos;
    private final int proteinas;
    private final int lipidos;
    private final int imagenResId;
    private String imagenUri;

    public Comida(
            int diaId,
            String tipo,
            String nombre,
            String descripcion,
            int gramaje,
            int calorias,
            int carbohidratos,
            int proteinas,
            int lipidos,
            int imagenResId,
            String imagenUri
    ) {
        this.diaId = diaId;
        this.tipo = tipo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.gramaje = gramaje;
        this.calorias = calorias;
        this.carbohidratos = carbohidratos;
        this.proteinas = proteinas;
        this.lipidos = lipidos;
        this.imagenResId = imagenResId;
        this.imagenUri = imagenUri;
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

    public int getGramaje() {
        return gramaje;
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

    public String getImagenUri() { return imagenUri; }

    public void setImagenUri(String imageUrl) {
    }
}
