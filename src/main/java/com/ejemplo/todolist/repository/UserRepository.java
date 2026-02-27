package com.ejemplo.todolist.repository;

import com.ejemplo.todolist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring genera: SELECT * FROM users WHERE username = ?
    // Optional porque el usuario puede no existir (no lanza excepción)
    Optional<User> findByUsername(String username);

    // Para comprobar si un username ya está registrado
    boolean existsByUsername(String username);

    boolean existsByMail(String mail);

}