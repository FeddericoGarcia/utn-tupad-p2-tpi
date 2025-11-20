# 📘 *Trabajo Práctico Integrador – Programación II*  
### *Universidad Tecnológica Nacional - UTN*   
*Sistema de Gestión de Productos y Código de Barras*

---

## 👥 *Integrantes del Trabajo*

- Federico E. Garcia Bengolea - Legajo 17968 
- Alan N. Jofre - Legajo 4846  
- Akier Aguirrezabala - Legajo 611581
- Jazmín Herrera - DNI 47864024

---

## 📑 *Índice General*

1. Introducción  
2. Objetivo General del Proyecto  
3. Descripción Funcional del Sistema  
4. Arquitectura del Proyecto  
5. Tecnologías Utilizadas  
6. Dependencias y Configuración Inicial  
7. Estructura de Carpetas del Proyecto  
8. Modelo de Datos y Estructura de Tablas  
9. Validaciones y Manejo de Excepciones  
10. Funcionamiento del Menú Principal  
11. Guía de Instalación (Windows + NetBeans + XAMPP/MySQL)  
12. Configuración del Driver JDBC  
13. Ejecución del Proyecto  
14. Errores Comunes y Soluciones  
15. Casos de Uso  
16. Flujo Transaccional del Alta de Producto + Código  
17. Mejoras Futuras Propuestas  
18. Conclusión  

---

## 1. 🧭 *Introducción*

El presente documento describe el desarrollo del Trabajo Práctico Integrador correspondiente a la materia *Programación II*, cuyo objetivo es implementar un sistema de gestión orientado a productos y sus códigos de barras asociados.

El proyecto fue desarrollado íntegramente en *Java 21, siguiendo buenas prácticas de programación, modularización, manejo de excepciones y acceso seguro a datos mediante **JDBC. Su persistencia se implementó sobre **MySQL, utilizando **XAMPP* como servidor local.

---

## 2. 🎯 *Objetivo General del Proyecto*

El objetivo final es desarrollar un sistema de consola capaz de:

- Gestionar productos  
- Asignar un código de barras EAN-13 a cada producto (relación 1 a 1)  
- Ejecutar operaciones CRUD completas  
- Validar datos de entrada  
- Utilizar transacciones  
- Manejar excepciones de manera controlada  

---

## 3. 📦 *Descripción Funcional del Sistema*

### *Gestión de Productos*

- Crear productos  
- Asignar un código de barras al momento del alta  
- Listar productos con su información  
- Actualizar nombre o precio  
- Eliminar producto  

### *Gestión de Códigos de Barras*

- Listar códigos  
- Buscar un código específico  
- Eliminar un código  
- Verificar duplicados  
- Validación estricta del formato *EAN-13*

---

## 4. 🏛️ *Arquitectura del Proyecto*

El proyecto se estructura bajo una arquitectura de *capas*:


Presentación (AppMenu / Main)
      ↓
Servicio (ProductoService, CodigoBarrasService)
      ↓
Acceso a Datos (DAO)
      ↓
Conexión a BD (DatabaseConnection)
      ↓
Base de Datos (MySQL)


---

## 5. 🛠️ *Tecnologías Utilizadas*

| Tecnología | Versión | Descripción |
|-----------|---------|-------------|
| *Java* | 21 | Lógica del sistema |
| *NetBeans* | 20 | IDE principal |
| *MySQL (XAMPP)* | 8.x | Motor de base de datos |
| *mysql-connector-j* | 8.4.0 | Driver JDBC |
| *CLI/Consola* | — | Interfaz de usuario |

---

## 6. 🔧 *Dependencias y Configuración Inicial*

Para ejecutar el proyecto correctamente se requiere:

- Java 21 instalado  
- MySQL activo vía XAMPP  
- Base de datos creada: *tpi_db*  
- Driver JDBC agregado al proyecto  
- NetBeans 20 o superior  

---

## 7. 📁 *Estructura de Carpetas del Proyecto*


