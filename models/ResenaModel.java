package com.example.apartamentos.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "resenas")
public class ResenaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resena")
    public Integer idResena;

    @NotNull(message = "La reservación es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reservacion", nullable = false)
    private ReservacionModel reservacion;

    @Min(value = 1, message = "La calificación de limpieza debe ser entre 1 y 5")
    @Max(value = 5, message = "La calificación de limpieza debe ser entre 1 y 5")
    @Column(name = "calificacion_limpieza")
    private Integer calificacionLimpieza;

    @Min(value = 1, message = "La calificación de ubicación debe ser entre 1 y 5")
    @Max(value = 5, message = "La calificación de ubicación debe ser entre 1 y 5")
    @Column(name = "calificacion_ubicacion")
    private Integer calificacionUbicacion;

    @Min(value = 1, message = "La calificación de comunicación debe ser entre 1 y 5")
    @Max(value = 5, message = "La calificación de comunicación debe ser entre 1 y 5")
    @Column(name = "calificacion_comunicacion")
    private Integer calificacionComunicacion;

    @NotNull(message = "La calificación general es obligatoria")
    @Min(value = 1, message = "La calificación general debe ser entre 1 y 5")
    @Max(value = 5, message = "La calificación general debe ser entre 1 y 5")
    @Column(name = "calificacion_general")
    private Integer calificacionGeneral;

    @Lob
    private String comentario;

    @Column(name = "fecha_resena")
    private LocalDateTime fechaResena;

    @Lob
    @Column(name = "respuesta_propietario")
    private String respuestaPropietario;

    @Column(name = "fecha_respuesta")
    private LocalDateTime fechaRespuesta;

    public Integer getIdResena() { return idResena; }
    public void setIdResena(Integer idResena) { this.idResena = idResena; }

    public ReservacionModel getReservacion() { return reservacion; }
    public void setReservacion(ReservacionModel reservacion) { this.reservacion = reservacion; }

    public Integer getCalificacionLimpieza() { return calificacionLimpieza; }
    public void setCalificacionLimpieza(Integer calificacionLimpieza) { this.calificacionLimpieza = calificacionLimpieza; }

    public Integer getCalificacionUbicacion() { return calificacionUbicacion; }
    public void setCalificacionUbicacion(Integer calificacionUbicacion) { this.calificacionUbicacion = calificacionUbicacion; }

    public Integer getCalificacionComunicacion() { return calificacionComunicacion; }
    public void setCalificacionComunicacion(Integer calificacionComunicacion) { this.calificacionComunicacion = calificacionComunicacion; }

    public Integer getCalificacionGeneral() { return calificacionGeneral; }
    public void setCalificacionGeneral(Integer calificacionGeneral) { this.calificacionGeneral = calificacionGeneral; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public LocalDateTime getFechaResena() { return fechaResena; }
    public void setFechaResena(LocalDateTime fechaResena) { this.fechaResena = fechaResena; }

    public String getRespuestaPropietario() { return respuestaPropietario; }
    public void setRespuestaPropietario(String respuestaPropietario) { this.respuestaPropietario = respuestaPropietario; }

    public LocalDateTime getFechaRespuesta() { return fechaRespuesta; }
    public void setFechaRespuesta(LocalDateTime fechaRespuesta) { this.fechaRespuesta = fechaRespuesta; }

    @PrePersist
    public void prePersist() {
        if (fechaResena == null) fechaResena = LocalDateTime.now();
    }
}
