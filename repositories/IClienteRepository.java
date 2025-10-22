package com.example.apartamentos.repositories;

import com.example.apartamentos.models.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IClienteRepository extends JpaRepository<ClienteModel, Long> {
    //aqui no lleva codigo
}
