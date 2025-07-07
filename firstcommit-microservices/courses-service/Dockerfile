


# Usa una imagen base oficial de Java. Asegúrate de que coincida con la versión con la que compilas.
FROM openjdk:21-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo .jar compilado desde la carpeta 'target' a la raíz del directorio de trabajo en el contenedor
# El nombre del archivo .jar debe coincidir con el que genera Maven.
COPY target/*.jar app.jar

# Expone el puerto en el que la aplicación Spring Boot corre por defecto
EXPOSE 8080

# El comando que se ejecutará cuando el contenedor inicie
ENTRYPOINT ["java", "-jar", "app.jar"]