package com.example.demo.datos.productos;

import com.example.demo.dtos.requestDTOs.productoDTOs.AddProductoDTO;
import com.example.demo.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
import com.example.demo.models.Producto;
import com.example.demo.models.Venta;

import java.time.LocalDate;
import java.util.List;

public class ProductoDatos {
    public static Producto crearProducto() {
        return new Producto(1L, "Procesador", "Intel Core i5 14600KF", 400000.0, 10.0);
    }

    public static List<Producto> crearProductos() {
        return List.of(
                new Producto(1L, "Procesador", "Intel Core i5 14600KF", 400000.0, 10.0),
                new Producto(2L, "Placa de video", "Nvidia RTX 3060", 500000.0, 10.0),
                new Producto(3L, "Placa de video", "Nvidia RTX 3070", 600000.0, 10.0),
                new Producto(4L, "Placa de video", "Nvidia RTX 3080", 700000.0, 10.0),
                new Producto(5L, "Placa de video", "Nvidia RTX 3090", 800000.0, 10.0)
        );
    }

    public static List<ProductoResponseDTO> crearProductosResponseDTO() {
        return List.of(
                new ProductoResponseDTO(1L, "Procesador", "Intel Core i5 14600KF", 400000.0, 10.0),
                new ProductoResponseDTO(2L, "Placa de video", "Nvidia RTX 3060", 500000.0, 10.0),
                new ProductoResponseDTO(3L, "Placa de video", "Nvidia RTX 3070", 600000.0, 10.0),
                new ProductoResponseDTO(4L, "Placa de video", "Nvidia RTX 3080", 700000.0, 10.0),
                new ProductoResponseDTO(5L, "Placa de video", "Nvidia RTX 3090", 800000.0, 10.0)
        );
    }

    public static ProductoResponseDTO crearProducto1ResponseDTO() {
        return new ProductoResponseDTO(1L, "Producto 1", "Marca 1", 100.0, 10.0);
    }

    public static List<ProductoResponseDTO> crearProductos1ResponseDTO() {
        return List.of(
                new ProductoResponseDTO(1L, "Producto 1", "Marca 1", 100.0, 10.0),
                new ProductoResponseDTO(2L, "Producto 2", "Marca 2", 200.0, 20.0)
        );
    }

    public static ProductoResponseDTO crearProductoResponseDTO() {
        return new ProductoResponseDTO(1L, "Procesador", "Intel Core i5 14600KF", 400000.0, 10.0);
    }

    public static AddProductoDTO crearAddProducto1DTO() {
        return new AddProductoDTO("Procesador", "Intel Core i5 14600KF", 400000.0, 10.0);
    }

    public static AddProductoDTO crearAddProducto2DTO() {
        return new AddProductoDTO("Placa de video", "NVidia RTX 3090", 600000.0, 3.0);
    }

    public static List<Producto> crearProductosSinVentas() {
        return List.of(
                new Producto(1L, "Producto 1", "Marca 1", 100.0, 10.0, null),
                new Producto(2L, "Producto 2", "Marca 2", 200.0, 20.0, null)
        );
    }

    public static List<Producto> crearProductosConYSinVentas() {
        return List.of(
                new Producto(1L, "Producto 1", "Marca 1", 100.0, 10.0, List.of(
                        new Venta(1L, LocalDate.now(), 1000.0, null, null)
                )),
                new Producto(2L, "Producto 2", "Marca 2", 200.0, 20.0, null)
        );
    }

    public static List<Producto> crearProductosConStockBajo() {
        return List.of(
                new Producto(1L, "Producto 1", "Marca 1", 100.0, 10.0, null),
                new Producto(2L, "Producto 2", "Marca 2", 200.0, 20.0, null),
                new Producto(3L, "Producto 3", "Marca 3", 300.0, 4.0, null),
                new Producto(4L, "Producto 4", "Marca 4", 400.0, 1.0, null)
        );
    }

    public static List<ProductoResponseDTO> crearProductosResponseDTOConStockBajo() {
        return List.of(
                new ProductoResponseDTO(3L, "Producto 3", "Marca 3", 300.0, 4.0),
                new ProductoResponseDTO(4L, "Producto 4", "Marca 4", 400.0, 1.0)
        );
    }


}
