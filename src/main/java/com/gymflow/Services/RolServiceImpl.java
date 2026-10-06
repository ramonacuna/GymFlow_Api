package com.gymflow.services;

import com.gymflow.repository.RolRepository;
import com.gymflow.models.Rol;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolServiceImpl implements RolService {
    private final RolRepository rolRepository;

    public RolServiceImpl(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    @Override
    public Optional<Rol> findById(Integer id) {
        return rolRepository.findById(id);
    }

    @Override
    public Rol save(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    public void deleteById(Integer id) {
        rolRepository.deleteById(id);
    }

    @Override
    public Optional<Rol> findByNombre(String nombre) {
        return rolRepository.findByName(nombre);
    }
}
