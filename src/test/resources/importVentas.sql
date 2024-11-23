INSERT INTO Ventas (venta_id, fecha_venta, total, cliente_id)
VALUES (1, '2021-01-01', 1000000, 1);

INSERT INTO Ventas (venta_id, fecha_venta, total, cliente_id)
VALUES (2, '2022-02-02', 400000, 1);

INSERT INTO productos_ventas (producto_id, venta_id)
VALUES (1, 1);

INSERT INTO productos_ventas (producto_id, venta_id)
VALUES (2, 1);

INSERT INTO productos_ventas (producto_id, venta_id)
VALUES (1, 2);