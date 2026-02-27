# 📋 To-Do List — Spring Boot + H2 + Docker

Aplicación web de gestión de tareas desarrollada con Spring Boot como ejercicio de aprendizaje. Incluye autenticación, área pública y área privada.

---

## 🛠️ Tecnologías

- **Spring Boot 3** — Framework principal
- **Spring MVC** — Controladores y rutas
- **Spring Security** — Autenticación y autorización
- **Spring Data JPA + Hibernate** — Acceso a base de datos
- **H2 Database** — Base de datos embebida
- **Thymeleaf** — Motor de plantillas HTML
- **Docker + Docker Compose** — Contenedorización

---

## ✨ Funcionalidades

- Registro de usuarios con validación de formulario
- Login y logout con sesiones
- Área pública visible sin autenticación
- Área privada protegida por Spring Security
- Crear, completar y eliminar tareas
- Cada usuario solo ve sus propias tareas
- Contador de tareas pendientes y completadas
- Consola web H2 para inspeccionar la base de datos

---

## 🚀 Cómo iniciar el proyecto

### Requisitos
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado y corriendo

### Pasos

1. Clona el repositorio
```bash
git clone https://github.com/Ramonmendoza13/todolist.git
cd todolist
```

2. Construye y arranca los contenedores
```bash
docker compose up --build
```

3. Abre el navegador en **http://localhost:8080**

### Usuario de prueba
| Campo | Valor |
|---|---|
| Usuario | `admin` |
| Contraseña | `password123` |

---

## 🗄️ Consola H2

Para inspeccionar la base de datos directamente desde el navegador:

- URL: **http://localhost:8080/h2-console**
- JDBC URL: `jdbc:h2:file:./data/todolist`
- User: `sa`
- Password: *(vacío)*

---

## 🛑 Parar el proyecto

```bash
# Parar los contenedores
docker compose down

# Parar y borrar los datos (resetea la base de datos)
docker compose down -v
```