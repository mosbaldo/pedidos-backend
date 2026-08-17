# Módulo de Gestión de Pedidos - Puerto de Liverpool (pedidos-backend)

Este repositorio contiene la implementación del microservicio **pedidos-backend**, desarrollado como parte de la evaluación técnica para el equipo de desarrollo de **El Puerto de Liverpool**. El sistema permite gestionar los datos de clientes, sus direcciones de envío y la sincronización con sus correspondientes pedidos y productos consumidos desde servicios externos, utilizando una arquitectura moderna, tolerante a fallos y altamente escalable.

---

## 🏗️ Arquitectura y Patrones de Diseño (No MVC)

Para cumplir estrictamente con los lineamientos del examen de **no admitir el patrón MVC tradicional** y asegurar un diseño limpio y mantenible, el proyecto se estructuró bajo los principios de **Clean Architecture (Arquitectura Limpia)** con un enfoque de **Puertos y Adaptadores (Arquitectura Hexagonal)**.

A continuación se presenta el diagrama de arquitectura actualizado que representa el flujo y desacoplamiento de dependencias del sistema:

<img width="2048" height="1459" alt="diagrama-arquitectura" src="https://github.com/user-attachments/assets/69349474-e4c3-43fa-b2ca-b2bc0d5053df" />

La aplicación se encuentra desacoplada en tres capas principales que garantizan la separación de responsabilidades y la inversión de dependencias:

1. **Capa de Dominio (`domain`):** El núcleo de la aplicación. Contiene los modelos de negocio puros (`Customer`, `Order`, `Item`) y las excepciones funcionales (`CustomerNotFoundException`). Esta capa es agnóstica a la infraestructura, bases de datos y frameworks.
2. **Capa de Aplicación (`application`):** Define los casos de uso del sistema. Aquí se encuentra la lógica para crear, actualizar, consultar y eliminar clientes, así como la sincronización de pedidos. Orquesta los flujos de datos comunicándose con el exterior a través de **Puertos** (interfaces).
3. **Capa de Infraestructura (`infrastructure`):** Contiene los adaptadores tecnológicos concretos que se conectan con el mundo exterior:
   * **`rest` (API Inbound):** Define los controladores REST autogenerados por OpenAPI a partir del contrato de diseño.
   * **`persistence` (DB Outbound):** Adaptador de Spring Data MongoDB para la persistencia física de clientes en MongoDB Atlas.
   * **`client` (HTTP Outbound):** Adaptador reactivo (usando `WebClient`) para consumir las APIs externas de `/pedidos` e `/items` de forma resiliente y no bloqueante.
   * **`mapper`:** Traductores de tipos de datos utilizando **MapStruct** para desacoplar DTOs de API, documentos de base de datos y modelos de dominio de manera bidireccional.

---

## 🛠️ Tecnologías y Herramientas Utilizadas

* **Java 17 (LTS)**
* **Spring Boot 3.5.x**
* **MongoDB (Atlas / Local)**
* **Spring WebFlux (WebClient)**
* **OpenAPI Generator (Contract-First)**
* **MapStruct**
* **Lombok**
* **Testcontainers (con MongoDB 4.4)**

---

## 🚀 Requisitos Previos

Asegúrate de contar con las siguientes herramientas instaladas en tu entorno local antes de arrancar la aplicación:

* **Java Development Kit (JDK) 17** (ej. Eclipse Temurin o similar)
* **Apache Maven 3.8+**
* **Docker Desktop** (para base de datos local y ejecución automatizada de Testcontainers)
* Un cliente REST como **Postman**, **Insomnia** o la interfaz gráfica integrada de **Swagger UI**.

---

## 🖥️ Cómo Levantar el Proyecto en Ambiente Local

### 1. Clonar el Repositorio
```bash
git clone <URL_DEL_REPOSITORIO>
cd pedidos-backend
```

### 2. Iniciar MongoDB de Forma Local (Docker)
Para iniciar una instancia limpia de MongoDB en un contenedor de Docker local expuesto en el puerto estándar (27017):
```bash
docker run -d -p 27017:27017 --name mongo-local mongo:latest
```

### 3. Generar Clases e Interfaces Base (OpenAPI & MapStruct)
Antes del primer arranque de la aplicación, es necesario ejecutar el proceso de compilación para que Maven genere los DTOs, controladores y mapeadores basados en el contrato OpenAPI y las interfaces de MapStruct:
```bash
mvn clean compile
```

