package com.example.apartamentos.services;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.apartamentos.models.PropiedadimagenModel;
import com.example.apartamentos.repositories.IPropiedadImagenRepository;

@Service
public class PropiedadImagenService {

    @Autowired
    private IPropiedadImagenRepository propiedadImagenRepository;

    public List<PropiedadimagenModel> getAllImagenes() {
        return propiedadImagenRepository.findAll();
    }

    public Optional<PropiedadimagenModel> getImagenById(Long id) {
        return propiedadImagenRepository.findById(id);
    }

    public PropiedadimagenModel saveImagen(PropiedadimagenModel imagen) {
        return propiedadImagenRepository.save(imagen);
    }

    public void deleteImagen(Long id) {
        propiedadImagenRepository.deleteById(id);
    }
}
