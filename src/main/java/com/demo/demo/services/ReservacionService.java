package com.example.apartamentos.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.apartamentos.models.PropiedadModel;
import com.example.apartamentos.models.ReservacionModel;
import com.example.apartamentos.models.ReservacionModel.EstadoReservacion;
import com.example.apartamentos.repositories.IReservacionRepository;
import com.example.apartamentos.repositories.IPropiedadRepository;

@Service
public class ReservacionService {

    @Autowired
    private IReservacionRepository reservacionRepository;

    @Autowired
    private IPropiedadRepository propiedadRepository;

    // ============ MÉTODOS CRUD BÁSICOS ============

    // Obtener todas las reservaciones
    public List<ReservacionModel> getAllReservaciones() {
        return reservacionRepository.findAll();
    }

    // Obtener una reservación por su ID
    public Optional<ReservacionModel> getReservacionById(Long id) {
        return reservacionRepository.findById(id);
    }

    // Guardar o actualizar una reservación
    public ReservacionModel saveReservacion(ReservacionModel reservacion) {
        return reservacionRepository.save(reservacion);
    }

    // Eliminar una reservación por ID
    public void deleteReservacion(Long id) {
        reservacionRepository.deleteById(id);
    }

    // ============ MÉTODOS DE VALIDACIONES.MD ============

    // 1. Comprobar disponibilidad de un apartamento
    public Boolean comprobarDisponibilidad(Long idPropiedad, LocalDate fechaEntrada, LocalDate fechaSalida) {
        if (fechaEntrada == null || fechaSalida == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");
        }
        if (fechaSalida.isBefore(fechaEntrada) || fechaSalida.isEqual(fechaEntrada)) {
            throw new IllegalArgumentException("La fecha de salida debe ser posterior a la fecha de entrada");
        }

        List<ReservacionModel> reservasSuperpuestas = reservacionRepository.findOverlappingReservations(
            idPropiedad, fechaEntrada, fechaSalida
        );

        return reservasSuperpuestas.isEmpty();
    }

    // 2. Reservar un apartamento
    public ReservacionModel reservarApartamento(ReservacionModel reservacion) {
        // Validar disponibilidad
        if (!comprobarDisponibilidad(
                reservacion.getPropiedad().getId_propiedad(),
                reservacion.getFechaEntrada(),
                reservacion.getFechaSalida())) {
            throw new IllegalArgumentException("La propiedad no está disponible para las fechas seleccionadas");
        }

        // Validar que la fecha de salida sea posterior a la entrada
        if (reservacion.getFechaSalida().isBefore(reservacion.getFechaEntrada()) ||
            reservacion.getFechaSalida().isEqual(reservacion.getFechaEntrada())) {
            throw new IllegalArgumentException("La fecha de salida debe ser posterior a la fecha de entrada");
        }

        // Generar código de reserva si no existe
        if (reservacion.getCodigoReserva() == null || reservacion.getCodigoReserva().isEmpty()) {
            reservacion.setCodigoReserva(generarCodigoReserva());
        }

        // Establecer estado inicial como pendiente
        if (reservacion.getEstado() == null) {
            reservacion.setEstado(EstadoReservacion.pendiente);
        }

        // Establecer fecha de reservación
        if (reservacion.getFechaReservacion() == null) {
            reservacion.setFechaReservacion(LocalDateTime.now());
        }

        // Calcular precio total si no está establecido
        if (reservacion.getPrecioTotal() == 0) {
            double precioCalculado = calcularPrecioTotal(
                reservacion.getPropiedad().getId_propiedad(),
                reservacion.getFechaEntrada(),
                reservacion.getFechaSalida()
            );
            reservacion.setPrecioTotal(precioCalculado);
        }

        return reservacionRepository.save(reservacion);
    }

    // 3. Cancelar una reservación
    public void cancelarReservacion(Long idReservacion) {
        Optional<ReservacionModel> reservacionOpt = reservacionRepository.findById(idReservacion);
        if (reservacionOpt.isEmpty()) {
            throw new IllegalArgumentException("Reservación no encontrada con ID: " + idReservacion);
        }

        ReservacionModel reservacion = reservacionOpt.get();
        if (reservacion.getEstado() == EstadoReservacion.finalizada) {
            throw new IllegalArgumentException("No se puede cancelar una reservación finalizada");
        }
        if (reservacion.getEstado() == EstadoReservacion.cancelada) {
            throw new IllegalArgumentException("La reservación ya está cancelada");
        }

        reservacion.setEstado(EstadoReservacion.cancelada);
        reservacionRepository.save(reservacion);
    }

