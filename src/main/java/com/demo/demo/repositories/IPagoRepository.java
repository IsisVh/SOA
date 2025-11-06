package com.example.apartamentos.repositories;

import com.example.apartamentos.models.PagoModel;

import org.springframework.data.jpa.repository.JpaRepository;



public interface IPagoRepository extends JpaRepository<PagoModel, Integer> {
  
}
