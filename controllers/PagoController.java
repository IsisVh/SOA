package com.example.apartamentos.controllers;

import com.example.apartamentos.models.PagoModel;
import com.example.apartamentos.services.PagoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    // ✅ Obtener todos los pagos
    @GetMapping
    public List<PagoModel> getAllPagos() {
        return pagoService.getAllPagos();
    }

    // ✅ Obtener un pago por ID
    @GetMapping("/{id}")
    public ResponseEntity<PagoModel> getPagoById(@PathVariable Integer id) {
        Optional<PagoModel> pago = pagoService.getPagoById(id);
        return pago.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Crear un nuevo pago
    @PostMapping
    public PagoModel createPago(@Valid @RequestBody PagoModel pago) {
        return pagoService.savePago(pago);
    }

    // ✅ Actualizar un pago existente
    @PutMapping("/{id}")
    public ResponseEntity<PagoModel> updatePago(@PathVariable Integer id, @Valid @RequestBody PagoModel pagoDetails) {
        Optional<PagoModel> pagoOptional = pagoService.getPagoById(id);
        if (pagoOptional.isPresent()) {
            PagoModel pagoToUpdate = pagoOptional.get();
            pagoToUpdate.setMonto(pagoDetails.getMonto());
            pagoToUpdate.setMetodoPago(pagoDetails.getMetodoPago());
            pagoToUpdate.setEstado(pagoDetails.getEstado());
            pagoToUpdate.setFechaPago(pagoDetails.getFechaPago());
            pagoToUpdate.setReferenciaPago(pagoDetails.getReferenciaPago());
            pagoToUpdate.setDatosPago(pagoDetails.getDatosPago());
            pagoToUpdate.setReservacion(pagoDetails.getReservacion());

            PagoModel updatedPago = pagoService.savePago(pagoToUpdate);
            return ResponseEntity.ok(updatedPago);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Eliminar un pago
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePago(@PathVariable Integer id) {
        Optional<PagoModel> pago = pagoService.getPagoById(id);
        if (pago.isPresent()) {
            pagoService.deletePago(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
