package com.example.apartamentos.controllers;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.apartamentos.models.ReservacionModel;
import com.example.apartamentos.models.ReservacionModel.EstadoReservacion;
import com.example.apartamentos.services.ReservacionService;

@RestController
@RequestMapping("/reservaciones")
@CrossOrigin(origins = "*") // permite peticiones desde cualquier frontend
public class ReservacionController {

    @Autowired
    private ReservacionService reservacionService;

    // 📥 Obtener todas las reservaciones
    @GetMapping
    public List<ReservacionModel> getAllReservaciones() {
        return reservacionService.getAllReservaciones();
    }

    // 🔍 Obtener una reservación por ID
    @GetMapping("/{id}")
    public Optional<ReservacionModel> getReservacionById(@PathVariable("id") Long id) {
        return reservacionService.getReservacionById(id);
    }

    // 💾 Guardar o actualizar una reservación
    @PostMapping
    public ReservacionModel saveReservacion(@Valid @RequestBody ReservacionModel reservacion) {
        return reservacionService.saveReservacion(reservacion);
    }

    // 🗑 Eliminar una reservación
    @DeleteMapping("/{id}")
    public String deleteReservacion(@PathVariable("id") Long id) {
        reservacionService.deleteReservacion(id);
        return "Reservación con ID " + id + " eliminada correctamente.";
    }

    // ============ ENDPOINTS DE VALIDACIONES.MD ============

    // 1. Comprobar disponibilidad de una propiedad
    @GetMapping("/disponibilidad/{idPropiedad}")
    public ResponseEntity<Map<String, Object>> comprobarDisponibilidad(
            @PathVariable Long idPropiedad,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaEntrada,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaSalida) {

        Boolean disponible = reservacionService.comprobarDisponibilidad(idPropiedad, fechaEntrada, fechaSalida);
        return ResponseEntity.ok(Map.of(
            "disponible", disponible,
            "idPropiedad", idPropiedad,
            "fechaEntrada", fechaEntrada,
            "fechaSalida", fechaSalida
        ));
    }

    // 2. Reservar apartamento (método especializado)
    @PostMapping("/reservar")
    public ResponseEntity<ReservacionModel> reservarApartamento(@Valid @RequestBody ReservacionModel reservacion) {
        ReservacionModel nuevaReservacion = reservacionService.reservarApartamento(reservacion);
        return ResponseEntity.ok(nuevaReservacion);
    }

    // 3. Cancelar reservación
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<String> cancelarReservacion(@PathVariable Long id) {
        reservacionService.cancelarReservacion(id);
        return ResponseEntity.ok("Reservación cancelada correctamente");
    }

    // 4. Actualizar reservación
    @PutMapping("/{id}/actualizar")
    public ResponseEntity<ReservacionModel> actualizarReservacion(
            @PathVariable Long id,
            @Valid @RequestBody ReservacionModel reservacion) {
        ReservacionModel reservacionActualizada = reservacionService.actualizarReservacion(id, reservacion);
        return ResponseEntity.ok(reservacionActualizada);
    }

    // 5. Confirmar reservación
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<ReservacionModel> confirmarReservacion(@PathVariable Long id) {
        ReservacionModel reservacionConfirmada = reservacionService.confirmarReservacion(id);
        return ResponseEntity.ok(reservacionConfirmada);
    }

    // 6. Obtener reservaciones por cliente
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<ReservacionModel>> obtenerReservacionesPorCliente(@PathVariable Long idCliente) {
        List<ReservacionModel> reservaciones = reservacionService.obtenerReservacionesPorCliente(idCliente);
        return ResponseEntity.ok(reservaciones);
    }

    // 7. Obtener reservaciones por propiedad
    @GetMapping("/propiedad/{idPropiedad}")
    public ResponseEntity<List<ReservacionModel>> obtenerReservacionesPorPropiedad(@PathVariable Long idPropiedad) {
        List<ReservacionModel> reservaciones = reservacionService.obtenerReservacionesPorPropiedad(idPropiedad);
        return ResponseEntity.ok(reservaciones);
    }

