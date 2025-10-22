package com.example.apartamentos.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes")
public class MensajeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private Integer idMensaje;

    // remitente -> FK a usuarios.id_usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_remitente", nullable = false)
    private ClienteModel remitente;

    // destinatario -> FK a usuarios.id_usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_destinatario", nullable = false)
    private ClienteModel destinatario;

    // asociacion opcional a reservacion
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reservacion", nullable = true)
    private ReservacionModel reservacion;

    @Column(length = 200)
    private String asunto;

    @Lob
    private String contenido;

    private Boolean leido;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    public Integer getIdMensaje() { return idMensaje; }
    public void setIdMensaje(Integer idMensaje) { this.idMensaje = idMensaje; }

    public ClienteModel getRemitente() { return remitente; }
    public void setRemitente(ClienteModel remitente) { this.remitente = remitente; }

    public ClienteModel getDestinatario() { return destinatario; }
    public void setDestinatario(ClienteModel destinatario) { this.destinatario = destinatario; }

    public ReservacionModel getReservacion() { return reservacion; }
    public void setReservacion(ReservacionModel reservacion) { this.reservacion = reservacion; }

    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public Boolean getLeido() { return leido; }
    public void setLeido(Boolean leido) { this.leido = leido; }

    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    @PrePersist
    public void prePersist() {
        if (fechaEnvio == null) fechaEnvio = LocalDateTime.now();
        if (leido == null) leido = false;
    }
}
