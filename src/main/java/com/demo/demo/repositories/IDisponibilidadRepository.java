package com.example.apartamentos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.apartamentos.models.DisponibilidadModel;

@Repository
public interface IDisponibilidadRepository extends JpaRepository<DisponibilidadModel, Long> {

}
