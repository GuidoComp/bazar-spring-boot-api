package com.example.demo.utils;

import com.example.demo.dtos.requestDTOs.clienteDTOs.AddClienteDTO;
import com.example.demo.dtos.requestDTOs.productoDTOs.AddProductoDTO;
import com.example.demo.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.example.demo.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
import com.example.demo.models.Cliente;
import com.example.demo.models.Producto;
import com.example.demo.models.Venta;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenericModelMapperTest {
    GenericModelMapper modelMapper;

    @BeforeEach
    void setUpAll() {
//        modelMapper = GenericModelMapper.getModelMapper();
    }

    @Nested
    @Tag("MapperTest")
    @DisplayName("Probando mapper")
    class MapperTest {
        @Test
        void getAllClientesTest() {

        }
    }

    @Test
    public void deberiaMappearUnProductoEnProductoDTO() {
        // Arrange
        ProductoResponseDTO productoResponseDTO;
        Producto producto = new Producto();
        producto.setProductoId(1L);
        producto.setNombre("Producto 1");
        producto.setMarca("Marca 1");
        producto.setCosto(100.0);
        producto.setCantidadDisponible(10.0);
        Venta venta = new Venta(1L, LocalDate.now(), 1000.0, null, null);
        List<Venta> ventas = new ArrayList<>();
        ventas.add(venta);
        producto.setVentas(ventas);
        // Act
        productoResponseDTO = this.modelMapper.mapProductoToDTO(producto);
        // Assert
        assertEquals(producto.getProductoId(), productoResponseDTO.getProductoId());
        assertEquals(producto.getNombre(), productoResponseDTO.getNombre());
        assertEquals(producto.getMarca(), productoResponseDTO.getMarca());
        assertEquals(producto.getCosto(), productoResponseDTO.getCosto());
        assertEquals(producto.getCantidadDisponible(), productoResponseDTO.getCantidadDisponible());
    }
    @Test
    public void deberiaDevolverNullAlMappearUnProductoNull() {
        // Arrange
        ProductoResponseDTO productoResponseDTO;
        Producto producto = null;
        // Act
        productoResponseDTO = this.modelMapper.mapProductoToDTO(producto);
        // Assert
        assertNull(productoResponseDTO);
    }

    @Test
    public void deberiaMappearUnProductoDTOEnProducto() {
        // Arrange
        AddProductoDTO addProductoDTO = new AddProductoDTO();
        addProductoDTO.setNombre("Producto 1");
        addProductoDTO.setMarca("Marca 1");
        addProductoDTO.setCosto(100.0);
        addProductoDTO.setCantidadDisponible(10.0);

        Producto producto;
        // Act
        producto = this.modelMapper.mapDTOToProducto(addProductoDTO);
        // Assert
        assertEquals(addProductoDTO.getNombre(), producto.getNombre());
        assertEquals(addProductoDTO.getMarca(), producto.getMarca());
        assertEquals(addProductoDTO.getCosto(), producto.getCosto());
        assertEquals(addProductoDTO.getCantidadDisponible(), producto.getCantidadDisponible());
    }

    @Test
    public void deberiaDevolverNullAlMappearUnAddProductoDTONulo() {
        // Arrange
        AddProductoDTO addProductoDTO = null;
        Producto producto;
        // Act
        producto = this.modelMapper.mapDTOToProducto(addProductoDTO);
        // Assert
        assertNull(producto);
    }

    @Test
    void mapClientesToDTO() {
        List<Cliente> clientes = List.of(new Cliente(1L, "Juan", "Perez", "36158155", null),
                new Cliente(2L, "Pedro", "Gomez", "36158156", null)
        );
        List< ClienteResponseDTO> clientesDTO = this.modelMapper.mapClientesToDTO(clientes);

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
    void mapAddClienteDTOToCliente() {
        // Arrange
        AddClienteDTO addClienteDTO = new AddClienteDTO("Juan", "Perez", "36158155");
        // Act
        Cliente cliente = this.modelMapper.mapAddClienteDTOToCliente(addClienteDTO);
        // Assert
        assertEquals(addClienteDTO.getNombre(), cliente.getNombre());
        assertEquals(addClienteDTO.getApellido(), cliente.getApellido());
        assertEquals(addClienteDTO.getDni(), cliente.getDni());
    }

    @Test
    void mapClienteToDTO() {
        // Arrange
        Cliente cliente = new Cliente(1L, "Juan", "Perez", "36158155", null);
        // Act
        ClienteResponseDTO clienteResponseDTO = this.modelMapper.mapClienteToDTO(cliente);
        // Assert
        assertEquals(cliente.getClienteId(), clienteResponseDTO.getClienteId());
        assertEquals(cliente.getNombre(), clienteResponseDTO.getNombre());
        assertEquals(cliente.getApellido(), clienteResponseDTO.getApellido());
        assertEquals(cliente.getDni(), clienteResponseDTO.getDni());
    }
}