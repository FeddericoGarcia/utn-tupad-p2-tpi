-- Crear BD
CREATE DATABASE IF NOT EXISTS tpi_programacion2 CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE tpi_programacion2;

-- Tabla producto (A)
CREATE TABLE IF NOT EXISTS producto (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  eliminado BOOLEAN DEFAULT FALSE,
  nombre VARCHAR(120) NOT NULL,
  marca VARCHAR(80),
  categoria VARCHAR(80),
  precio DOUBLE(10,2) NOT NULL,
  peso DOUBLE(10,3)
) ENGINE=InnoDB;

-- Tabla codigo_barras (B) con FK única a producto
CREATE TABLE IF NOT EXISTS codigo_barras (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  eliminado BOOLEAN DEFAULT FALSE,
  tipo ENUM('EAN13','EAN8','UPC') NOT NULL,
  valor VARCHAR(20) NOT NULL UNIQUE,
  fecha_asignacion DATE,
  observaciones VARCHAR(255),
  producto_id BIGINT UNIQUE,
  CONSTRAINT fk_cb_producto FOREIGN KEY (producto_id) REFERENCES producto(id) ON DELETE CASCADE
) ENGINE=InnoDB;
