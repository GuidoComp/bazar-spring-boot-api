-- Initial schema. Refleja lo que Hibernate generaría a partir de las
-- @Entity actuales (Cliente, Producto, Venta, User) + naming strategy
-- SpringPhysicalNamingStrategy default + MySQL8Dialect.

CREATE TABLE Clientes (
    cliente_id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(255),
    apellido VARCHAR(255),
    dni VARCHAR(255),
    PRIMARY KEY (cliente_id),
    CONSTRAINT uk_clientes_dni UNIQUE (dni)
) ENGINE=InnoDB;

CREATE TABLE Productos (
    producto_id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(200) NOT NULL,
    marca VARCHAR(200) NOT NULL,
    costo DOUBLE NOT NULL,
    cantidad_disponible DOUBLE NOT NULL,
    PRIMARY KEY (producto_id),
    CONSTRAINT uk_productos_nombre_marca UNIQUE (nombre, marca)
) ENGINE=InnoDB;

CREATE TABLE Ventas (
    venta_id BIGINT NOT NULL AUTO_INCREMENT,
    fecha_venta DATE,
    total DOUBLE,
    cliente_id BIGINT,
    PRIMARY KEY (venta_id),
    CONSTRAINT fk_ventas_cliente FOREIGN KEY (cliente_id) REFERENCES Clientes(cliente_id)
) ENGINE=InnoDB;

CREATE TABLE productos_ventas (
    venta_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    PRIMARY KEY (venta_id, producto_id),
    CONSTRAINT fk_prodventas_venta FOREIGN KEY (venta_id) REFERENCES Ventas(venta_id),
    CONSTRAINT fk_prodventas_producto FOREIGN KEY (producto_id) REFERENCES Productos(producto_id)
) ENGINE=InnoDB;

CREATE TABLE users (
    id INTEGER NOT NULL AUTO_INCREMENT,
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    role VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE=InnoDB;
