package com.example.demo;

import com.example.demo.dtos.requestDTOs.clienteDTOs.AddClienteDTO;
import com.example.demo.dtos.requestDTOs.productoDTOs.AddProductoDTO;
import com.example.demo.dtos.requestDTOs.ventaDTOs.AddVentaDTO;
import com.example.demo.models.Cliente;
import com.example.demo.models.Producto;
import com.example.demo.models.Venta;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Datos {
    public static final List<Venta> VENTAS = List.of(new Venta(1L, LocalDate.now(), 1000.0, null, null));
    public static final AddProductoDTO ADD_PRODUCTO_DTO = new AddProductoDTO("Producto 1", "Marca 1", 1000.0, 10.0);
    public static final List<Producto> PRODUCTOS_SIN_VENTAS = List.of(
            new Producto(1L, "Producto 1", "Marca 1", 100.0, 10.0, null),
            new Producto(2L, "Producto 2", "Marca 2", 200.0, 20.0, null)
    );
    public static final List<Cliente> CLIENTES_SIN_VENTAS = List.of(new Cliente(1L, "Juan", "Perez", "36158155"),
            new Cliente(2L, "Pedro", "Gomez", "36158156", null),
            new Cliente(3L, "Marcelo", "Troncho", "36158188")
    );
    public static final AddClienteDTO ADD_CLIENTE_DTO = new AddClienteDTO("Juan", "Perez", "36158155");
    public static final Cliente CLIENTE_SIN_VENTAS = new Cliente(1L, "Juan", "Perez", "36158155", null);
    public static final Cliente CLIENTE_CON_VENTAS = new Cliente(1L, "Juan", "Perez", "36158155", new ArrayList<>(VENTAS));
    public static final AddVentaDTO ADD_VENTA_DTO = new AddVentaDTO(LocalDate.now(), List.of(1L, 2L), 1L);
    public static final List<Producto> PRODUCTOS_CON_Y_SIN_VENTAS = List.of(
            new Producto(1L, "Producto 1", "Marca 1", 100.0, 10.0, List.of(
                    new Venta(1L, LocalDate.now(), 1000.0, null, null)
            )),
            new Producto(2L, "Producto 2", "Marca 2", 200.0, 20.0, null)
    );
    public static final Venta VENTA_CON_PRODUCTOS_Y_CLIENTE = new Venta(1L, LocalDate.now(), 1000.0, PRODUCTOS_CON_Y_SIN_VENTAS, CLIENTE_SIN_VENTAS);
    public static final List<Venta> VENTAS_CON_PRODUCTOS_Y_CLIENTE = List.of(
            new Venta(1L, LocalDate.now(), 1000.0, List.of(
                    new Producto(1L, "Producto 1", "Marca 1", 100.0, 10.0, null)
            ), new Cliente(1L, "Juan", "Perez", "36158155")),
            new Venta(2L, LocalDate.now(), 2000.0, List.of(
                    new Producto(2L, "Producto 2", "Marca 2", 200.0, 20.0, null)
            ), new Cliente(2L, "Pedro", "Gomez", "36158156"))
    );
}