    // 4. Actualizar una reservación
    public ReservacionModel actualizarReservacion(Long idReservacion, ReservacionModel reservacionActualizada) {
        Optional<ReservacionModel> reservacionOpt = reservacionRepository.findById(idReservacion);
        if (reservacionOpt.isEmpty()) {
            throw new IllegalArgumentException("Reservación no encontrada con ID: " + idReservacion);
        }

        ReservacionModel reservacionExistente = reservacionOpt.get();

        // No permitir actualizar si está finalizada o cancelada
        if (reservacionExistente.getEstado() == EstadoReservacion.finalizada ||
            reservacionExistente.getEstado() == EstadoReservacion.cancelada) {
            throw new IllegalArgumentException("No se puede actualizar una reservación finalizada o cancelada");
        }

        // Verificar disponibilidad si cambian las fechas
        if (!reservacionActualizada.getFechaEntrada().equals(reservacionExistente.getFechaEntrada()) ||
            !reservacionActualizada.getFechaSalida().equals(reservacionExistente.getFechaSalida())) {

            // Verificar disponibilidad excluyendo la reservación actual
            List<ReservacionModel> reservasSuperpuestas = reservacionRepository.findOverlappingReservations(
                reservacionExistente.getPropiedad().getId_propiedad(),
                reservacionActualizada.getFechaEntrada(),
                reservacionActualizada.getFechaSalida()
            );

            // Filtrar la reservación actual de las superpuestas
            reservasSuperpuestas.removeIf(r -> r.getId().equals(idReservacion));

            if (!reservasSuperpuestas.isEmpty()) {
                throw new IllegalArgumentException("La propiedad no está disponible para las nuevas fechas");
            }
        }

        // Actualizar campos
        reservacionExistente.setFechaEntrada(reservacionActualizada.getFechaEntrada());
        reservacionExistente.setFechaSalida(reservacionActualizada.getFechaSalida());
        reservacionExistente.setNumeroHuespedes(reservacionActualizada.getNumeroHuespedes());
        reservacionExistente.setNotas(reservacionActualizada.getNotas());

        // Recalcular precio total si cambiaron las fechas
        if (!reservacionActualizada.getFechaEntrada().equals(reservacionExistente.getFechaEntrada()) ||
            !reservacionActualizada.getFechaSalida().equals(reservacionExistente.getFechaSalida())) {
            double precioCalculado = calcularPrecioTotal(
                reservacionExistente.getPropiedad().getId_propiedad(),
                reservacionActualizada.getFechaEntrada(),
                reservacionActualizada.getFechaSalida()
            );
            reservacionExistente.setPrecioTotal(precioCalculado);
        }

        return reservacionRepository.save(reservacionExistente);
    }

    // 5. Confirmar una reservación
    public ReservacionModel confirmarReservacion(Long idReservacion) {
        Optional<ReservacionModel> reservacionOpt = reservacionRepository.findById(idReservacion);
        if (reservacionOpt.isEmpty()) {
            throw new IllegalArgumentException("Reservación no encontrada con ID: " + idReservacion);
        }

        ReservacionModel reservacion = reservacionOpt.get();
        if (reservacion.getEstado() == EstadoReservacion.confirmada) {
            throw new IllegalArgumentException("La reservación ya está confirmada");
        }
        if (reservacion.getEstado() == EstadoReservacion.cancelada) {
            throw new IllegalArgumentException("No se puede confirmar una reservación cancelada");
        }
        if (reservacion.getEstado() == EstadoReservacion.finalizada) {
            throw new IllegalArgumentException("No se puede confirmar una reservación finalizada");
        }

        reservacion.setEstado(EstadoReservacion.confirmada);
        return reservacionRepository.save(reservacion);
    }

    // 6. Obtener reservaciones por cliente
    public List<ReservacionModel> obtenerReservacionesPorCliente(Long idCliente) {
        return reservacionRepository.findByClienteId(idCliente);
    }

    // 7. Obtener reservaciones por propiedad
    public List<ReservacionModel> obtenerReservacionesPorPropiedad(Long idPropiedad) {
        return reservacionRepository.findByPropiedadIdPropiedad(idPropiedad);
    }

    // 8. Obtener reservaciones por estado
    public List<ReservacionModel> obtenerReservacionesPorEstado(EstadoReservacion estado) {
        return reservacionRepository.findByEstado(estado);
    }

    // 9. Obtener reservaciones entre fechas
    public List<ReservacionModel> obtenerReservacionesEntreFechas(LocalDate inicio, LocalDate fin) {
        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(23, 59, 59);
        return reservacionRepository.findByFechaReservacionBetween(inicioDateTime, finDateTime);
    }

    // 10. Calcular precio total
    public Double calcularPrecioTotal(Long idPropiedad, LocalDate fechaEntrada, LocalDate fechaSalida) {
        Optional<PropiedadModel> propiedadOpt = propiedadRepository.findById(idPropiedad);
        if (propiedadOpt.isEmpty()) {
            throw new IllegalArgumentException("Propiedad no encontrada con ID: " + idPropiedad);
        }

        PropiedadModel propiedad = propiedadOpt.get();
        long dias = ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);

        if (dias <= 0) {
            throw new IllegalArgumentException("El número de días debe ser mayor que 0");
        }

