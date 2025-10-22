package com.example.apartamentos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.apartamentos.models.ReservacionModel;
import com.example.apartamentos.repositories.IReservacionRepository;

@Service
public class ReservacionService {

    @Autowired
    private IReservacionRepository reservacionRepository;

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
}
