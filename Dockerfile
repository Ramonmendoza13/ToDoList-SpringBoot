# Dockerfile

# ETAPA 1: Compilar la aplicación
# Usamos una imagen con Maven y Java 21 para compilar
FROM maven:3.9-eclipse-temurin-21 AS build

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos primero solo el pom.xml para aprovechar la caché de Docker.
# Si el pom.xml no cambia, Docker no vuelve a descargar las dependencias.
COPY pom.xml .
RUN mvn dependency:go-offline

# Ahora copiamos el código fuente
COPY src ./src

# Compilamos y generamos el JAR (saltamos los tests para que sea más rápido)
RUN mvn clean package -DskipTests

# ---------------------------------------------------------------

# ETAPA 2: Imagen final (más pequeña, sin Maven)
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copiamos solo el JAR generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Creamos la carpeta donde H2 guardará el archivo de base de datos
RUN mkdir -p /app/data

# Exponemos el puerto 8080 (el que usa Spring Boot por defecto)
EXPOSE 8080

# Comando que se ejecuta cuando arranca el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]