package com.ejemplo.todolist.controller;

import com.ejemplo.todolist.model.Task;
import com.ejemplo.todolist.model.User;
import com.ejemplo.todolist.repository.UserRepository;
import com.ejemplo.todolist.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
// ↓ Prefijo común para todas las rutas de este controlador.
// Equivale a Route::prefix('tasks')... en Laravel.
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    public TaskController(TaskService taskService, UserRepository userRepository) {
        this.taskService = taskService;
        this.userRepository = userRepository;
    }

    // ── Método auxiliar: obtener usuario actual de BD ────────────
    // @AuthenticationPrincipal UserDetails nos da el usuario autenticado de la sesión,
    // pero es el UserDetails de Spring Security, no nuestra entidad User.
    // Con este método obtenemos nuestra entidad User real de la BD.
    private User getCurrentUser(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // ── GET /tasks ────────────────────────────────────────────────
    // Lista todas las tareas del usuario autenticado
    @GetMapping
    public String listTasks(
            // ↓ Inyecta el usuario autenticado automáticamente desde la sesión
            @AuthenticationPrincipal UserDetails userDetails,
            // ↓ Model es el equivalente a compact() en Laravel — pasa datos a la vista
            Model model
    ) {
        User user = getCurrentUser(userDetails);
        List<Task> tasks = taskService.getTasksByUser(user);

        // Añadimos datos al modelo (disponibles en la plantilla Thymeleaf)
        model.addAttribute("tasks", tasks);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("pendingCount", taskService.countPendingTasks(user));
        model.addAttribute("completedCount", taskService.countCompletedTasks(user));

        return "tasks/list";  // → templates/tasks/list.html
    }

    // ── POST /tasks ───────────────────────────────────────────────
    // Crear una nueva tarea
    @PostMapping
    public String createTask(
            @AuthenticationPrincipal UserDetails userDetails,
            // ↓ @RequestParam captura campos del formulario
            @RequestParam String title,
            @RequestParam(required = false) String description,
            // ↓ RedirectAttributes permite pasar mensajes flash tras redirección.
            // Equivale a ->with('success', '...') en Laravel.
            RedirectAttributes redirectAttributes
    ) {
        if (title == null || title.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El título no puede estar vacío");
            return "redirect:/tasks";
        }

        User user = getCurrentUser(userDetails);
        taskService.createTask(title.trim(), description, user);

        redirectAttributes.addFlashAttribute("success", "Tarea creada correctamente");
        return "redirect:/tasks";  // Redirige a GET /tasks
    }

    // ── POST /tasks/{id}/toggle ───────────────────────────────────
    // Marcar como completada/pendiente
    @PostMapping("/{id}/toggle")
    public String toggleTask(
            @AuthenticationPrincipal UserDetails userDetails,
            // ↓ @PathVariable captura el {id} de la URL.
            // Equivale a $id en Route::post('/tasks/{id}/toggle', ...) en Laravel.
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        User user = getCurrentUser(userDetails);
        boolean success = taskService.toggleTask(id, user);

        if (!success) {
            redirectAttributes.addFlashAttribute("error", "Tarea no encontrada");
        }

        return "redirect:/tasks";
    }

    // ── POST /tasks/{id}/delete ───────────────────────────────────
    // Eliminar tarea.
    // Nota: los formularios HTML solo soportan GET y POST.
    // Por eso usamos POST en vez de DELETE.
    @PostMapping("/{id}/delete")
    public String deleteTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        User user = getCurrentUser(userDetails);
        boolean success = taskService.deleteTask(id, user);

        if (success) {
            redirectAttributes.addFlashAttribute("success", "Tarea eliminada");
        } else {
            redirectAttributes.addFlashAttribute("error", "Tarea no encontrada");
        }

        return "redirect:/tasks";
    }
}