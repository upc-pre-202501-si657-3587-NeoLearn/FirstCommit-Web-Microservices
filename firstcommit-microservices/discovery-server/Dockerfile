# Usa una imagen base oficial de Java.
# Se recomienda usar la misma versión para todos los servicios (ej. openjdk:21-slim)
FROM openjdk:21-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo .jar compilado desde la carpeta 'target' a la raíz del directorio de trabajo
# El nombre del archivo .jar debe coincidir con el que genera Maven (ej: discovery-server-0.0.1-SNAPSHOT.jar)
COPY target/*.jar app.jar

# Expone el puerto en el que la aplicación Eureka corre por defecto
EXPOSE 8761

# El comando que se ejecutará cuando el contenedor inicie
ENTRYPOINT ["java", "-jar", "app.jar"]