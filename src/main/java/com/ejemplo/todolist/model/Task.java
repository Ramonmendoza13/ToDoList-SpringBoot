package com.ejemplo.todolist.model;

// Importaciones de JPA (Java Persistence API)
import jakarta.persistence.*;
// Importaciones de validación
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

// ↓ Le dice a JPA que esta clase es una tabla en la base de datos
@Entity
// ↓ Nombre de la tabla en H2 (opcional, por convención sería "task")
@Table(name = "tasks")
public class Task {

    // ↓ Esta es la clave primaria (equivale a id BIGINT PRIMARY KEY)
    @Id
    // ↓ El valor se genera automáticamente (AUTO_INCREMENT)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ↓ @Column define cómo es la columna en la tabla
    @Column(nullable = false)   // NOT NULL en SQL
    // ↓ Validación: no puede estar vacío (se valida en el controlador)
    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 200, message = "El título no puede tener más de 200 caracteres")
    private String title;

    // Descripción opcional, puede ser null
    @Column(columnDefinition = "TEXT")  // Columna de tipo TEXT
    private String description;

    // ↓ false = pendiente, true = completada
    @Column(nullable = false)
    private boolean completed = false;  // Valor por defecto

    // Fecha de creación
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Relación con el usuario dueño de la tarea
    // Muchas tareas pertenecen a un usuario (Many tasks → One user)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)  // Columna de clave foránea
    private User user;

    // ↓ Este método se ejecuta automáticamente ANTES de guardar en BD
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // ── Constructores ──────────────────────────────────────────

    // Constructor vacío OBLIGATORIO para JPA
    public Task() {}

    public Task(String title, String description, User user) {
        this.title = title;
        this.description = description;
        this.user = user;
    }

    // ── Getters y Setters ──────────────────────────────────────
    // En IntelliJ: clic derecho → Generate → Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}