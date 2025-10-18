USE tpi_programacion2;

INSERT INTO producto (nombre, marca, categoria, precio, peso) VALUES
('Yerba Mate', 'Taragui', 'Alimentos', 799.99, 0.5),
('Shampoo', 'Head&Shoulders', 'Higiene', 1299.50, NULL);

INSERT INTO codigo_barras (tipo, valor, fecha_asignacion, observaciones, producto_id) VALUES
('EAN13','7790000000001', CURDATE(), 'Asignado inicialmente', 1),
('EAN13','7790000000002', CURDATE(), 'Asignado inicialmente', 2);
