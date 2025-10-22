package com.example.apartamentos.models;

import jakarta.persistence.*;

@Entity
@Table(name = "propiedad_imagenes")
public class PropiedadimagenModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imagen")
    private Long idImagen; // ✅ Llave primaria

    @Column(name = "url_imagen", nullable = false, length = 255)
    private String urlImagen;

    @ManyToOne
    @JoinColumn(name = "id_propiedad", nullable = false) // ✅ Llave foránea
    private PropiedadModel propiedad;

    // ✅ Constructor vacío
    public PropiedadimagenModel() {}

    // ✅ Constructor con parámetros
    public PropiedadimagenModel(String urlImagen, PropiedadModel propiedad) {
        this.urlImagen = urlImagen;
        this.propiedad = propiedad;
    }

    // ✅ Getters y Setters
    public Long getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(Long idImagen) {
        this.idImagen = idImagen;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public PropiedadModel getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(PropiedadModel propiedad) {
        this.propiedad = propiedad;
    }
}
