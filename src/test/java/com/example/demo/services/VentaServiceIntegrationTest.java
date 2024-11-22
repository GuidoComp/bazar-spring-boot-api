package com.example.demo.services;

import com.example.demo.datos.ventas.VentaDatos;
import com.example.demo.dtos.requestDTOs.ventaDTOs.AddVentaDTO;
import com.example.demo.dtos.responseDTOs.ventaDTOs.VentaResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.utils.ErrorMsgs;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class VentaServiceIntegrationTest {
    @Autowired
    IVentaService ventaService;

    @Autowired
    IProductoService productoService;

    @Autowired
    IClienteService clienteService;

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Add Venta Test")
    class AddVentaTest {
        @Test
        @Transactional
        void addVentaSinProductosLanzaExcepcionTest() {
            AddVentaDTO addVentaDTO = VentaDatos.crearAddVenta1DTO();
            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> ventaService.addVenta(addVentaDTO));
            assertEquals(String.format(ErrorMsgs.PRODUCTOS_NOT_FOUND, "[1, 2]"), exception.getMessage());
        }

        @Test
        @Transactional // para limpiar la bd despues de la prueba (rollback)
        @Sql(scripts = "/importProducto1.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void addVentaSin1ProductoDeUnaListaLanzaExcepcionTest() {
            AddVentaDTO addVentaDTO = VentaDatos.crearAddVenta1DTO();

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> ventaService.addVenta(addVentaDTO));

            assertEquals(String.format(ErrorMsgs.PRODUCTOS_NOT_FOUND, "[2]"), exception.getMessage());
        }

        @Test
        @Transactional
        @Sql(scripts = "/import2Productos.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void addVentaSinClienteLanzaExcepcionTest() {
            AddVentaDTO addVentaDTO = VentaDatos.crearAddVenta1DTO();

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> ventaService.addVenta(addVentaDTO));

            assertEquals(String.format(ErrorMsgs.CLIENTE_NOT_FOUND_ID, "1"), exception.getMessage());
        }

        @Test
        @Transactional
        @DisplayName("Agregar venta con productos y cliente existente")
        @Sql(scripts = "/import2Productos1Cliente.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void addVentaTest() {
            AddVentaDTO addVentaDTO = VentaDatos.crearAddVenta1DTO();

            VentaResponseDTO ventaResponseDTO = ventaService.addVenta(addVentaDTO);

            assertEquals(1, ventaService.getVentas().size());
            assertEquals(1L, ventaResponseDTO.getVentaId());
            assertEquals(2, ventaResponseDTO.getProductos().size());
            assertEquals(addVentaDTO.getIdCliente(), ventaResponseDTO.getCliente().getClienteId());
            assertEquals(addVentaDTO.getFechaVenta(), ventaResponseDTO.getFechaVenta());
            assertEquals(1000000.0, ventaResponseDTO.getTotal());
            assertEquals(9, productoService.getProductos().get(0).getCantidadDisponible());
            assertEquals(2, productoService.getProductos().get(1).getCantidadDisponible());
        }
    }

    @Nested
    @DisplayName("Get Venta Test")
    class GetVentaTest {
        @Test
        void getVentasTest() {
            assertEquals(1, ventaService.getVentas().size());
        }
    }
}