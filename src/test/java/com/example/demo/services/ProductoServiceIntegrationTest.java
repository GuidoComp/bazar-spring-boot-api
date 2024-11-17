package com.example.demo.services;

import com.example.demo.Datos;
import com.example.demo.dtos.requestDTOs.productoDTOs.AddProductoDTO;
import com.example.demo.dtos.requestDTOs.productoDTOs.UpdateProductoDTO;
import com.example.demo.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
import com.example.demo.exceptions.NoStockException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.RestrictException;
import com.example.demo.models.Producto;
import com.example.demo.utils.ErrorMsgs;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductoServiceIntegrationTest {
    @Autowired
    IProductoService service;

    @Test
    @Order(1)
    void getProductosEmpty() {
        var productos = service.getProductos();

        assertNotNull(productos);
        assertTrue(productos.isEmpty());
    }

    @Test
    @Order(2)
    void addProducto() {
        AddProductoDTO addProductoDTO = Datos.ADD_PRODUCTO_DTO;

        var productoResponseDTO = service.addProducto(addProductoDTO);

        assertNotNull(productoResponseDTO);
        assertEquals(addProductoDTO.getNombre(), productoResponseDTO.getNombre());
        assertEquals(addProductoDTO.getMarca(), productoResponseDTO.getMarca());
        assertEquals(1L, productoResponseDTO.getProductoId());
    }

    @Test
    @Order(3)
    void addProductoExistenteLanzaExcepcion() {
        AddProductoDTO addProductoDTO = Datos.ADD_PRODUCTO_DTO;

        assertThrows(RestrictException.class, () -> service.addProducto(addProductoDTO), ErrorMsgs.PRODUCTO_YA_INGRESADO);
    }

    @Test
    @Order(4)
    void deleteProducto() {
        ProductoResponseDTO productoResponseDTO = service.deleteProducto(1L);
        assertNotNull(productoResponseDTO);
        assertEquals(1L, productoResponseDTO.getProductoId());
        assertEquals(Datos.ADD_PRODUCTO_DTO.getNombre(), productoResponseDTO.getNombre());
        assertEquals(Datos.ADD_PRODUCTO_DTO.getMarca(), productoResponseDTO.getMarca());
        assertFalse(service.productoExistente(Datos.ADD_PRODUCTO_DTO.getNombre(), Datos.ADD_PRODUCTO_DTO.getMarca()));
    }

    @Test
    @Order(5)
    void deleteProductoInexistenteLanzaExcepcion() {
        assertThrows(ResourceNotFoundException.class, () -> service.deleteProducto(1L), ErrorMsgs.PRODUCTO_NOT_FOUND + " con el id 1");
    }

//    @Test
//    @Order(6)
//    void deleteProductoConVentasLanzaExcepcion() {
//        AddProductoDTO addProductoDTO = Datos.ADD_PRODUCTO_DTO;
//
//        service.addProducto(addProductoDTO);
//
//        assertThrows(RestrictException.class, () -> service.deleteProducto(2L), ErrorMsgs.DELETE_PRODUCTO_RESTRICCION_FK);
//    }


    @Test
    @Order(7)
    void updateProducto() {
        AddProductoDTO addProductoDto = Datos.ADD_PRODUCTO_DTO;
        service.addProducto(addProductoDto);
        ProductoResponseDTO productoResponseDTO = service.updateProducto(2L, new UpdateProductoDTO("Nuevo nombre", "Nueva marca", null, null));

        assertNotNull(productoResponseDTO);
        assertEquals(2L, productoResponseDTO.getProductoId());
        assertEquals("Nuevo nombre", productoResponseDTO.getNombre());
        assertEquals("Nueva marca", productoResponseDTO.getMarca());
        assertEquals(addProductoDto.getCosto(), productoResponseDTO.getCosto());
        assertEquals(addProductoDto.getCantidadDisponible(), productoResponseDTO.getCantidadDisponible());
    }

    @Test
    @Order(8)
    void getProductosByIds() {
        service.addProducto(new AddProductoDTO("Monitor 27", "Samsung", 100.0, 10.0));
        List<Producto> productos = service.getProductosByIds(List.of(1L, 2L, 5L, 3L));

        assertNotNull(productos);
        assertEquals(2, productos.size());
        assertEquals(2L, productos.get(0).getProductoId());
        assertEquals("Nuevo nombre", productos.get(0).getNombre());
        assertEquals("Monitor 27", productos.get(1).getNombre());
    }

    @Test
    @Order(9)
    void getProductosConStockBajo() {
        service.addProducto(new AddProductoDTO("Monitor 22", "Samsung", 100.0, 3.0));
        List<ProductoResponseDTO> productosConStockBajo = service.getProductosConStockBajo();

        assertNotNull(productosConStockBajo);
        assertEquals(1, productosConStockBajo.size());
        assertEquals("Monitor 22", productosConStockBajo.get(0).getNombre());
    }

    @Test
    @Order(10)
    void productoExistente() {
        boolean productoExistente = service.productoExistente("Monitor 22", "Samsung");
        assertTrue(productoExistente);
    }

    @Test
    @Order(11)
    void productoNoExistente() {
        boolean productoExistente = service.productoExistente("SubLow", "Meyer Sound");
        assertFalse(productoExistente);
    }

    @Test
    @Order(12)
    void checkStock() {
        assertThrows(NoStockException.class, () -> service.checkStock(new Producto(1L, "Monitor 22", "Samsung", 100.0, 0.0)));
    }
}