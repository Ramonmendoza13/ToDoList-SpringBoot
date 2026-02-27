package com.ejemplo.todolist.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String mail;

    @Column(nullable = false, unique = true)  // unique = UNIQUE en SQL
    @NotBlank
    private String username;

    @Column(nullable = false)
    @NotBlank
    private String password;  // Aquí guardaremos la contraseña ENCRIPTADA (bcrypt)

    // Un usuario tiene muchas tareas
    // mappedBy = el nombre del campo en Task que tiene la relación
    // cascade = si borramos el usuario, se borran sus tareas también
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks;

    // ── Constructor vacío OBLIGATORIO para JPA ──
    public User() {}

    public User(String mail,String username, String password) {
        this.mail = mail;
        this.username = username;
        this.password = password;
    }

    // ── Getters y Setters ──
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMail() {return mail;}

    public void setMail(String mail) { this.mail = mail;}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }
}