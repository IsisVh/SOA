package com.example.apartamentos.controllers;

import com.example.apartamentos.models.ResenaModel;
import com.example.apartamentos.services.ResenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/resenas")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    // Recuperar todas las reseñas
    @GetMapping
    public List<ResenaModel> getAllResenas() {
        return resenaService.getAllResenas();
    }

    // Obtener una reseña por ID
    @GetMapping("/{id}")
    public ResponseEntity<ResenaModel> getResenaById(@PathVariable Integer id) {
        Optional<ResenaModel> resena = resenaService.getResenaById(id);
        return resena.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());

    }
}