### 4. Arrancar la Aplicación
Una vez compilado el proyecto y con Docker activo, arranca el servidor embebido de Spring Boot:
```bash
mvn spring-boot:run
```
El microservicio se iniciará por defecto en el puerto **8080** y con el contexto de versión base **/api/v1** (ej. `http://localhost:8080/api/v1`).

---

## 🧪 Ejecución de la Suite de Pruebas (Testcontainers)

El desarrollo del proyecto incluye una suite de pruebas de integración para la capa de persistencia utilizando **Testcontainers**. 

Las pruebas de base de datos se ejecutan de forma aislada y repetible. Testcontainers detectará tu entorno de Docker local, descargará una imagen ligera de MongoDB (versión `4.4` para máxima compatibilidad de CPU sin requisitos AVX) y la iniciará en un puerto aleatorio efímero exclusivamente para la duración de los tests, destruyéndola automáticamente al finalizar.

Para ejecutar todas las pruebas unitarias y de integración:
```bash
mvn clean test
```

---

## 🌐 Flujo de CI/CD (GitHub Actions)

El proyecto cuenta con un flujo automatizado de integración y entrega continua (CI/CD) configurado en GitHub Actions bajo el estándar de GitFlow:

* **Calidad de Código y Validación (CI):** El job `build` valida que cualquier Pull Request hacia `develop` o `main` compile exitosamente y que todas las pruebas pasen en verde en la terminal antes de permitir el merge.
* **Despliegue Continuo (CD):** El job `publish-and-deploy` se activa de forma exclusiva al fusionar cambios a la rama `main`. Construye la imagen de producción usando un `Dockerfile` optimizado en múltiples etapas, la publica en **Docker Hub** y notifica a **Render** mediante un webhook para redesplegar el servicio en producción en segundos.

---

## 📖 Documentación de la API (Swagger UI)

Una vez que la aplicación esté corriendo localmente, puedes acceder a la consola interactiva de **Swagger UI** para documentar y probar todos los endpoints desde el navegador en la siguiente URL:

👉 **http://localhost:8080/api/v1/swagger-ui/index.html**

---

## 🔌 Referencia de Endpoints y Pruebas (cURL)

A continuación se presentan los comandos cURL recomendados para probar las operaciones CRUD del recurso Clientes en tu entorno local.

### 1. Registrar un Cliente Nuevo (POST)
Crea un cliente asociándole su dirección de envío. El sistema consumirá el MockAPI externo de `/pedidos` de forma no bloqueante, detectará si el `userId` tiene pedidos activos, los mapeará y persistirá el registro:
* **Endpoint:** `POST http://localhost:8080/api/v1/customers`
* **Código HTTP de Éxito:** `201 Created`

```bash
curl -X POST http://localhost:8080/api/v1/customers \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "75c97531-abf5-4524-8107-90aa48d08efc",
    "firstName": "Miguel",
    "lastName": "Gallardo",
    "secondLastName": "Toledo",
    "email": "osbald91@gmail.com",
    "shippingAddress": "Av. Principal 123, Col. Centro, CDMX"
  }'
```

### 2. Consultar Cliente por ID (GET)
Recupera el perfil de un cliente junto con sus direcciones de entrega y sus pedidos enriquecidos en tiempo real:
* **Endpoint:** `GET http://localhost:8080/api/v1/customers/{userId}`
* **Código HTTP de Éxito:** `200 OK`

```bash
curl -X GET http://localhost:8080/api/v1/customers/75c97531-abf5-4524-8107-90aa48d08efc
```

### 3. Actualizar Cliente Existente (PUT)
Permite modificar los datos básicos y la dirección de envío del usuario:
* **Endpoint:** `PUT http://localhost:8080/api/v1/customers/{userId}`
* **Código HTTP de Éxito:** `200 OK`

```bash
curl -X PUT http://localhost:8080/api/v1/customers/75c97531-abf5-4524-8107-90aa48d08efc \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Miguel Osbaldo",
    "lastName": "Gallardo",
    "secondLastName": "Toledo",
    "email": "osbald91@gmail.com",
    "shippingAddress": "Av. Universidad 456, Col. Del Valle, CDMX"
  }'
```

### 4. Eliminar un Cliente (DELETE)
Remueve físicamente el registro del cliente de la colección de MongoDB:
* **Endpoint:** `DELETE http://localhost:8080/api/v1/customers/{userId}`
* **Código HTTP de Éxito:** `204 No Content`

```bash
curl -X DELETE http://localhost:8080/api/v1/customers/75c97531-abf5-4524-8107-90aa48d08efc
```
