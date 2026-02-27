package com.ejemplo.todolist.service;

import com.ejemplo.todolist.model.Task;
import com.ejemplo.todolist.model.User;
import com.ejemplo.todolist.repository.TaskRepository;
// ↓ @Service indica que es un componente de servicio (Spring lo gestiona)
import org.springframework.stereotype.Service;
// ↓ Hace que las operaciones de BD sean transaccionales
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service  // ← ¡No olvides esta anotación!
public class TaskService {

    // Inyección de dependencias por constructor (recomendado sobre @Autowired)
    // Spring detecta que necesitamos TaskRepository y lo inyecta automáticamente
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Obtener todas las tareas de un usuario
    public List<Task> getTasksByUser(User user) {
        return taskRepository.findByUser(user);
    }

    // Crear una nueva tarea
    @Transactional  // Si algo falla, hace rollback automático
    public Task createTask(String title, String description, User user) {
        Task task = new Task(title, description, user);
        return taskRepository.save(task);  // save() = INSERT en BD
    }

    // Marcar tarea como completada/pendiente (toggle)
    @Transactional
    public boolean toggleTask(Long taskId, User user) {
        // findByIdAndUser protege que un usuario no pueda modificar tareas ajenas
        Optional<Task> optionalTask = taskRepository.findByIdAndUser(taskId, user);

        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setCompleted(!task.isCompleted());  // Cambia el estado
            taskRepository.save(task);  // save() con id existente = UPDATE en BD
            return true;
        }
        return false;  // Tarea no encontrada o no pertenece al usuario
    }

    // Eliminar una tarea
    @Transactional
    public boolean deleteTask(Long taskId, User user) {
        Optional<Task> optionalTask = taskRepository.findByIdAndUser(taskId, user);

        if (optionalTask.isPresent()) {
            taskRepository.delete(optionalTask.get());
            return true;
        }
        return false;
    }

    // Estadísticas rápidas
    public long countPendingTasks(User user) {
        return taskRepository.countByUserAndCompleted(user, false);
    }

    public long countCompletedTasks(User user) {
        return taskRepository.countByUserAndCompleted(user, true);
    }
}