        return dias * propiedad.getPrecio_noche();
    }

    // 11. Verificar disponibilidad entre fechas
    public Boolean verificarDisponibilidadEntreFechas(Long idPropiedad, LocalDate fechaEntrada, LocalDate fechaSalida) {
        return comprobarDisponibilidad(idPropiedad, fechaEntrada, fechaSalida);
    }

    // 12. Obtener días ocupados de un mes
    public List<LocalDate> obtenerDiasOcupados(Long idPropiedad, int mes, int anio) {
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("El mes debe estar entre 1 y 12");
        }

        List<ReservacionModel> reservaciones = reservacionRepository.findReservationsByPropertyAndMonth(
            idPropiedad, mes, anio
        );

        List<LocalDate> diasOcupados = new ArrayList<>();

        for (ReservacionModel reservacion : reservaciones) {
            LocalDate fecha = reservacion.getFechaEntrada();
            while (!fecha.isAfter(reservacion.getFechaSalida())) {
                // Solo agregar si está en el mes solicitado
                if (fecha.getMonthValue() == mes && fecha.getYear() == anio) {
                    if (!diasOcupados.contains(fecha)) {
                        diasOcupados.add(fecha);
                    }
                }
                fecha = fecha.plusDays(1);
            }
        }

        diasOcupados.sort(LocalDate::compareTo);
        return diasOcupados;
    }

    // 13. Generar código de reserva
    public String generarCodigoReserva() {
        String codigo = "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        // Verificar que no exista ya (muy improbable pero mejor verificar)
        while (reservacionRepository.findByCodigoReserva(codigo) != null) {
            codigo = "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
        return codigo;
    }

    // 14. Enviar confirmación de reserva (stub - implementar con servicio de email)
    public void enviarConfirmacionReserva(Long idReservacion) {
        Optional<ReservacionModel> reservacionOpt = reservacionRepository.findById(idReservacion);
        if (reservacionOpt.isEmpty()) {
            throw new IllegalArgumentException("Reservación no encontrada con ID: " + idReservacion);
        }

        // TODO: Implementar lógica de envío de email
        System.out.println("Enviando confirmación de reserva para ID: " + idReservacion);
        // Aquí se integraría con un servicio de email como JavaMailSender o un servicio externo
    }

    // 15. Enviar recordatorio de reserva (stub - implementar con servicio de email)
    public void enviarRecordatorioReserva(Long idReservacion) {
        Optional<ReservacionModel> reservacionOpt = reservacionRepository.findById(idReservacion);
        if (reservacionOpt.isEmpty()) {
            throw new IllegalArgumentException("Reservación no encontrada con ID: " + idReservacion);
        }

        ReservacionModel reservacion = reservacionOpt.get();
        // Verificar si la reserva está próxima (por ejemplo, 3 días antes)
        long diasHastaEntrada = ChronoUnit.DAYS.between(LocalDate.now(), reservacion.getFechaEntrada());

        // TODO: Implementar lógica de envío de email
        System.out.println("Enviando recordatorio de reserva para ID: " + idReservacion +
                         " (faltan " + diasHastaEntrada + " días)");
    }

    // 16. Procesar check-in
    public ReservacionModel procesarCheckIn(Long idReservacion) {
        Optional<ReservacionModel> reservacionOpt = reservacionRepository.findById(idReservacion);
        if (reservacionOpt.isEmpty()) {
            throw new IllegalArgumentException("Reservación no encontrada con ID: " + idReservacion);
        }

        ReservacionModel reservacion = reservacionOpt.get();

        if (reservacion.getEstado() != EstadoReservacion.confirmada) {
            throw new IllegalArgumentException("Solo se puede hacer check-in de reservaciones confirmadas");
        }

        // Verificar que sea la fecha correcta (hoy debe ser igual o posterior a fechaEntrada)
        if (LocalDate.now().isBefore(reservacion.getFechaEntrada())) {
            throw new IllegalArgumentException("No se puede hacer check-in antes de la fecha de entrada");
        }

        reservacion.setFechaCheckin(LocalDateTime.now());
        // Mantener el estado como confirmada o cambiarlo a un estado "en_curso" si existe
        return reservacionRepository.save(reservacion);
    }

    // 17. Procesar check-out
    public ReservacionModel procesarCheckOut(Long idReservacion) {
        Optional<ReservacionModel> reservacionOpt = reservacionRepository.findById(idReservacion);
        if (reservacionOpt.isEmpty()) {
            throw new IllegalArgumentException("Reservación no encontrada con ID: " + idReservacion);
        }

        ReservacionModel reservacion = reservacionOpt.get();

        if (reservacion.getFechaCheckin() == null) {
            throw new IllegalArgumentException("No se puede hacer check-out sin haber hecho check-in primero");
        }

        if (reservacion.getEstado() == EstadoReservacion.finalizada) {
            throw new IllegalArgumentException("La reservación ya está finalizada");
        }

        reservacion.setFechaCheckout(LocalDateTime.now());
        reservacion.setEstado(EstadoReservacion.finalizada);
        return reservacionRepository.save(reservacion);
    }
}
