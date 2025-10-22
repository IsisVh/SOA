package com.example.apartamentos.controllers;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.apartamentos.models.ReservacionModel;
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
    public ReservacionModel saveReservacion(@RequestBody ReservacionModel reservacion) {
        return reservacionService.saveReservacion(reservacion);
    }

    // 🗑 Eliminar una reservación
    @DeleteMapping("/{id}")
    public String deleteReservacion(@PathVariable("id") Long id) {
        reservacionService.deleteReservacion(id);
        return "Reservación con ID " + id + " eliminada correctamente.";
    }
}

