package com.example.apartamentos.controllers;

import com.example.apartamentos.models.MensajeModel;
import com.example.apartamentos.services.MensajeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mensajes")
@CrossOrigin(origins = "*")
public class MensajeController {

    private final MensajeService mensajeService;

    public MensajeController(MensajeService mensajeService) {
        this.mensajeService = mensajeService;
    }

    @GetMapping
    public List<MensajeModel> getAllMensajes() {
        return mensajeService.getAllMensajes();
    }

    @GetMapping("/{id}")
    public Optional<MensajeModel> getMensajeById(@PathVariable Integer id) {
        return mensajeService.getMensajeById(id);
    }

    @PostMapping
    public MensajeModel saveMensaje(@Valid @RequestBody MensajeModel mensaje) {
        return mensajeService.saveMensaje(mensaje);
    }

    @DeleteMapping("/{id}")
    public void deleteMensaje(@PathVariable Integer id) {
        mensajeService.deleteMensaje(id);
    }
}
