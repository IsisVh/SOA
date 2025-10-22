package com.example.apartamentos.services;


import com.example.apartamentos.models.MensajeModel;
import com.example.apartamentos.repositories.IMensajeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MensajeService {

    private final IMensajeRepository mensajeRepository;

    public MensajeService(IMensajeRepository mensajeRepository) {
        this.mensajeRepository = mensajeRepository;
    }

    public List<MensajeModel> getAllMensajes() {
        return mensajeRepository.findAll();
    }

    public Optional<MensajeModel> getMensajeById(Integer id) {
        return mensajeRepository.findById(id);
    }

    public MensajeModel saveMensaje(MensajeModel mensaje) {
        return mensajeRepository.save(mensaje);
    }

    public void deleteMensaje(Integer id) {
        mensajeRepository.deleteById(id);
    }
}
