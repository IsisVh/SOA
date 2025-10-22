package com.example.apartamentos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.apartamentos.models.PropiedadModel;
import com.example.apartamentos.repositories.IPropiedadRepository;

@Service
public class PropiedadService {

    @Autowired
    private IPropiedadRepository propiedadRepository;

    public List<PropiedadModel> getAllPropiedades() {
        return propiedadRepository.findAll();
    }

    public Optional<PropiedadModel> getPropiedadById(Long id) {
        return propiedadRepository.findById(id);
    }

    public PropiedadModel savePropiedad(PropiedadModel propiedad) {
        return propiedadRepository.save(propiedad);
    } 
    
    public void deletePropiedad(Long id) {
        propiedadRepository.deleteById(id);
    }

    // ACTIVIDAD: Crea un metodo Recuperar propiedades por tipo

     public List<PropiedadModel> getPropiedadesByTipo(String tipo) {
        return propiedadRepository.findByTipo(tipo);
    }
}
