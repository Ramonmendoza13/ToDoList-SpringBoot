package com.ejemplo.todolist.service;

import com.ejemplo.todolist.model.User;
import com.ejemplo.todolist.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Spring Security llama a este método cuando alguien intenta hacer login.
    // Recibe el username del formulario y debe devolver un UserDetails.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Buscamos el usuario en BD
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado: " + username
                ));

        // Devolvemos un UserDetails con username, contraseña encriptada y roles.
        // Spring Security se encarga de verificar la contraseña automáticamente.
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                // ↑ Por simplificar, todos los usuarios tienen el rol ROLE_USER
        );
    }
}