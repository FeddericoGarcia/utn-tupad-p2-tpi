# 🧾 Trabajo Final Integrador – Programación II 
### Tecnicatura Universitaria en Programación a Distancia
**Universidad Tecnológica Nacional (UTN)**  


**Materia:** Programación II  
**Carrera:** Tecnicatura Universitaria en Programación a Distancia  
**Modalidad:**  Virtual  
**Comisión:** M2025  

**Trabajo:** Sistema de Gestión de Productos con Código de Barras  

**Docente:** [Nombre del Profesor/a o Cátedra]  
**Tutor:** [Nombre del Tutor/a]  
**Año:** 2025  

---

### 👨‍💻 Integrantes del Equipo

| Alumno | Legajo | 
|---------|---------|
| Federico E. García Bengolea | 101613 |
| Alan J | 4846 | 
| Jazmin Herrera | [Completar] | 
| Fernando  | [Completar] | 

---

## 🧠 Descripción del Proyecto

El presente trabajo práctico integrador consiste en el desarrollo de una aplicación **de consola en Java** que implemente una **relación 1 a 1 entre las entidades “Producto” y “Código de Barras”**, aplicando los conceptos de **POO, JDBC, manejo de excepciones y transacciones SQL**.

La aplicación permite realizar las operaciones CRUD completas (alta, baja, modificación y consulta) sobre ambas entidades, incluyendo una **operación transaccional compuesta** que permite **crear un producto junto con su código de barras en la misma transacción**.

El sistema se conecta a una base de datos **MySQL** gestionada desde **phpMyAdmin (XAMPP)**, y utiliza **JDBC** para realizar las operaciones de persistencia.  



## 🧩 Funcionalidades Principales

### Entidad A: Producto
- Crear producto y su código de barras asociado (transaccional).  
- Listar todos los productos.  
- Buscar producto por ID.  
- Actualizar datos del producto.  
- Eliminar producto de forma lógica.

### Entidad B: Código de Barras
- Crear código de barras (opcionalmente sin producto asociado).  
- Listar todos los códigos.  
- Buscar código por ID.  
- Actualizar datos del código.  
- Eliminar código de forma lógica.


## ⚙️ Estructura del Proyecto

src/  
com/tpi/tfi/  
├ config/  
│ └─ DatabaseConnection.java  
├ dao/  
│ ├─ GenericDAO.java  
│ ├─ ProductoDAO.java  
│ └─ CodigoBarrasDAO.java  
├ entities/  
│ ├─ Producto.java  
│ └─ CodigoBarras.java  
├ service/  
│ ├─ ProductoService.java  
│ └─ CodigoBarrasService.java  
└ main/  
....└─ AppMenu.java

---

## 🧰 Tecnologías Utilizadas

| Componente | Tecnología |
|-------------|-------------|
| Lenguaje | Java 21 |
| IDE | Apache NetBeans |
| Conexión BD | JDBC |
| Base de Datos | MySQL (XAMPP / phpMyAdmin) |
| Control de Versiones | GitHub |
| Patrón de diseño | DAO + Service Layer |
| Paradigma | POO |

---

## 🧾 Esquema de Base de Datos

**Base:** `tpi_programacion2`  
Contiene dos tablas relacionadas 1→1:  
- `producto`  
- `codigo_barras`  

La relación se establece mediante la clave foránea `codigo_barras.producto_id` (con restricción UNIQUE).

---

## ▶️ Ejecución

1. Crear la base de datos ejecutando el script:  
   - `tfi_programacion2_schema.sql`  
   - (opcional) `tfi_programacion2_sample_data.sql`

2. Configurar conexión en el archivo:  
   `com/tpi/tfi/config/DatabaseConnection.java`

3. Ejecutar la clase principal:  
   `com.tpi.tfi.main.AppMenu`

4. Navegar por el menú principal desde la consola.




## 🧾 Anexo de Inteligencia Artificial
Todo el proceso de generación, estructura del código, y documentación fue asistido por la herramienta **ChatGPT (GPT-5)**.   
En el **Anexo de IA** se detallan los *prompts* utilizados y las iteraciones relevantes para la construcción del proyecto.


