package com.example.apartamentos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.apartamentos.models.ResenaModel;



public interface IResenaRepository extends JpaRepository<ResenaModel, Integer> {

}

