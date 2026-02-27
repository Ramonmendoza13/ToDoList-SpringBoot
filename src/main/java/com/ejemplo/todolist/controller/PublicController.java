package com.ejemplo.todolist.controller;

// ↓ @Controller indica que esta clase es un controlador MVC
import com.ejemplo.todolist.dto.RegisterDto;
import com.ejemplo.todolist.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
// ↓ Para mapear rutas
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller  // ← ¡No olvides esta anotación!
public class PublicController {

    // Inyectamos UserService porque ahora necesitamos la lógica de registro
    private final UserService userService;

    public PublicController(UserService userService) {
        this.userService = userService;
    }

    // Página de inicio pública.
    // Equivale a Route::get('/', ...) en Laravel.
    @GetMapping("/")
    public String index() {
        // Devolvemos el nombre de la plantilla Thymeleaf (sin .html)
        // Spring buscará: src/main/resources/templates/index.html
        return "index";
    }

    // Página de login
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // ── GET /register ─────────────────────────────────────────────
    // Simplemente muestra el formulario vacío.
    // Pasamos un RegisterDto vacío al modelo para que Thymeleaf
    // pueda enlazar cada campo con th:field
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "register";
    }

    // ── POST /register ────────────────────────────────────────────
    @PostMapping("/register")
    public String register(
            // ↓ @Valid activa las validaciones del DTO (@NotBlank, @Email, @Size...)
            @Valid
            // ↓ @ModelAttribute enlaza el formulario HTML con el objeto RegisterDto
            @ModelAttribute("registerDto") RegisterDto registerDto,
            // ↓ BindingResult recoge los errores de validación.
            // IMPORTANTE: debe ir justo después del objeto validado, si no Spring
            // lanza excepción en vez de capturar los errores
            BindingResult bindingResult,
            // ↓ Para pasar mensajes flash a la siguiente página tras redirigir
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        // Si hay errores de validación (@NotBlank, @Email, etc.)
        // volvemos a mostrar el formulario con los mensajes de error
        if (bindingResult.hasErrors()) {
            return "register";
        }

        // Llamamos al servicio. Devuelve null si todo fue bien,
        // o un String con el mensaje de error si algo falló
        String error = userService.register(registerDto);

        if (error != null) {
            // Añadimos el error al modelo para mostrarlo en el formulario
            model.addAttribute("error", error);
            return "register";
        }

        // Todo correcto: redirigimos al login con mensaje de éxito
        // addFlashAttribute sobrevive a la redirección (como ->with() en Laravel)
        redirectAttributes.addFlashAttribute("success", "Cuenta creada correctamente. Ya puedes iniciar sesión.");
        return "redirect:/login";
    }
}