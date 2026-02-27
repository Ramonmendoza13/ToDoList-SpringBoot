package com.ejemplo.todolist;

import com.ejemplo.todolist.model.User;
import com.ejemplo.todolist.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TodolistApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodolistApplication.class, args);
    }

    // ↓ Este método se ejecuta cuando la aplicación arranca.
    // Es el equivalente a los Seeders de Laravel.
    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Solo creamos el usuario si no existe (para no duplicarlo en cada reinicio)
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User( "test@mail.com",
                        "admin",
                        passwordEncoder.encode("password123")  // ← Encriptamos con BCrypt
                );
                userRepository.save(admin);
                System.out.println("✅ Usuario de prueba creado: admin / password123");
            }
        };
    }
}