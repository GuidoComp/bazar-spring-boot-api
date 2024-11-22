package com.example.demo.utils;

import com.example.demo.datos.clientes.ClienteDatos;
import com.example.demo.datos.productos.ProductoDatos;
import com.example.demo.datos.ventas.VentaDatos;
import com.example.demo.dtos.requestDTOs.clienteDTOs.AddClienteDTO;
import com.example.demo.dtos.requestDTOs.productoDTOs.AddProductoDTO;
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
                    .ventas(VentaDatos.crearVentasConProductosYClientes())
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
            AddProductoDTO addProductoDTO = ProductoDatos.crearAddProducto1DTO();
            Producto producto = mapper.mapDTOToProducto(addProductoDTO);
            // Assert
            assertEquals(addProductoDTO.getNombre(), producto.getNombre());
            assertEquals(addProductoDTO.getMarca(), producto.getMarca());
            assertEquals(addProductoDTO.getCosto(), producto.getCosto());
            assertEquals(addProductoDTO.getCantidadDisponible(), producto.getCantidadDisponible());
        }

        @Test
        @DisplayName("Productos a DTOs")
        void mapProductosToDTO() {
            List<Producto> productos = ProductoDatos.crearProductosSinVentas();
            List<ProductoResponseDTO> productosDTO = mapper.mapProductosToDTO(productos);

            assertNotNull(productosDTO);
            assertEquals(productos.size(), productosDTO.size());
            assertEquals(productos.get(0).getProductoId(), productosDTO.get(0).getProductoId());
            assertEquals(productos.get(0).getNombre(), productosDTO.get(0).getNombre());
            assertEquals(productos.get(0).getMarca(), productosDTO.get(0).getMarca());
            assertEquals(productos.get(0).getCosto(), productosDTO.get(0).getCosto());
            assertEquals(productos.get(0).getCantidadDisponible(), productosDTO.get(0).getCantidadDisponible());
            assertEquals(productos.get(1).getProductoId(), productosDTO.get(1).getProductoId());
            assertEquals(productos.get(1).getNombre(), productosDTO.get(1).getNombre());
        }
    }

    @Nested
    @DisplayName("Mapper de Cliente")
    class ClienteMapper {
        @Test
        @DisplayName("Clientes a DTO")
        void mapClientesToDTO() {
            List<Cliente> clientes = ClienteDatos.crearClientesSinVentas();
            List<ClienteResponseDTO> clientesDTO = mapper.mapClientesToDTO(clientes);

            assertNotNull(clientesDTO);
            assertEquals(clientes.size(), clientesDTO.size());
            assertEquals(clientes.get(0).getClienteId(), clientesDTO.get(0).getClienteId());
            assertEquals(clientes.get(0).getNombre(), clientesDTO.get(0).getNombre());
            assertEquals(clientes.get(0).getApellido(), clientesDTO.get(0).getApellido());
            assertEquals(clientes.get(0).getDni(), clientesDTO.get(0).getDni());
            assertEquals(clientes.get(1).getClienteId(), clientesDTO.get(1).getClienteId());
            assertEquals(clientes.get(1).getNombre(), clientesDTO.get(1).getNombre());
        }

        @Test
        @DisplayName("AddCliente DTO a Cliente")
        void mapAddClienteDTOToCliente() {
            // Act
            AddClienteDTO addClienteDTO = ClienteDatos.crearAddClienteDTO();
            Cliente cliente = mapper.mapAddClienteDTOToCliente(addClienteDTO);
            // Assert
            assertEquals(addClienteDTO.getNombre(), cliente.getNombre());
            assertEquals(addClienteDTO.getApellido(), cliente.getApellido());
            assertEquals(addClienteDTO.getDni(), cliente.getDni());
        }

        @Test
        @DisplayName("Cliente a DTO")
        void mapClienteToDTO() {
            // Act
            Cliente cliente = ClienteDatos.crearClienteSinVentas();
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
            AddVentaDTO addVentaDTO = VentaDatos.crearAddVenta1DTO();
            Venta venta = mapper.mapAddVentaDTOToVenta(addVentaDTO);

            assertNotNull(venta);
            assertEquals(addVentaDTO.getFechaVenta(), venta.getFechaVenta());
            assertEquals(addVentaDTO.getIdCliente(), venta.getCliente().getClienteId());
            assertEquals(addVentaDTO.getIdsProductos().size(), venta.getProductos().size());
        }

        @Test
        @DisplayName("Venta -con Productos y Cliente- a DTO")
        void mapVentaToDTO() {
            Venta mockVenta = VentaDatos.crearVenta1ConProductosYCliente();
            VentaResponseDTO ventaResponseDTO = mapper.mapVentaToDTO(mockVenta);

            assertNotNull(ventaResponseDTO);
            assertEquals(mockVenta.getVentaId(), ventaResponseDTO.getVentaId());
            assertEquals(mockVenta.getFechaVenta(), ventaResponseDTO.getFechaVenta());
            assertEquals(mockVenta.getTotal(), ventaResponseDTO.getTotal());
            assertEquals(mockVenta.getProductos().size(), ventaResponseDTO.getProductos().size());
            assertEquals(mockVenta.getCliente().getClienteId(), ventaResponseDTO.getCliente().getClienteId());
        }

        @Test
        @DisplayName("Ventas a DTOs")
        void mapVentasToDto() {
            List<Venta> ventas = VentaDatos.crearVentasConProductosYClientes();
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