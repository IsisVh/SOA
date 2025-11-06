package com.example.apartamentos.services;

import com.example.apartamentos.models.PagoModel;
import com.example.apartamentos.repositories.IPagoRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    private final IPagoRepository pagoRepository;

    public PagoService(IPagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public List<PagoModel> getAllPagos() {
        return pagoRepository.findAll();
    }

    public Optional<PagoModel> getPagoById(Integer id) {
        return pagoRepository.findById(id);
    }

    public PagoModel savePago(PagoModel pago) {
        return pagoRepository.save(pago);
    }

    public void deletePago(Integer id) {
        pagoRepository.deleteById(id);
    }
}
