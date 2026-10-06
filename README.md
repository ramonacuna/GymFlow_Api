# GymFlow API 🏋️‍♂️

GymFlow es una API RESTful desarrollada con **Spring Boot** y **Java 21** para la administración integral de gimnasios y centros deportivos. Permite gestionar socios, planes de membresía, suscripciones activas, usuarios del sistema y roles de acceso, asegurando cada operación mediante autenticación sin estado (**Stateless**) con **JWT (JSON Web Tokens)**.

---

## 🚀 Tecnologías Principales

- **Java 21**
- **Spring Boot** (Spring Data JPA, Spring Security, Spring Web)
- **SpringDoc OpenAPI / Swagger UI** (`springdoc-openapi-starter-webmvc-ui`)
- **JSON Web Token (jjwt)** (`0.12.6`)
- **MySQL** & **Hibernate**
- **Lombok**
- **Maven**

---

## 📐 Arquitectura del Proyecto

El sistema sigue una arquitectura en capas desacoplada y orientada al dominio:

```
src/main/java/com/gymflow/
├── config/             # Configuraciones globales (OpenAPI / Swagger)
├── controllers/        # Controladores REST y manejo de excepciones
├── dto/                # Data Transfer Objects y validaciones (Bean Validation)
├── models/             # Entidades JPA (Socio, Plan, Membresia, Usuario, Rol)
├── repository/         # Interfaces Spring Data JPA
├── security/           # Filtro JWT, UserDetailsService, AuthenticationEntryPoint
└── services/           # Lógica de negocio (interfaces e implementaciones)
```

### Modelo de Datos
- **Usuario**: Credenciales (`email`, `password` cifrado con BCrypt) y asociación a un `Rol`.
- **Rol**: Perfil de acceso (`ADMIN`, `SOCIO`, etc.).
- **Socio**: Datos de la persona (`nombre`, `dni`, `telefono`) vinculado uno a uno con su cuenta de `Usuario`.
- **Plan**: Ofertas de membresía (`nombre`, `precio`, `duracionDias`).
- **Membresía**: Relaciona un `Socio` con un `Plan` (`fechaInicio`, `fechaFin`, `estado`).

---

## 📖 Documentación Interactiva (Swagger / OpenAPI)

El proyecto cuenta con interfaz interactiva generada con **SpringDoc OpenAPI** para probar y explorar todos los endpoints desde el navegador.

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Especificación OpenAPI (JSON)**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### Cómo usar la documentación interactiva con JWT:
1. Inicia la aplicación.
2. Abre la URL de **Swagger UI** en tu navegador.
3. En la sección **Autenticación**, ejecuta la operación `POST /api/auth/login` con tus credenciales para obtener el token.
4. Copia el valor del campo `token` devuelto.
5. Haz clic en el botón verde **Authorize** (ubicado en la parte superior derecha de la interfaz).
6. Pega el token en el campo `Value` (Swagger lo prefija con `Bearer` de forma automática) y haz clic en **Authorize**.
7. ¡Listo! Ahora puedes ejecutar cualquiera de los endpoints protegidos directamente desde Swagger UI.

---

## 🔒 Seguridad y Control de Acceso

La seguridad se gestiona a través de tokens JWT con las siguientes reglas:

| Recurso / Endpoint | Método HTTP | Permisos Requeridos |
|---|---|---|
| `/api/auth/**` | POST | Público (Sin autenticación) |
| `/v3/api-docs/**`, `/swagger-ui/**`, `/swagger-ui.html` | GET | Público |
| `/api/planes/**` | GET | Público |
| `/api/planes/**` | POST, PUT, DELETE | Rol `ADMIN` |
| `/api/roles/**` | CUALQUIERA | Rol `ADMIN` |
| `/api/socios/**` | CUALQUIERA | Autenticado (Cualquier usuario con token válido) |
| `/api/membresias/**` | CUALQUIERA | Autenticado (Cualquier usuario con token válido) |
| `/api/usuarios/**` | CUALQUIERA | Autenticado (Cualquier usuario con token válido) |

