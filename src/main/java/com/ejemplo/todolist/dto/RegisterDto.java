package com.ejemplo.todolist.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// ↓ NO tiene @Entity, NO es una tabla. Es solo un objeto para transportar
// los datos del formulario HTML hasta el controlador.
public class RegisterDto {

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 3, max = 20, message = "El usuario debe tener entre 3 y 20 caracteres")
    private String username;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotBlank(message = "Debes confirmar la contraseña")
    private String confirmPassword;

    // ── Constructor vacío necesario para que Thymeleaf pueda crear el objeto ──
    public RegisterDto() {}

    // ── Getters y Setters ──
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}