/src
 ├── config
 │     └── DatabaseConnection.java
 ├── dao
 │     ├── ProductoDAO.java
 │     └── CodigoBarrasDAO.java
 ├── entities
 │     ├── Producto.java
 │     └── CodigoBarras.java
 ├── exceptions
 │     └── DataAccessException.java
 ├── service
 │     ├── ProductoService.java
 │     └── CodigoBarrasService.java
 ├── utils
 │     └── Validador.java
 └── main
       ├── AppMenu.java
       └── Main.java


---

## 8. 🗄️ *Modelo de Datos y Estructura de Tablas*

### *Tabla: producto*

sql
CREATE TABLE producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10,2) NOT NULL
);


### *Tabla: codigo_barras*

sql
CREATE TABLE codigo_barras (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(13) NOT NULL UNIQUE,
    producto_id INT UNIQUE,
    FOREIGN KEY (producto_id) REFERENCES producto(id) ON DELETE CASCADE
);


---

## 9. 🛡️ *Validaciones y Manejo de Excepciones*

### ✔ Validaciones implementadas

- Nombre no vacío → se convierte a *UPPERCASE*  
- Precio numérico → mayor que cero  
- Código EAN-13 → exactamente 13 dígitos  
- Verificación de unicidad  
- Manejo de errores por entrada inválida  

### ✔ Excepciones manejadas

- SQLException  
- NumberFormatException  
- IllegalArgumentException  
- DataAccessException personalizada  
- Errores de conexión  
- Fallos transaccionales con rollback automático  

---

## 10. 📋 *Funcionamiento del Menú Principal*

### *Menú Principal*


===============================
        MENU PRINCIPAL
===============================
1. Gestión de Productos
2. Gestión de Códigos de Barras
0. Salir


### *Menú de Productos*


1. Crear Producto con Código
2. Listar Productos
3. Actualizar Producto
4. Eliminar Producto
0. Volver


### *Menú de Códigos*


1. Listar Códigos
2. Buscar Código
3. Eliminar Código
0. Volver


---

## 11. 💻 *Guía de Instalación (Windows)*

1. Instalar *XAMPP*  
2. Iniciar módulo *MySQL*  
3. Crear la base tpi_db  
4. Instalar *Java 21*  
5. Abrir el proyecto en *NetBeans 20*  
6. Descargar mysql-connector-j-8.4.0.jar  
7. Agregar al proyecto:  
   Right-click Project → Properties → Libraries → Add JAR  
8. Ejecutar Main.java  

---

## 12. 🔌 *Configuración del Driver JDBC*

URL utilizada en la conexión:


jdbc:mysql://localhost:3306/tpi_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC


- Usuario: root  
- Contraseña: (vacía)

---

## 13. ▶ *Ejecución del Proyecto*

En NetBeans:


Run → Run Project


O directamente desde la terminal del IDE:


java Main


---

## 14. ❗ *Errores Comunes y Soluciones*

| Error | Causa | Solución |
|------|--------|----------|
| Unknown database | La base no existe | Crear tpi_db |
| No suitable driver | Falta el JAR JDBC | Agregar mysql-connector |
| Access denied | MySQL tiene contraseña | Configurarla en DatabaseConnection |
| Duplicate entry | Código repetido | Validar código antes del insert |
| Cannot connect | XAMPP detenido | Activar MySQL |

---

## 15. 📘 *Casos de Uso*

- Alta de producto + código de barras  
- Búsqueda por código  
- Listado de todos los productos  
- Actualización por ID  
- Eliminación física controlada  

---

## 16. 🔄 *Flujo Transaccional del Alta*

1. Iniciar transacción manual  
2. Insertar producto  
3. Insertar código asociado  
4. Validar duplicados  
5. Commit si todo es correcto  
6. Rollback si ocurre cualquier error  

---

## 17. 🚀 *Mejoras Futuras Propuestas*

- Implementar baja lógica  
- Crear interfaz gráfica (JavaFX o Swing)  
- Exportación CSV/Excel  
- Implementar categorías o stock  
- API REST con Spring Boot  

---

## 18. 🏁 *Conclusión*

El proyecto implementa de forma correcta:

- arquitectura en capas  
- validaciones robustas  
- código modular  
- transacciones  
- excepciones personalizadas  
- persistencia JDBC  
- buenas prácticas de desarrollo  

Su estructura sólida permite una fácil extensión y mantenimiento futuro.

---
