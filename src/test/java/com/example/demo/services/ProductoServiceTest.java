package com.example.demo.services;

import com.example.demo.dtos.requestDTOs.productoDTOs.AddProductoDTO;
import com.example.demo.dtos.requestDTOs.productoDTOs.UpdateProductoDTO;
import com.example.demo.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Producto;
import com.example.demo.models.Venta;
import com.example.demo.repositories.IProductoRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //reemplaza a openMocks(this)
class ProductoServiceTest {
    @InjectMocks
    private ProductoService productoService;
    @Mock
    private IProductoRepository productoRepository;
    private static Producto producto;

    @BeforeAll
    public static void setUp() {
        producto = new Producto();
        producto.setProductoId(1L);
        producto.setNombre("Producto 1");
        producto.setMarca("marca 1");
        producto.setCosto(100.0);
        producto.setCantidadDisponible(10.0);
        producto.setVentas(new ArrayList<>());
    }

    @Test
    public void deberia_devolver_una_lista_linkedlist_de_ProductoResponseDTO() {
        List<ProductoResponseDTO> productosDTO = null;
        // Arrange / Given
        List<Producto> productos = new ArrayList<>();

        productos.add(producto);
        when(productoRepository.findAll()).thenReturn(productos);
        // Act / When
        productosDTO = productoService.getProductos();
        // Assert / Then
        assertNotNull(productosDTO, "La lista de productosDTO no debería ser null");
        assertTrue(productosDTO.isEmpty() || !productos.isEmpty());
        assertEquals(productosDTO.getClass(), LinkedList.class, "La lista debería ser de tipo LinkedList");
        assertEquals(productosDTO.get(0).getClass(), ProductoResponseDTO.class, "El elemento de la lista debería ser de tipo ProductoResponseDTO");

        verify(productoRepository, times(1)).findAll();
    }

    @Test
    public void deberia_mappear_dto_a_producto_agregar_un_producto_y_devolver_un_dto() {
        //Given
        AddProductoDTO addProductoDTO = new AddProductoDTO();
        addProductoDTO.setNombre(producto.getNombre());
        addProductoDTO.setMarca(producto.getMarca());
        addProductoDTO.setCosto(producto.getCosto());
        addProductoDTO.setCantidadDisponible(producto.getCantidadDisponible());

        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        //When
        ProductoResponseDTO productoResponseDTO = productoService.addProducto(addProductoDTO);

        //Then
        ArgumentCaptor<Producto> productoArgumentCaptor = ArgumentCaptor.forClass(Producto.class); // crea un captor de argumento de tipo Producto

        verify(productoRepository).save(productoArgumentCaptor.capture()); // captura el argumento de tipo Producto
        Producto productoCapturado = productoArgumentCaptor.getValue();
        assertEquals(productoCapturado.getClass(), producto.getClass(), "El producto capturado debería ser de tipo Producto");

        assertNotNull(productoResponseDTO, "El productoResponseDTO no debería ser null");
        assertEquals(productoResponseDTO.getNombre(), addProductoDTO.getNombre(), "El nombre del productoResponseDTO debería ser igual al nombre del addProductoDTO");
        assertEquals(productoResponseDTO.getMarca(), addProductoDTO.getMarca(), "La marca del productoResponseDTO debería ser igual a la marca del addProductoDTO");

        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    public void deberia_devolver_un_ProductoResponseDTO_y_eliminar_el_producto() {
        //Given
        Long id = 1L;
        when(productoRepository.findById(any(Long.class))).thenReturn(Optional.of(producto));
        //When
        ProductoResponseDTO productoResponseDTO = productoService.deleteProducto(id);
        //Then
        verify(productoRepository, times(1)).delete(any(Producto.class));
        assertEquals(productoResponseDTO.getProductoId(), producto.getProductoId());
        assertEquals(productoResponseDTO.getNombre(), producto.getNombre());
        assertEquals(productoResponseDTO.getMarca(), producto.getMarca());
        assertEquals(productoResponseDTO.getCosto(), producto.getCosto());
        assertEquals(productoResponseDTO.getCantidadDisponible(), producto.getCantidadDisponible());
    }

    @Test
    public void deberia_arrojar_una_excepcion_si_no_encuentra_producto_al_eliminar() {
        //Given
        when(productoRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        //When
        assertThrows(ResourceNotFoundException.class, () -> productoService.deleteProducto(1L));
        //Then
        verify(productoRepository, never()).delete(any(Producto.class));
    }

    @Test
    public void deberia_devolver_un_ProductoResponseDTO_y_actualizar_el_producto() {
        //Given
        Long id = 4L;
        when(productoRepository.findById(any(Long.class))).thenReturn(Optional.of(producto));
        UpdateProductoDTO updateProductoDTO = new UpdateProductoDTO(
                "Producto 2",
                "marca 2",
                200.0,
                20.0
        );
        //When
        ProductoResponseDTO productoResponseDTO = productoService.updateProducto(id, updateProductoDTO);
        //Then
        verify(productoRepository, times(1)).save(any(Producto.class));
        assertEquals(productoResponseDTO.getProductoId(), producto.getProductoId());
        assertEquals(productoResponseDTO.getNombre(), "Producto 2");
        assertEquals(productoResponseDTO.getMarca(), "marca 2");
        assertEquals(productoResponseDTO.getCosto(), 200.0);
        assertEquals(productoResponseDTO.getCantidadDisponible(), 20.0);
    }

    @Test
    public void cuando_un_atributo_no_sea_modificado_debe_devolver_el_mismo() {
        //Given
        Long id = 4L;
        when(productoRepository.findById(any(Long.class))).thenReturn(Optional.of(producto));
        UpdateProductoDTO updateProductoDTO = new UpdateProductoDTO(
                null,
                null,
                200.0,
                20.0
        );
        //When
        ProductoResponseDTO productoResponseDTO = productoService.updateProducto(id, updateProductoDTO);
        //Then
        verify(productoRepository, times(1)).save(any(Producto.class));
        assertEquals(productoResponseDTO.getProductoId(), producto.getProductoId());
        assertEquals(productoResponseDTO.getNombre(), producto.getNombre());
        assertEquals(productoResponseDTO.getMarca(), producto.getMarca());
        assertEquals(productoResponseDTO.getCosto(), 200.0);
        assertEquals(productoResponseDTO.getCantidadDisponible(), 20.0);
    }

    @Test
    void deberia_lanzar_ResourceNotFoundException_cuando_el_producto_no_existe() {
        //Given
        Long id = 1L;
        when(productoRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        UpdateProductoDTO updateProductoDTO = new UpdateProductoDTO(
                "Producto 2",
                "marca 2",
                200.0,
                20.0
        );
        //When
        assertThrows(ResourceNotFoundException.class, () -> productoService.updateProducto(id, updateProductoDTO));
        //Then
        verify(productoRepository, never()).save(any(Producto.class));
    }
}