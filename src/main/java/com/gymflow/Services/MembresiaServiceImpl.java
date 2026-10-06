package com.gymflow.services;

import com.gymflow.repository.MembresiaRepository;
import com.gymflow.models.Membresia;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MembresiaServiceImpl implements MembresiaService {
    private final MembresiaRepository membresiaRepository;

    public MembresiaServiceImpl(MembresiaRepository membresiaRepository) {
        this.membresiaRepository = membresiaRepository;
    }

    @Override
    public List<Membresia> findAll() {
        return membresiaRepository.findAll();
    }

    @Override
    public Optional<Membresia> findById(Integer id) {
        return membresiaRepository.findById(id);
    }

    @Override
    public Membresia save(Membresia membresia) {
        return membresiaRepository.save(membresia);
    }

    @Override
    public void deleteById(Integer id) {
        membresiaRepository.deleteById(id);
    }

    @Override
    public List<Membresia> findBySocioId(Integer socioId) {
        return membresiaRepository.findBySocio_Id(socioId);
    }

    @Override
    public Optional<Membresia> findBySocioIdAndEstado(Integer socioId, String estado) {
        return membresiaRepository.findBySocio_IdAndEstado(socioId, estado);
    }

    @Override
    public List<Membresia> findByEstado(String estado) {
        return membresiaRepository.findByEstado(estado);
    }
}
