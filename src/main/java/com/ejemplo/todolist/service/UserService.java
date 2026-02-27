package com.ejemplo.todolist.service;

import com.ejemplo.todolist.dto.RegisterDto;
import com.ejemplo.todolist.model.User;
import com.ejemplo.todolist.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Inyectamos los dos: el repositorio para guardar en BD
    // y el passwordEncoder para encriptar la contraseña
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Este método devuelve un String con el error, o null si todo fue bien.
    // Así el controlador sabrá si hay que mostrar un error o redirigir al login.
    public String register(RegisterDto dto) {

        // Comprobación 1: ¿el username ya existe?
        if (userRepository.existsByUsername(dto.getUsername())) {
            return "El nombre de usuario ya está en uso";
        }

        // Comprobación 2: ¿el email ya existe?
        if (userRepository.existsByMail(dto.getEmail())) {
            return "El email ya está registrado";
        }

        // Comprobación 3: ¿las contraseñas coinciden?
        // Esta validación no se puede hacer con anotaciones porque
        // compara dos campos distintos del DTO, hay que hacerla a mano.
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            return "Las contraseñas no coinciden";
        }

        // Todo correcto: creamos el User real a partir del DTO
        // Aquí es donde el DTO se "convierte" en entidad
        User user = new User(
                dto.getEmail(),
                dto.getUsername(),
                passwordEncoder.encode(dto.getPassword())  // ← Encriptamos con BCrypt
        );

        userRepository.save(user);

        return null;  // null = sin errores = registro exitoso
    }
}