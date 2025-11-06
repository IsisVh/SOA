package com.example.apartamentos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.apartamentos.models.ReservacionModel;
import com.example.apartamentos.models.ReservacionModel.EstadoReservacion;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IReservacionRepository extends JpaRepository<ReservacionModel, Long> {

    // Buscar reservaciones por cliente
    List<ReservacionModel> findByClienteId(Long idCliente);

    // Buscar reservaciones por propiedad
    List<ReservacionModel> findByPropiedadIdPropiedad(Long idPropiedad);

    // Buscar reservaciones por estado
    List<ReservacionModel> findByEstado(EstadoReservacion estado);

    // Buscar reservaciones entre fechas (por fecha de reservación)
    List<ReservacionModel> findByFechaReservacionBetween(java.time.LocalDateTime inicio, java.time.LocalDateTime fin);

    // Buscar reservaciones por código
    ReservacionModel findByCodigoReserva(String codigoReserva);

    // Verificar solapamiento de fechas para una propiedad (crucial para disponibilidad)
    @Query("SELECT r FROM ReservacionModel r WHERE r.propiedad.id_propiedad = :idPropiedad " +
           "AND r.estado IN ('confirmada', 'pendiente') " +
           "AND ((r.fechaEntrada <= :fechaSalida AND r.fechaSalida >= :fechaEntrada))")
    List<ReservacionModel> findOverlappingReservations(
        @Param("idPropiedad") Long idPropiedad,
        @Param("fechaEntrada") LocalDate fechaEntrada,
        @Param("fechaSalida") LocalDate fechaSalida
    );

    // Obtener todas las fechas ocupadas para una propiedad en un mes específico
    @Query("SELECT r FROM ReservacionModel r WHERE r.propiedad.id_propiedad = :idPropiedad " +
           "AND r.estado IN ('confirmada', 'pendiente') " +
           "AND ((YEAR(r.fechaEntrada) = :anio AND MONTH(r.fechaEntrada) = :mes) " +
           "OR (YEAR(r.fechaSalida) = :anio AND MONTH(r.fechaSalida) = :mes))")
    List<ReservacionModel> findReservationsByPropertyAndMonth(
        @Param("idPropiedad") Long idPropiedad,
        @Param("mes") int mes,
        @Param("anio") int anio
    );

}