    // 8. Obtener reservaciones por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ReservacionModel>> obtenerReservacionesPorEstado(@PathVariable EstadoReservacion estado) {
        List<ReservacionModel> reservaciones = reservacionService.obtenerReservacionesPorEstado(estado);
        return ResponseEntity.ok(reservaciones);
    }

    // 9. Obtener reservaciones entre fechas
    @GetMapping("/fechas")
    public ResponseEntity<List<ReservacionModel>> obtenerReservacionesEntreFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        List<ReservacionModel> reservaciones = reservacionService.obtenerReservacionesEntreFechas(inicio, fin);
        return ResponseEntity.ok(reservaciones);
    }

    // 10. Calcular precio total
    @GetMapping("/calcular-precio/{idPropiedad}")
    public ResponseEntity<Map<String, Object>> calcularPrecioTotal(
            @PathVariable Long idPropiedad,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaEntrada,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaSalida) {

        Double precioTotal = reservacionService.calcularPrecioTotal(idPropiedad, fechaEntrada, fechaSalida);
        return ResponseEntity.ok(Map.of(
            "precioTotal", precioTotal,
            "idPropiedad", idPropiedad,
            "fechaEntrada", fechaEntrada,
            "fechaSalida", fechaSalida
        ));
    }

    // 11. Verificar disponibilidad entre fechas (alias de método 1)
    @GetMapping("/verificar-disponibilidad/{idPropiedad}")
    public ResponseEntity<Boolean> verificarDisponibilidad(
            @PathVariable Long idPropiedad,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaEntrada,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaSalida) {

        Boolean disponible = reservacionService.verificarDisponibilidadEntreFechas(idPropiedad, fechaEntrada, fechaSalida);
        return ResponseEntity.ok(disponible);
    }

    // 12. Obtener días ocupados de una propiedad en un mes
    @GetMapping("/dias-ocupados/{idPropiedad}")
    public ResponseEntity<List<LocalDate>> obtenerDiasOcupados(
            @PathVariable Long idPropiedad,
            @RequestParam int mes,
            @RequestParam int anio) {

        List<LocalDate> diasOcupados = reservacionService.obtenerDiasOcupados(idPropiedad, mes, anio);
        return ResponseEntity.ok(diasOcupados);
    }

    // 13. Generar código de reserva
    @GetMapping("/generar-codigo")
    public ResponseEntity<Map<String, String>> generarCodigoReserva() {
        String codigo = reservacionService.generarCodigoReserva();
        return ResponseEntity.ok(Map.of("codigoReserva", codigo));
    }

    // 14. Enviar confirmación de reserva
    @PostMapping("/{id}/enviar-confirmacion")
    public ResponseEntity<String> enviarConfirmacion(@PathVariable Long id) {
        reservacionService.enviarConfirmacionReserva(id);
        return ResponseEntity.ok("Confirmación enviada correctamente");
    }

    // 15. Enviar recordatorio de reserva
    @PostMapping("/{id}/enviar-recordatorio")
    public ResponseEntity<String> enviarRecordatorio(@PathVariable Long id) {
        reservacionService.enviarRecordatorioReserva(id);
        return ResponseEntity.ok("Recordatorio enviado correctamente");
    }

    // 16. Procesar check-in
    @PutMapping("/{id}/check-in")
    public ResponseEntity<ReservacionModel> procesarCheckIn(@PathVariable Long id) {
        ReservacionModel reservacion = reservacionService.procesarCheckIn(id);
        return ResponseEntity.ok(reservacion);
    }

    // 17. Procesar check-out
    @PutMapping("/{id}/check-out")
    public ResponseEntity<ReservacionModel> procesarCheckOut(@PathVariable Long id) {
        ReservacionModel reservacion = reservacionService.procesarCheckOut(id);
        return ResponseEntity.ok(reservacion);
    }
}

