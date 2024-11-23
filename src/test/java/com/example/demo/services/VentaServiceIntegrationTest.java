package com.example.demo.services;

import com.example.demo.datos.ventas.VentaDatos;
import com.example.demo.dtos.requestDTOs.ventaDTOs.AddVentaDTO;
import com.example.demo.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
import com.example.demo.dtos.responseDTOs.ventaDTOs.InfoMayorVenta;
import com.example.demo.dtos.responseDTOs.ventaDTOs.MontoYCantidadTotalDTO;
import com.example.demo.dtos.responseDTOs.ventaDTOs.VentaResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Producto;
import com.example.demo.models.Venta;
import com.example.demo.utils.ErrorMsgs;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class VentaServiceIntegrationTest {
    @Autowired
    IVentaService ventaService;

    @Autowired
    IProductoService productoService;

    @Autowired
    IClienteService clienteService;

    @Nested
    @DisplayName("Add Venta Test")
    @Order(1)
    @Transactional
    class AddVentaTest {
        @Test
        void addVentaSinProductosLanzaExcepcionTest() {
            AddVentaDTO addVentaDTO = VentaDatos.crearAddVenta1DTO();
            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> ventaService.addVenta(addVentaDTO));
            assertEquals(String.format(ErrorMsgs.PRODUCTOS_NOT_FOUND, "[1, 2]"), exception.getMessage());
        }

        @Test
        @Sql(scripts = "/importProducto1.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void addVentaSin1ProductoDeUnaListaLanzaExcepcionTest() {
            AddVentaDTO addVentaDTO = VentaDatos.crearAddVenta1DTO();

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> ventaService.addVenta(addVentaDTO));

            assertEquals(String.format(ErrorMsgs.PRODUCTOS_NOT_FOUND, "[2]"), exception.getMessage());
        }

        @Test
        @Sql(scripts = "/import2Productos.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void addVentaSinClienteLanzaExcepcionTest() {
            AddVentaDTO addVentaDTO = VentaDatos.crearAddVenta1DTO();

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> ventaService.addVenta(addVentaDTO));

            assertEquals(String.format(ErrorMsgs.CLIENTE_NOT_FOUND_ID, "1"), exception.getMessage());
        }

        @Test
        @DisplayName("Agregar venta con productos y cliente existente")
        @Sql(scripts = {"/import2Productos.sql", "/importCliente.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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
    @Order(2)
    @Transactional
    class GetVentaTest {
        @Test
        void getVentasEmptyTest() {
            List<VentaResponseDTO> ventas = ventaService.getVentas();
            assertNotNull(ventas);
            assertEquals(0, ventas.size());
        }

        @Test
        @Sql(scripts = {"/import2Productos.sql", "/importCliente.sql", "/importVentas.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void getVentasTest() {
            List<VentaResponseDTO> ventas = ventaService.getVentas();
            assertNotNull(ventas);
            assertEquals(2, ventas.size());
            assertEquals(1L, ventas.get(0).getVentaId());
            assertEquals(2L, ventas.get(1).getVentaId());
            assertEquals(2, ventas.get(0).getProductos().size());
            assertEquals(1, ventas.get(1).getProductos().size());
            assertEquals(1L, ventas.get(0).getCliente().getClienteId());
            assertEquals(1L, ventas.get(1).getCliente().getClienteId());
            assertEquals(1000000.0, ventas.get(0).getTotal());
            assertEquals(400000.0, ventas.get(1).getTotal());
        }

        @Test
        @Sql(scripts = {"/import2Productos.sql", "/importCliente.sql", "/importVentas.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void getVentaByIdTest() {
            Venta venta = ventaService.getVentaById(1L);
            assertNotNull(venta);
            assertEquals(1000000, venta.getTotal());
            assertEquals(LocalDate.of(2021, 1, 1), venta.getFechaVenta());
            assertEquals(2, venta.getProductos().size());
        }

        @Test
        @Sql(scripts = {"/import2Productos.sql", "/importCliente.sql", "/importVentas.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void getProductosDeVentaTest() {
            Producto productoImportado = new Producto(1L, "Procesador", "Intel Core i5 14600KF", 400000.0, 10.0);
            List<ProductoResponseDTO> productosDTODeVenta = ventaService.getProductosDTODeVenta(1L);
            assertEquals(2, productosDTODeVenta.size());
            assertEquals(productoImportado.getNombre(), productosDTODeVenta.get(0).getNombre());
            assertEquals(productoImportado.getMarca(), productosDTODeVenta.get(0).getMarca());
            assertEquals(productoImportado.getCantidadDisponible(), productosDTODeVenta.get(0).getCantidadDisponible());
        }

        @Test
        @Sql(scripts = "/import3Ventas4Productos2Clientes.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void getVentasByDateTest() {
            List<Venta> ventasByDate = ventaService.getVentasByDate(LocalDate.of(2021, 1, 1));
            assertEquals(2, ventasByDate.size());
            assertEquals(1L, ventasByDate.get(0).getVentaId());
            assertEquals(3L, ventasByDate.get(1).getVentaId());
            assertEquals(4, ventasByDate.get(1).getProductos().size());
            assertEquals(2, ventasByDate.get(0).getProductos().size());
        }

        @Test
        void getVentasByDateEmptyArrojaExcepcionTest() {
            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> ventaService.getVentasByDate(LocalDate.now()));
            assertEquals(String.format(ErrorMsgs.NO_SALES_FOR_THAT_DATE, LocalDate.now()), exception.getMessage());
        }

        @Test
        @Sql(scripts = "/import3Ventas4Productos2Clientes.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void getMontoYCantidadTotalesTest() {
            MontoYCantidadTotalDTO montoYCantidadTotales = ventaService.getMontoYCantidadTotales(LocalDate.of(2021, 1, 1));
            assertEquals(2670000.0, montoYCantidadTotales.getMontoTotal());
            assertEquals(LocalDate.of(2021, 1, 1), montoYCantidadTotales.getFecha());
            assertEquals(2, montoYCantidadTotales.getCantidadVentas());
        }

        @Test
        void getInfoMayorVentaEmptyArrojaExcepcionTest() {
            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> ventaService.getInfoMayorVenta());
            assertEquals(ErrorMsgs.NO_SALES_YET, exception.getMessage());
        }

        @Test
        @Sql(scripts = "/import3Ventas4Productos2Clientes.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void getInfoMayorVentaTest() {
            InfoMayorVenta infoMayorVenta = ventaService.getInfoMayorVenta();
            assertEquals(3L, infoMayorVenta.getIdVenta());
            assertEquals(1670000.0, infoMayorVenta.getTotal());
            assertEquals(4, infoMayorVenta.getCantProductos());
            assertEquals("Guido", infoMayorVenta.getNombreCliente());
            assertEquals("Compagno", infoMayorVenta.getApellidoCliente());
        }
    }
}