package com.example.demo.utils;

import com.example.demo.dtos.requestDTOs.productoDTOs.AddProductoDTO;
import com.example.demo.models.Cliente;
import com.example.demo.models.Producto;
import com.example.demo.models.Venta;

import java.time.LocalDate;
import java.util.List;

public class Datos {
    public static final List<Venta> VENTAS = List.of(new Venta(1L, LocalDate.now(), 1000.0, null, null));
    public static final AddProductoDTO ADD_PRODUCTO_DTO = new AddProductoDTO("Producto 1", "Marca 1", 1000.0, 10.0);
    public static final List<Producto> PRODUCTOS_SIN_VENTAS = List.of(
            new Producto(1L, "Producto 1", "Marca 1", 100.0, 10.0, null),
            new Producto(2L, "Producto 2", "Marca 2", 200.0, 20.0, null)
    );
    public static final List<Cliente> CLIENTES_SIN_VENTAS = List.of(new Cliente(1L, "Juan", "Perez", "36158155", null),
            new Cliente(2L, "Pedro", "Gomez", "36158156", null)
    );
}
