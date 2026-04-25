package com.gcompagno.ecommerce.datos.ventas;

import com.gcompagno.ecommerce.datos.clientes.ClienteDatos;
import com.gcompagno.ecommerce.datos.productos.ProductoDatos;
import com.gcompagno.ecommerce.dtos.requestDTOs.ventaDTOs.AddVentaDTO;
import com.gcompagno.ecommerce.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.gcompagno.ecommerce.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
import com.gcompagno.ecommerce.dtos.responseDTOs.ventaDTOs.VentaResponseDTO;
import com.gcompagno.ecommerce.models.Cliente;
import com.gcompagno.ecommerce.models.Producto;
import com.gcompagno.ecommerce.models.Venta;

import java.time.LocalDate;
import java.util.List;

public class VentaDatos {
    public static List<Venta> crearVentaSinProductosNiClientes() {
        return List.of(new Venta(1L, LocalDate.now(), 1000.0, null, null));
    }

    public static AddVentaDTO crearAddVenta1DTO() {
        return new AddVentaDTO(LocalDate.now(), List.of(1L, 2L), 1L);
    }

    public static VentaResponseDTO crearVenta1ResponseDTO() {
        return new VentaResponseDTO(1L, LocalDate.now(), 300.0, ProductoDatos.crearProductos1ResponseDTO(), ClienteDatos.crearClienteResponseDTO());
    }

    public static VentaResponseDTO crearVentaResponseDTOSinProductos() {
        return new VentaResponseDTO(1L, LocalDate.now(), 1000.0, null, ClienteDatos.crearClienteResponseDTO());
    }

    public static Venta crearVenta1ConProductosYCliente() {
        return new Venta(1L, LocalDate.now(), 300.0, ProductoDatos.crearProductosConYSinVentas(), ClienteDatos.crearClienteSinVentas());
    }

    public static List<Venta> crearVentasConProductosYClientes() {
        return List.of(
                new Venta(1L, LocalDate.now(), 1000.0, List.of(
                        new Producto(1L, "Producto 1", "Marca 1", 100.0, 10.0, null)
                ), new Cliente(1L, "Juan", "Perez", "36158155")),
                new Venta(2L, LocalDate.now(), 2000.0, List.of(
                        new Producto(2L, "Producto 2", "Marca 2", 200.0, 20.0, null)
                ), new Cliente(2L, "Pedro", "Gomez", "36158156"))
        );
    }

    public static List<VentaResponseDTO> crearVentasResponseDTOConProductosYClientes() {
        return List.of(
                new VentaResponseDTO(1L, LocalDate.now(), 1000.0, List.of(
                        new ProductoResponseDTO(1L, "Producto 1", "Marca 1", 100.0, 10.0)
                ), new ClienteResponseDTO(1L, "Juan", "Perez", "36158155")),
                new VentaResponseDTO(2L, LocalDate.now(), 2000.0, List.of(
                        new ProductoResponseDTO(2L, "Producto 2", "Marca 2", 200.0, 20.0)
                ), new ClienteResponseDTO(2L, "Pedro", "Gomez", "36158156"))
        );
    }
}
