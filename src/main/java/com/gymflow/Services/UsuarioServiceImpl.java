package com.gymflow.Services;

import com.gymflow.Repository.UsuarioRepository;
import com.gymflow.models.Rol;
import com.gymflow.models.Usuario;

import java.util.List;

public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario findByEmail(String email) {
        return null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public List<Usuario> findByRol(Rol rol) {
        return List.of();
    }
}
