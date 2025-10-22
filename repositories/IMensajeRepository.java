package com.example.apartamentos.repositories;

import com.example.apartamentos.models.MensajeModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMensajeRepository extends JpaRepository<MensajeModel, Integer> {
}
