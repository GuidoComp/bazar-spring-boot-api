INSERT INTO Clientes (cliente_id, nombre, apellido, dni)
VALUES (1, 'Juan', 'Perez', '36158155');

INSERT INTO Clientes (cliente_id, nombre, apellido, dni)
VALUES (2, 'Guido', 'Compagno', '25159159');

INSERT INTO Productos (producto_id, nombre, marca, costo, cantidad_disponible)
VALUES (1, 'Procesador', 'Intel Core i5 14600KF', 400000, 10);

INSERT INTO Productos (producto_id, nombre, marca, costo, cantidad_disponible)
VALUES (2, 'Placa de video', 'NVidia RTX 3090', 600000, 3);

INSERT INTO Productos (producto_id, nombre, marca, costo, cantidad_disponible)
VALUES (3, 'Motherboard', 'MSI Tomahawk Max', 420000, 5);

INSERT INTO Productos (producto_id, nombre, marca, costo, cantidad_disponible)
VALUES (4, 'Memoria RAM', 'XPG Lancer 32 Gb', 250000, 5);

INSERT INTO Ventas (venta_id, fecha_venta, total, cliente_id)
VALUES (1, '2021-01-01', 1000000, 1);

INSERT INTO Ventas (venta_id, fecha_venta, total, cliente_id)
VALUES (2, '2022-02-02', 400000, 1);

INSERT INTO Ventas (venta_id, fecha_venta, total, cliente_id)
VALUES (3, '2021-01-01', 1670000, 2);

INSERT INTO productos_ventas (producto_id, venta_id)
VALUES (1, 1);

INSERT INTO productos_ventas (producto_id, venta_id)
VALUES (2, 1);

INSERT INTO productos_ventas (producto_id, venta_id)
VALUES (1, 2);

INSERT INTO productos_ventas (producto_id, venta_id)
VALUES (1, 3);

INSERT INTO productos_ventas (producto_id, venta_id)
VALUES (2, 3);

INSERT INTO productos_ventas (producto_id, venta_id)
VALUES (3, 3);

INSERT INTO productos_ventas (producto_id, venta_id)
VALUES (4, 3);