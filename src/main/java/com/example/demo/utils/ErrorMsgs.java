package com.example.demo.utils;

public class ErrorMsgs {
    public static final String PRODUCTO_NOT_FOUND = "Producto no encontrado";
    public static final String VENTA_NOT_FOUND = "Venta no encontrada con el id %s";
    public static final String CLIENTE_NOT_FOUND = "Cliente no encontrado con el id %s";
    public static final String NOMBRE_PRODUCTO_REQUIRED = "El nombre del producto es requerido";
    public static final String NOMBRE_PRODUCTO_LENGTH = "El nombre del producto debe tener entre 3 y 200 caracteres";
    public static final String MARCA_LENGTH = "La marca del producto debe tener entre 3 y 200 caracteres";
    public static final String MARCA_REQUIRED = "La marca del producto es requerida";
    public static final String COSTO_REQUIRED = "El costo del producto es requerido";
    public static final String COSTO_NEGATIVO = "El costo del producto no puede ser 0 o negativo";
    public static final String CANTIDAD_NEGATIVA = "La cantidad disponible del producto no puede ser negativa";
    public static final String CANTIDAD_REQUIRED = "La cantidad disponible del producto es requerida";
    public static final String NOMBRE_CLIENTE_REQUERIDO = "El nombre del cliente es requerido";
    public static final String NOMBRE_CLIENTE_NO_VACIO = "El nombre del cliente no puede ser vacío";
    public static final String APELLIDO_CLIENTE_REQUERIDO = "El apellido del cliente es requerido";
    public static final String APELLIDO_CLIENTE_NO_VACIO = "El apellido del cliente no puede ser vacío";
    public static final String APELLIDO_CLIENTE_MIN_LENGTH = "El apellido está incompleto";
    public static final String NOMBRE_CLIENTE_MIN_LENGTH = "El apellido está incompleto";
    public static final String DNI_CLIENTE_REQUERIDO = "El dni es requerido";
    public static final String DNI_CLIENTE_NO_VACIO = "El dni no puede ser vacío";
    public static final String DNI_CLIENTE_NEGATIVO = "El dni erróneo";
    public static final String DNI_LENGTH = "El dni debe tener 8 caracteres";
    public static final String VENTA_FECHA_VENTA_NOT_NULL = "La fecha de venta no puede ser nula";
    public static final String VENTA_TOTAL_NOT_NULL = "Debe especificarse el monto total de la venta";
    public static final String VENTA_TOTAL_POSITIVE = "El monto total de la venta no puede ser negativo";
    public static final String VENTA_PRODUCTOS_NOT_NULL = "La venta debe tener al menos 1 producto";
    public static final String VENTA_CLIENTE_NOT_NULL = "La venta debe tener 1 cliente";
    public static final String PRODUCTO_SIN_STOCK = "No hay stock disponible para el producto: %s - %s - %s";
}
