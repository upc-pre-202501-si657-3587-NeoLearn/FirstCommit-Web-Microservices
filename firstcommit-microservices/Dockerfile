# --- Stage 1: Build ---
# Usamos una imagen de Maven para compilar TODO el proyecto
FROM maven:3.9-eclipse-temurin-21 AS build

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos TODOS los pom.xml primero para aprovechar la caché de capas de Docker
# Esto es crucial para acelerar builds futuras.
COPY pom.xml .
COPY common-events/pom.xml ./common-events/
COPY courses-service/pom.xml ./courses-service/
COPY discovery-server/pom.xml ./discovery-server/
COPY gateway-server/pom.xml ./gateway-server/
COPY iam-service/pom.xml ./iam-service/
COPY memberships-service/pom.xml ./memberships-service/
COPY projects-service/pom.xml ./projects-service/
COPY roadmaps-service/pom.xml ./roadmaps-service/

# Descargamos todas las dependencias
RUN mvn dependency:go-offline

# Ahora copiamos el resto del código fuente
COPY . .

# Compilamos TODO el proyecto, saltando los tests para velocidad
RUN mvn clean package -DskipTests

# --- Stage 2: Final Image (Plantilla) ---
# Esta es una imagen base que usaremos para cada microservicio
FROM eclipse-temurin:21-jre-alpine AS final

# Creamos un usuario no-root por seguridad
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

# Definimos un argumento para el JAR que vamos a copiar
ARG JAR_FILE
ARG SERVICE_NAME

# Copiamos el JAR específico desde el stage de build
COPY --from=build /app/${SERVICE_NAME}/target/${JAR_FILE} app.jar

# Expone el puerto por defecto
EXPOSE 8080

# Comando de ejecución
ENTRYPOINT ["java","-jar","/app.jar"]