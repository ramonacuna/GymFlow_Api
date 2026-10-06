package com.gymflow.services;

import com.gymflow.repository.SocioRepository;
import com.gymflow.models.Socio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SocioServiceImpl implements SocioService {

    private final SocioRepository socioRepository;

    public SocioServiceImpl(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    @Override
    public List<Socio> findAll() {
        return socioRepository.findAll();
    }

    @Override
    public Optional<Socio> findById(Integer id) {
        return socioRepository.findById(id);
    }

    @Override
    public Socio save(Socio socio) {
        return socioRepository.save(socio);
    }

    @Override
    public void deleteById(Integer id) {
        socioRepository.deleteById(id);
    }

    @Override
    public Optional<Socio> findByDni(String dni) {
        return socioRepository.findByDni(dni);
    }

    @Override
    public Optional<Socio> findByDni(int dni) {
        return socioRepository.findByDni(String.valueOf(dni));
    }

    @Override
    public boolean existsByDni(String dni) {
        return socioRepository.existsByDni(dni);
    }

    @Override
    public boolean existsByDni(int dni) {
        return socioRepository.existsByDni(String.valueOf(dni));
    }

    @Override
    public Optional<Socio> findByUsuarioId(Integer usuarioId) {
        return socioRepository.findByUsuarioId(usuarioId);
    }
}
