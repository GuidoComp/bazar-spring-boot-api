package com.example.demo.services;

import com.example.demo.datos.productos.ProductoDatos;
import com.example.demo.dtos.requestDTOs.productoDTOs.AddProductoDTO;
import com.example.demo.dtos.requestDTOs.productoDTOs.UpdateProductoDTO;
import com.example.demo.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
import com.example.demo.exceptions.NoStockException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.RestrictException;
import com.example.demo.models.Producto;
import com.example.demo.utils.ErrorMsgs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProductoServiceIntegrationTest {
    @Autowired
    IProductoService service;

    @Test
    void getProductosEmpty() {
        var productos = service.getProductos();

        assertNotNull(productos);
        assertTrue(productos.isEmpty());
    }

    @Test
    void addProducto() {
        AddProductoDTO addProductoDTO = ProductoDatos.crearAddProducto1DTO();

        ProductoResponseDTO productoResponseDTO = service.addProducto(addProductoDTO);

        assertNotNull(productoResponseDTO);
        assertNotNull(productoResponseDTO.getProductoId());
        assertEquals(addProductoDTO.getNombre(), productoResponseDTO.getNombre());
        assertEquals(addProductoDTO.getMarca(), productoResponseDTO.getMarca());
        assertEquals(addProductoDTO.getCosto(), productoResponseDTO.getCosto());
        assertEquals(addProductoDTO.getCantidadDisponible(), productoResponseDTO.getCantidadDisponible());
    }

    @Test
    void addProductoExistenteLanzaExcepcion() {
        AddProductoDTO addProductoDTO = ProductoDatos.crearAddProducto1DTO();
        service.addProducto(addProductoDTO);

        RestrictException restrictException = assertThrows(RestrictException.class, () -> service.addProducto(addProductoDTO));
        assertEquals(restrictException.getMessage(), ErrorMsgs.PRODUCTO_YA_INGRESADO);
    }

    @Test
    void deleteProducto() {
        AddProductoDTO addProductoDTO = ProductoDatos.crearAddProducto1DTO();
        ProductoResponseDTO creado = service.addProducto(addProductoDTO);

        ProductoResponseDTO eliminado = service.deleteProducto(creado.getProductoId());

        assertNotNull(eliminado);
        assertEquals(creado.getProductoId(), eliminado.getProductoId());
        assertEquals(addProductoDTO.getNombre(), eliminado.getNombre());
        assertEquals(addProductoDTO.getMarca(), eliminado.getMarca());
        assertFalse(service.productoExistente(addProductoDTO.getNombre(), addProductoDTO.getMarca()));
    }

    @Test
    void deleteProductoInexistenteLanzaExcepcion() {
        assertThrows(ResourceNotFoundException.class, () -> service.deleteProducto(9999L), ErrorMsgs.PRODUCTO_NOT_FOUND + " con el id 9999");
    }

    @Test
    void updateProducto() {
        AddProductoDTO addProductoDto = ProductoDatos.crearAddProducto1DTO();
        ProductoResponseDTO creado = service.addProducto(addProductoDto);

        ProductoResponseDTO productoResponseDTO = service.updateProducto(
                creado.getProductoId(),
                new UpdateProductoDTO("Nuevo nombre", "Nueva marca", null, null)
        );

        assertNotNull(productoResponseDTO);
        assertEquals(creado.getProductoId(), productoResponseDTO.getProductoId());
        assertEquals("Nuevo nombre", productoResponseDTO.getNombre());
        assertEquals("Nueva marca", productoResponseDTO.getMarca());
        assertEquals(addProductoDto.getCosto(), productoResponseDTO.getCosto());
        assertEquals(addProductoDto.getCantidadDisponible(), productoResponseDTO.getCantidadDisponible());
    }

    @Test
    void getProductosByIds() {
        ProductoResponseDTO p1 = service.addProducto(new AddProductoDTO("Procesador", "Intel Core i5 14600KF", 400000.0, 10.0));
        ProductoResponseDTO p2 = service.addProducto(new AddProductoDTO("Monitor 27", "Samsung", 100.0, 10.0));

        List<Producto> productos = service.getProductosByIds(List.of(p1.getProductoId(), p2.getProductoId()));

        assertNotNull(productos);
        assertEquals(2, productos.size());
        assertTrue(productos.stream().anyMatch(p -> p.getProductoId().equals(p1.getProductoId()) && "Procesador".equals(p.getNombre())));
        assertTrue(productos.stream().anyMatch(p -> p.getProductoId().equals(p2.getProductoId()) && "Monitor 27".equals(p.getNombre())));
    }

    @Test
    void getProductosConStockBajo() {
        service.addProducto(new AddProductoDTO("Procesador", "Intel Core i5 14600KF", 400000.0, 10.0));
        service.addProducto(new AddProductoDTO("Monitor 22", "Samsung", 100.0, 3.0));

        List<ProductoResponseDTO> productosConStockBajo = service.getProductosConStockBajo();

        assertNotNull(productosConStockBajo);
        assertEquals(1, productosConStockBajo.size());
        assertEquals("Monitor 22", productosConStockBajo.get(0).getNombre());
    }

    @Test
    void productoExistente() {
        service.addProducto(new AddProductoDTO("Monitor 22", "Samsung", 100.0, 3.0));

        boolean productoExistente = service.productoExistente("Monitor 22", "Samsung");

        assertTrue(productoExistente);
    }

    @Test
    void productoNoExistente() {
        boolean productoExistente = service.productoExistente("SubLow", "Meyer Sound");
        assertFalse(productoExistente);
    }

    @Test
    void checkStock() {
        assertThrows(NoStockException.class, () -> service.checkStock(new Producto(1L, "Monitor 22", "Samsung", 100.0, 0.0)));
    }
}
