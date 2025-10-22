package com.example.apartamentos.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.apartamentos.models.DisponibilidadModel;
import com.example.apartamentos.services.DisponibilidadService;

@RestController
@RequestMapping("/disponibilidad")
@CrossOrigin(origins = "*")
public class DisponibilidadController {

    @Autowired
    private DisponibilidadService disponibilidadService;

    @GetMapping
    public List<DisponibilidadModel> getAllDisponibilidades() {
        return disponibilidadService.getAllDisponibilidades();
    }

    @GetMapping("/{id}")
    public Optional<DisponibilidadModel> getDisponibilidadById(@PathVariable Long id) {
        return disponibilidadService.getDisponibilidadById(id);
    }

    @PostMapping
    public DisponibilidadModel saveDisponibilidad(@RequestBody DisponibilidadModel disponibilidad) {
        return disponibilidadService.saveDisponibilidad(disponibilidad);
    }

    @DeleteMapping("/{id}")
    public void deleteDisponibilidad(@PathVariable Long id) {
        disponibilidadService.deleteDisponibilidad(id);
    }
}