---

## ⚙️ Configuración y Requisitos

### Requisitos Previos
- **Java JDK 21** o superior instalado.
- Servidor **MySQL 8.x** en ejecución.

### Configuración de Base de Datos
Edita el archivo `src/main/resources/application.properties` con tus credenciales de MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gymflow_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=tu_contraseña
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Seguridad JWT
jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
jwt.expiration=86400000
```

> **Nota:** La propiedad `createDatabaseIfNotExist=true` creará la base de datos `gymflow_db` automáticamente al iniciar por primera vez.

---

## 🛠️ Ejecución del Proyecto

### En Linux / macOS:
```bash
./mvnw clean compile
./mvnw spring-boot:run
```

### En Windows:
```powershell
.\mvnw.cmd clean compile
.\mvnw.cmd spring-boot:run
```

### Empaquetado para Producción:
```bash
./mvnw clean package -DskipTests
java -jar target/gymflow-0.0.1-SNAPSHOT.jar
```

La aplicación quedará disponible en el puerto por defecto: `http://localhost:8080`.

---

## 📡 Catálogo de Endpoints

### 1. Autenticación (`/api/auth`)
- `POST /api/auth/register`: Registrar una nueva cuenta de usuario y opcionalmente su perfil de socio.
- `POST /api/auth/login`: Autenticar credenciales y obtener el token JWT.

#### Ejemplo de Login:
```json
POST /api/auth/login
Content-Type: application/json

{
  "email": "admin@gymflow.com",
  "password": "Password123"
}
```

#### Respuesta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "id": 1,
  "email": "admin@gymflow.com",
  "rol": "ADMIN"
}
```

---

### 2. Planes (`/api/planes`)
- `GET /api/planes`: Obtener todos los planes disponibles.
- `GET /api/planes/{id}`: Detalle de un plan por ID.
- `GET /api/planes/nombre/{nombre}`: Buscar plan por nombre.
- `POST /api/planes`: Crear un nuevo plan (*Requiere ADMIN*).
- `PUT /api/planes/{id}`: Actualizar un plan (*Requiere ADMIN*).
- `DELETE /api/planes/{id}`: Eliminar un plan (*Requiere ADMIN*).

---

### 3. Socios (`/api/socios`)
- `GET /api/socios`: Listar todos los socios registrados.
- `GET /api/socios/{id}`: Obtener socio por ID.
- `GET /api/socios/dni/{dni}`: Buscar socio por número de DNI.
- `GET /api/socios/usuario/{usuarioId}`: Obtener el socio vinculado a un usuario.
- `POST /api/socios`: Registrar un nuevo socio (asociado a un `usuarioId`).
- `PUT /api/socios/{id}`: Actualizar información de un socio.
- `DELETE /api/socios/{id}`: Eliminar un socio.

---

### 4. Membresías (`/api/membresias`)
- `GET /api/membresias`: Listar todas las membresías.
- `GET /api/membresias/{id}`: Obtener membresía por ID.
- `GET /api/membresias/socio/{socioId}`: Historial de membresías de un socio.
- `GET /api/membresias/estado/{estado}`: Filtrar membresías por estado (`ACTIVA`, `VENCIDA`, etc.).
- `GET /api/membresias/socio/{socioId}/estado/{estado}`: Consultar membresía de un socio por estado.
- `POST /api/membresias`: Registrar nueva membresía asociando socio y plan.
- `PUT /api/membresias/{id}`: Actualizar estado o fechas de una membresía.
- `DELETE /api/membresias/{id}`: Eliminar una membresía.

---

### 5. Usuarios y Roles (`/api/usuarios`, `/api/roles`)
- `GET /api/usuarios`, `POST /api/usuarios`, `PUT /api/usuarios/{id}`, `DELETE /api/usuarios/{id}`: Administración de usuarios.
- `GET /api/roles`, `POST /api/roles`, `DELETE /api/roles/{id}`: Administración de roles (*Requiere ADMIN*).
