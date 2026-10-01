package com.gymflow.Services;

import com.gymflow.models.Rol;
import com.gymflow.models.Usuario;

import java.util.List;

public interface UsuarioService {
    Usuario findByEmail(String email);
    boolean existsByEmail(String email);
    List<Usuario> findByRol(Rol rol);
}
