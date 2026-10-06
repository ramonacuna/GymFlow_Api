package com.gymflow.controllers;

import com.gymflow.repository.RolRepository;
import com.gymflow.repository.UsuarioRepository;
import com.gymflow.services.SocioService;
import com.gymflow.dto.AuthRequest;
import com.gymflow.dto.AuthResponse;
import com.gymflow.dto.RegisterRequest;
import com.gymflow.models.Rol;
import com.gymflow.models.Socio;
import com.gymflow.models.Usuario;
import com.gymflow.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Autenticación", description = "Endpoints de autenticación y registro de usuarios con tokens JWT")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final SocioService socioService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtService jwtService,
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            SocioService socioService,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.socioService = socioService;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario con email y contraseña, retornando un token JWT para autorizar peticiones subsecuentes.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("rol", usuario.getRol() != null ? usuario.getRol().getName() : "USER");
        extraClaims.put("id", usuario.getId());

        String token = jwtService.generateToken(extraClaims, userDetails);

        AuthResponse response = AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .id(usuario.getId())
                .email(usuario.getEmail())
                .rol(usuario.getRol() != null ? usuario.getRol().getName() : "USER")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Registrar nuevo usuario", description = "Crea un nuevo usuario en el sistema. Opcionalmente crea el perfil de socio asociado.")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "El email ya está registrado"));
        }

        Rol rol;
        if (request.getRolId() != null) {
            rol = rolRepository.findById(request.getRolId())
                    .orElseThrow(() -> new IllegalArgumentException("El rol especificado no existe"));
        } else {
            rol = rolRepository.findByName("SOCIO")
                    .or(() -> rolRepository.findByName("ROLE_SOCIO"))
                    .or(() -> rolRepository.findByName("USER"))
                    .orElseGet(() -> rolRepository.save(new Rol(null, "SOCIO")));
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setEmail(request.getEmail());
        nuevoUsuario.setPassword(passwordEncoder.encode(request.getPassword()));
        nuevoUsuario.setRol(rol);
        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        // Si se enviaron datos de Socio, crearlo asociado
        if (request.getNombre() != null && request.getDni() != null) {
            Socio nuevoSocio = new Socio();
            nuevoSocio.setNombre(request.getNombre());
            nuevoSocio.setDni(request.getDni());
            nuevoSocio.setTelefono(request.getTelefono());
            nuevoSocio.setUsuario(usuarioGuardado);
            socioService.save(nuevoSocio);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(usuarioGuardado.getEmail());
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("rol", rol.getName());
        extraClaims.put("id", usuarioGuardado.getId());

        String token = jwtService.generateToken(extraClaims, userDetails);

        AuthResponse response = AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .id(usuarioGuardado.getId())
                .email(usuarioGuardado.getEmail())
                .rol(rol.getName())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
