package com.example.apartamentos.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "disponibilidad")
public class DisponibilidadModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_disponibilidad")
    private Long idDisponibilidad; // ✅ Llave primaria

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_propiedad", nullable = false) // ✅ Llave foránea
    private PropiedadModel propiedad;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "disponible", nullable = false)
    private boolean disponible;

    @Column(name = "precio_especial")
    private Double precioEspecial;

    public DisponibilidadModel() {}

    public DisponibilidadModel(PropiedadModel propiedad, LocalDate fecha, boolean disponible, Double precioEspecial) {
        this.propiedad = propiedad;
        this.fecha = fecha;
        this.disponible = disponible;
        this.precioEspecial = precioEspecial;
    }

    // Getters y Setters
    public Long getIdDisponibilidad() {
        return idDisponibilidad;
    }

    public void setIdDisponibilidad(Long idDisponibilidad) {
        this.idDisponibilidad = idDisponibilidad;
    }

    public PropiedadModel getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(PropiedadModel propiedad) {
        this.propiedad = propiedad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public Double getPrecioEspecial() {
        return precioEspecial;
    }

    public void setPrecioEspecial(Double precioEspecial) {
        this.precioEspecial = precioEspecial;
    }
}
