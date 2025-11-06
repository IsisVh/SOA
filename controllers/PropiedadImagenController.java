package com.example.apartamentos.controllers;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.apartamentos.models.PropiedadimagenModel;
import com.example.apartamentos.services.PropiedadImagenService;

@RestController
@RequestMapping("/imagenes")
@CrossOrigin(origins = "*")
public class PropiedadImagenController {

    @Autowired
    private PropiedadImagenService imagenService;

    @GetMapping
    public List<PropiedadimagenModel> getAllImagenes() {
        return imagenService.getAllImagenes();
    }

    @GetMapping("/{id}")
    public Optional<PropiedadimagenModel> getImagenById(@PathVariable Long id) {
        return imagenService.getImagenById(id);
    }

    @PostMapping
    public PropiedadimagenModel saveImagen(@Valid @RequestBody PropiedadimagenModel imagen) {
        return imagenService.saveImagen(imagen);
    }

    @DeleteMapping("/{id}")
    public void deleteImagen(@PathVariable Long id) {
        imagenService.deleteImagen(id);
    }
}

