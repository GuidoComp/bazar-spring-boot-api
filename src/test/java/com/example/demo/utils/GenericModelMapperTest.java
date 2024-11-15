package com.example.demo.utils;

import com.example.demo.dtos.requestDTOs.clienteDTOs.AddClienteDTO;
import com.example.demo.dtos.requestDTOs.ventaDTOs.AddVentaDTO;
import com.example.demo.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.example.demo.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
import com.example.demo.dtos.responseDTOs.ventaDTOs.VentaResponseDTO;
import com.example.demo.models.Cliente;
import com.example.demo.models.Producto;
import com.example.demo.models.Venta;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
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
            // Arrange
            AddClienteDTO addClienteDTO = new AddClienteDTO("Juan", "Perez", "36158155");
            // Act
            Cliente cliente = mapper.mapAddClienteDTOToCliente(addClienteDTO);
            // Assert
            assertEquals(addClienteDTO.getNombre(), cliente.getNombre());
            assertEquals(addClienteDTO.getApellido(), cliente.getApellido());
            assertEquals(addClienteDTO.getDni(), cliente.getDni());
        }

        @Test
        @DisplayName("Cliente a DTO")
        void mapClienteToDTO() {
            // Arrange
            Cliente cliente = new Cliente(1L, "Juan", "Perez", "36158155", null);
            // Act
            ClienteResponseDTO clienteResponseDTO = mapper.mapClienteToDTO(cliente);
            // Assert
            assertEquals(cliente.getClienteId(), clienteResponseDTO.getClienteId());
            assertEquals(cliente.getNombre(), clienteResponseDTO.getNombre());
            assertEquals(cliente.getApellido(), clienteResponseDTO.getApellido());
            assertEquals(cliente.getDni(), clienteResponseDTO.getDni());
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
            AddVentaDTO addVentaDTO = new AddVentaDTO(LocalDate.now(), List.of(1L, 2L), 1L);

            Venta venta = mapper.mapAddVentaDTOToVenta(addVentaDTO);

            assertNotNull(venta);
            assertEquals(addVentaDTO.getFechaVenta(), venta.getFechaVenta());
            assertEquals(addVentaDTO.getIdCliente(), venta.getCliente().getClienteId());
            assertEquals(addVentaDTO.getIdsProductos().size(), venta.getProductos().size());
        }

        @Test
        @DisplayName("Venta -con Productos y Cliente- a DTO")
        void mapVentaToDTO() {
            List<Producto> productos = List.of(
                    new Producto(1L, "Producto 1", "Marca 1", 100.0, 10.0, List.of(
                            new Venta(1L, LocalDate.now(), 1000.0, null, null)
                    )),
                    new Producto(2L, "Producto 2", "Marca 2", 200.0, 20.0, null)
            );
            Venta venta = new Venta(1L, LocalDate.now(), 1000.0, productos, new Cliente(1L, "Juan", "Perez", "36158155"));

            VentaResponseDTO ventaResponseDTO = mapper.mapVentaToDTO(venta);

            assertNotNull(ventaResponseDTO);
            assertEquals(venta.getVentaId(), ventaResponseDTO.getVentaId());
            assertEquals(venta.getFechaVenta(), ventaResponseDTO.getFechaVenta());
            assertEquals(venta.getTotal(), ventaResponseDTO.getTotal());
            assertEquals(venta.getProductos().size(), ventaResponseDTO.getProductos().size());
            assertEquals(venta.getCliente().getClienteId(), ventaResponseDTO.getCliente().getClienteId());
        }

        @Test
        @DisplayName("Ventas a DTOs")
        void mapVentasToDto() {
            List<Venta> ventas = List.of(
                    new Venta(1L, LocalDate.now(), 1000.0, List.of(
                            new Producto(1L, "Producto 1", "Marca 1", 100.0, 10.0, null)
                    ), new Cliente(1L, "Juan", "Perez", "36158155")),
                    new Venta(2L, LocalDate.now(), 2000.0, List.of(
                            new Producto(2L, "Producto 2", "Marca 2", 200.0, 20.0, null)
                    ), new Cliente(2L, "Pedro", "Gomez", "36158156"))
            );

            List<VentaResponseDTO> ventasDTO = mapper.mapVentasToDTO(ventas);

            assertNotNull(ventasDTO);
            assertEquals(ventas.size(), ventasDTO.size());
            assertEquals(ventas.get(0).getVentaId(), ventasDTO.get(0).getVentaId());
            assertEquals(ventas.get(0).getFechaVenta(), ventasDTO.get(0).getFechaVenta());
            assertEquals(ventas.get(0).getTotal(), ventasDTO.get(0).getTotal());
            assertEquals(ventas.get(0).getProductos().size(), ventasDTO.get(0).getProductos().size());
            assertEquals(ventas.get(0).getCliente().getClienteId(), ventasDTO.get(0).getCliente().getClienteId());
            assertEquals(ventas.get(1).getVentaId(), ventasDTO.get(1).getVentaId());
            assertEquals(ventas.get(1).getFechaVenta(), ventasDTO.get(1).getFechaVenta());
            assertEquals(ventas.get(1).getTotal(), ventasDTO.get(1).getTotal());
            assertEquals(ventas.get(1).getProductos().size(), ventasDTO.get(1).getProductos().size());
            assertEquals(ventas.get(1).getCliente().getClienteId(), ventasDTO.get(1).getCliente().getClienteId());
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