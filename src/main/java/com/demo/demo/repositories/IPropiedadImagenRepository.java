package com.example.apartamentos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.apartamentos.models.PropiedadimagenModel;

@Repository
public interface IPropiedadImagenRepository extends JpaRepository<PropiedadimagenModel, Long> {

}
