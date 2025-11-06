package com.example.apartamentos.services;

import com.example.apartamentos.models.ResenaModel;
import com.example.apartamentos.repositories.IResenaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResenaService {

    @Autowired
    private IResenaRepository resenaRepo;

   
    public List<ResenaModel> getAllResenas() {
        return resenaRepo.findAll();
    }

    
    public Optional<ResenaModel> getResenaById(Integer id) {
        return resenaRepo.findById(id);
    }

   
    public ResenaModel saveResena(ResenaModel resena) {
        return resenaRepo.save(resena);
    }

    
    public void deleteResena(Integer id) {
        resenaRepo.deleteById(id);
    }
}
