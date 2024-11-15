package com.example.demo.utils;

import com.example.demo.Datos;
import com.example.demo.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.example.demo.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
import com.example.demo.dtos.responseDTOs.ventaDTOs.VentaResponseDTO;
import com.example.demo.models.Cliente;
import com.example.demo.models.Producto;
import com.example.demo.models.Venta;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class GenericModelMapperTest {
    @Autowired
    private GenericModelMapper mapper;

    @Nested
    @DisplayName("Mapper de Producto")
    class ProductoMapper {
        @Test
        @DisplayName("Producto a DTO")
        public void deberiaMappearUnProductoEnProductoDTO() {
            // Arrange
            Producto producto = new Producto();
            producto.builder()
                    .productoId(1L)
                    .nombre("Producto 1")
                    .marca("Marca 1")
                    .costo(100.0)
                    .cantidadDisponible(10.0)
                    .ventas(Datos.VENTAS)
                    .build();

            // Act
            ProductoResponseDTO productoResponseDTO = mapper.mapProductoToDTO(producto);

            // Assert
            assertEquals(producto.getProductoId(), productoResponseDTO.getProductoId());
            assertEquals(producto.getNombre(), productoResponseDTO.getNombre());
            assertEquals(producto.getMarca(), productoResponseDTO.getMarca());
            assertEquals(producto.getCosto(), productoResponseDTO.getCosto());
            assertEquals(producto.getCantidadDisponible(), productoResponseDTO.getCantidadDisponible());
        }

        @Test
        @DisplayName("DTO a Producto")
        public void deberiaMappearUnProductoDTOEnProducto() {
            // Act
            Producto producto = mapper.mapDTOToProducto(Datos.ADD_PRODUCTO_DTO);
            // Assert
            assertEquals(Datos.ADD_PRODUCTO_DTO.getNombre(), producto.getNombre());
            assertEquals(Datos.ADD_PRODUCTO_DTO.getMarca(), producto.getMarca());
            assertEquals(Datos.ADD_PRODUCTO_DTO.getCosto(), producto.getCosto());
            assertEquals(Datos.ADD_PRODUCTO_DTO.getCantidadDisponible(), producto.getCantidadDisponible());
        }

        @Test
        @DisplayName("Productos a DTOs")
        void mapProductosToDTO() {
            List<ProductoResponseDTO> productosDTO = mapper.mapProductosToDTO(Datos.PRODUCTOS_SIN_VENTAS);

            assertNotNull(productosDTO);
            assertEquals(Datos.PRODUCTOS_SIN_VENTAS.size(), productosDTO.size());
            assertEquals(Datos.PRODUCTOS_SIN_VENTAS.get(0).getProductoId(), productosDTO.get(0).getProductoId());
            assertEquals(Datos.PRODUCTOS_SIN_VENTAS.get(0).getNombre(), productosDTO.get(0).getNombre());
            assertEquals(Datos.PRODUCTOS_SIN_VENTAS.get(0).getMarca(), productosDTO.get(0).getMarca());
            assertEquals(Datos.PRODUCTOS_SIN_VENTAS.get(0).getCosto(), productosDTO.get(0).getCosto());
            assertEquals(Datos.PRODUCTOS_SIN_VENTAS.get(0).getCantidadDisponible(), productosDTO.get(0).getCantidadDisponible());
            assertEquals(Datos.PRODUCTOS_SIN_VENTAS.get(1).getProductoId(), productosDTO.get(1).getProductoId());
            assertEquals(Datos.PRODUCTOS_SIN_VENTAS.get(1).getNombre(), productosDTO.get(1).getNombre());
        }
    }

    @Nested
    @DisplayName("Mapper de Cliente")
    class ClienteMapper {
        @Test
        @DisplayName("Clientes a DTO")
        void mapClientesToDTO() {
            List<ClienteResponseDTO> clientesDTO = mapper.mapClientesToDTO(Datos.CLIENTES_SIN_VENTAS);

            assertNotNull(clientesDTO);
            assertEquals(Datos.CLIENTES_SIN_VENTAS.size(), clientesDTO.size());
            assertEquals(Datos.CLIENTES_SIN_VENTAS.get(0).getClienteId(), clientesDTO.get(0).getClienteId());
            assertEquals(Datos.CLIENTES_SIN_VENTAS.get(0).getNombre(), clientesDTO.get(0).getNombre());
            assertEquals(Datos.CLIENTES_SIN_VENTAS.get(0).getApellido(), clientesDTO.get(0).getApellido());
            assertEquals(Datos.CLIENTES_SIN_VENTAS.get(0).getDni(), clientesDTO.get(0).getDni());
            assertEquals(Datos.CLIENTES_SIN_VENTAS.get(1).getClienteId(), clientesDTO.get(1).getClienteId());
            assertEquals(Datos.CLIENTES_SIN_VENTAS.get(1).getNombre(), clientesDTO.get(1).getNombre());
        }

        @Test
        @DisplayName("AddCliente DTO a Cliente")
        void mapAddClienteDTOToCliente() {
            // Act
            Cliente cliente = mapper.mapAddClienteDTOToCliente(Datos.ADD_CLIENTE_DTO);
            // Assert
            assertEquals(Datos.ADD_CLIENTE_DTO.getNombre(), cliente.getNombre());
            assertEquals(Datos.ADD_CLIENTE_DTO.getApellido(), cliente.getApellido());
            assertEquals(Datos.ADD_CLIENTE_DTO.getDni(), cliente.getDni());
        }

        @Test
        @DisplayName("Cliente a DTO")
        void mapClienteToDTO() {
            // Act
            ClienteResponseDTO clienteResponseDTO = mapper.mapClienteToDTO(Datos.CLIENTE_SIN_VENTAS);
            // Assert
            assertEquals(Datos.CLIENTE_SIN_VENTAS.getClienteId(), clienteResponseDTO.getClienteId());
            assertEquals(Datos.CLIENTE_SIN_VENTAS.getNombre(), clienteResponseDTO.getNombre());
            assertEquals(Datos.CLIENTE_SIN_VENTAS.getApellido(), clienteResponseDTO.getApellido());
            assertEquals(Datos.CLIENTE_SIN_VENTAS.getDni(), clienteResponseDTO.getDni());
        }

        @Test
        @DisplayName("Cliente nulo a DTO")
        void mapClienteNullToDTOShouldThrowAnException() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mapper.mapClienteToDTO(null));

            String mensajeArrojado = exception.getMessage();
            assertEquals(mensajeArrojado, String.format(ErrorMsgs.PARAMETRO_NULO));
        }

        @Test
        @DisplayName("Cliente vacío a DTO")
        void mapClienteEmptyToDTO() {
            Cliente cliente = new Cliente();

            ClienteResponseDTO clienteResponseDTO = mapper.mapClienteToDTO(cliente);

            assertNotNull(clienteResponseDTO);
            assertNull(clienteResponseDTO.getNombre());
            assertNull(clienteResponseDTO.getDni());
            assertNull(clienteResponseDTO.getApellido());
            assertNull(clienteResponseDTO.getClienteId());
        }
    }

    @Nested
    @DisplayName("Mapper de Ventas")
    class VentaMapper {
        @Test
        @DisplayName("AddVentaDTO a Venta")
        void mapAddVentaDTOtoVenta() {
            Venta venta = mapper.mapAddVentaDTOToVenta(Datos.ADD_VENTA_DTO);

            assertNotNull(venta);
            assertEquals(Datos.ADD_VENTA_DTO.getFechaVenta(), venta.getFechaVenta());
            assertEquals(Datos.ADD_VENTA_DTO.getIdCliente(), venta.getCliente().getClienteId());
            assertEquals(Datos.ADD_VENTA_DTO.getIdsProductos().size(), venta.getProductos().size());
        }

        @Test
        @DisplayName("Venta -con Productos y Cliente- a DTO")
        void mapVentaToDTO() {
            VentaResponseDTO ventaResponseDTO = mapper.mapVentaToDTO(Datos.VENTA_CON_PRODUCTOS_Y_CLIENTE);

            assertNotNull(ventaResponseDTO);
            assertEquals(Datos.VENTA_CON_PRODUCTOS_Y_CLIENTE.getVentaId(), ventaResponseDTO.getVentaId());
            assertEquals(Datos.VENTA_CON_PRODUCTOS_Y_CLIENTE.getFechaVenta(), ventaResponseDTO.getFechaVenta());
            assertEquals(Datos.VENTA_CON_PRODUCTOS_Y_CLIENTE.getTotal(), ventaResponseDTO.getTotal());
            assertEquals(Datos.VENTA_CON_PRODUCTOS_Y_CLIENTE.getProductos().size(), ventaResponseDTO.getProductos().size());
            assertEquals(Datos.VENTA_CON_PRODUCTOS_Y_CLIENTE.getCliente().getClienteId(), ventaResponseDTO.getCliente().getClienteId());
        }

        @Test
        @DisplayName("Ventas a DTOs")
        void mapVentasToDto() {
            List<VentaResponseDTO> ventasDTO = mapper.mapVentasToDTO(Datos.VENTAS_CON_PRODUCTOS_Y_CLIENTE);

            assertNotNull(ventasDTO);
            assertEquals(Datos.VENTAS_CON_PRODUCTOS_Y_CLIENTE.size(), ventasDTO.size());
            assertEquals(Datos.VENTAS_CON_PRODUCTOS_Y_CLIENTE.get(0).getVentaId(), ventasDTO.get(0).getVentaId());
            assertEquals(Datos.VENTAS_CON_PRODUCTOS_Y_CLIENTE.get(0).getFechaVenta(), ventasDTO.get(0).getFechaVenta());
            assertEquals(Datos.VENTAS_CON_PRODUCTOS_Y_CLIENTE.get(0).getTotal(), ventasDTO.get(0).getTotal());
            assertEquals(Datos.VENTAS_CON_PRODUCTOS_Y_CLIENTE.get(0).getProductos().size(), ventasDTO.get(0).getProductos().size());
            assertEquals(Datos.VENTAS_CON_PRODUCTOS_Y_CLIENTE.get(0).getCliente().getClienteId(), ventasDTO.get(0).getCliente().getClienteId());
            assertEquals(Datos.VENTAS_CON_PRODUCTOS_Y_CLIENTE.get(1).getVentaId(), ventasDTO.get(1).getVentaId());
            assertEquals(Datos.VENTAS_CON_PRODUCTOS_Y_CLIENTE.get(1).getFechaVenta(), ventasDTO.get(1).getFechaVenta());
            assertEquals(Datos.VENTAS_CON_PRODUCTOS_Y_CLIENTE.get(1).getTotal(), ventasDTO.get(1).getTotal());
            assertEquals(Datos.VENTAS_CON_PRODUCTOS_Y_CLIENTE.get(1).getProductos().size(), ventasDTO.get(1).getProductos().size());
            assertEquals(Datos.VENTAS_CON_PRODUCTOS_Y_CLIENTE.get(1).getCliente().getClienteId(), ventasDTO.get(1).getCliente().getClienteId());
        }

        @Test
        @DisplayName("Venta nula a DTO")
        void mapVentaNullToDTOShouldThrowAnException() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mapper.mapVentaToDTO(null));

            String mensajeArrojado = exception.getMessage();
            assertEquals(mensajeArrojado, String.format(ErrorMsgs.PARAMETRO_NULO));
        }
    }
}