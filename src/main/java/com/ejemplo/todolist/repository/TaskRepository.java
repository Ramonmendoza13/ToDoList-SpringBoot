package com.ejemplo.todolist.repository;

import com.ejemplo.todolist.model.Task;
import com.ejemplo.todolist.model.User;
// JpaRepository nos da gratis: findAll, findById, save, delete, count...
import org.springframework.data.jpa.repository.JpaRepository;
// ↓ Esta anotación indica que es un repositorio (componente de acceso a datos)
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// ↓ JpaRepository<TipoEntidad, TipoDeLaClavePrimaria>
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Spring genera el SQL automáticamente a partir del nombre del método.
    // "findBy" + "User" → WHERE user_id = ?
    List<Task> findByUser(User user);

    // "findBy" + "User" + "And" + "Completed" → WHERE user_id = ? AND completed = ?
    List<Task> findByUserAndCompleted(User user, boolean completed);

    // Buscar por id y usuario (para seguridad: un usuario no puede ver tareas de otro)
    Optional<Task> findByIdAndUser(Long id, User user);

    // Contar tareas pendientes de un usuario
    long countByUserAndCompleted(User user, boolean completed);
}