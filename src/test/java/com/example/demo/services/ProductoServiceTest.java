package com.example.demo.services;

import com.example.demo.Datos;
import com.example.demo.dtos.requestDTOs.productoDTOs.AddProductoDTO;
import com.example.demo.dtos.requestDTOs.productoDTOs.UpdateProductoDTO;
import com.example.demo.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.RestrictException;
import com.example.demo.models.Producto;
import com.example.demo.models.Venta;
import com.example.demo.repositories.IProductoRepository;
import com.example.demo.utils.ErrorMsgs;
import com.example.demo.utils.IModelMapper;
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

import static com.example.demo.Datos.PRODUCTOS_RESPONSE_DTO_STOCK_BAJO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    IProductoRepository productoRepository;

    @Mock
    IModelMapper mapper;

    @InjectMocks
    ProductoService productoService;

    @Test
    void getProductosTest() {
        // Given
        List<Producto> productos = Datos.crearProductos();
        when(productoRepository.findAll()).thenReturn(productos);
        when(mapper.mapProductosToDTO(anyList())).thenReturn(Datos.crearProductosResponseDTO());

        // Act / When
        var productosDTO = productoService.getProductos();

        // Assert / Then
        assertNotNull(productosDTO, () -> "La lista de productosDTO no debería ser null");
        assertTrue(!productosDTO.isEmpty());
        assertEquals(productosDTO.get(0).getClass(), ProductoResponseDTO.class, () -> "El elemento de la lista debería ser de tipo ProductoResponseDTO");
        assertEquals(productosDTO.size(), Datos.crearProductos().size(), () -> "La lista de productosDTO debería tener la misma cantidad de elementos que la lista de productos");
        assertEquals(productosDTO.get(0).getProductoId(), Datos.crearProductos().get(0).getProductoId(), () -> "El id del primer productoDTO debería ser igual al id del primer producto");
        assertEquals(productosDTO.get(0).getNombre(), Datos.crearProductos().get(0).getNombre(), () -> "El nombre del primer productoDTO debería ser igual al nombre del primer producto");

        verify(productoRepository, times(1)).findAll();
        verify(mapper, times(1)).mapProductosToDTO(productos);
    }

    @Test
    void addProductoNoExistenteTest() {
        //Given
        AddProductoDTO addProductoDTO = Datos.crearAddProductoDTO();
        ProductoResponseDTO productoResponseDTO = Datos.crearProductoResponseDTO();

        when(mapper.mapDTOToProducto(any(AddProductoDTO.class))).thenReturn(Datos.crearProducto1());
        when(productoRepository.save(any(Producto.class))).thenReturn(Datos.crearProducto1());
        when(mapper.mapProductoToDTO(any(Producto.class))).thenReturn(productoResponseDTO);
        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);

        //When
        ProductoResponseDTO respuesta = productoService.addProducto(addProductoDTO);

        //Then
        verify(productoRepository).save(captor.capture());
        assertEquals(captor.getValue().getClass(), Datos.crearProducto1().getClass(), () -> "El producto capturado debería ser de tipo Producto");

        assertNotNull(respuesta, () -> "El productoResponseDTO no debería ser null");
        assertEquals(respuesta.getNombre(), addProductoDTO.getNombre(), () -> "El nombre del productoResponseDTO debería ser igual al nombre del addProductoDTO");
        assertEquals(respuesta.getMarca(), addProductoDTO.getMarca(), () -> "La marca del productoResponseDTO debería ser igual a la marca del addProductoDTO");
        assertFalse(productoService.productoExistente(addProductoDTO.getNombre(), addProductoDTO.getMarca()), () -> "El producto no debería existir");

        verify(productoRepository, times(1)).save(any(Producto.class));
        verify(mapper, times(1)).mapDTOToProducto(addProductoDTO);
        verify(mapper, times(1)).mapProductoToDTO(any(Producto.class));
    }

    @Test
    void addProductoExistenteLanzaExcepcionTest() {
        assertThrows(RestrictException.class, () -> {
            //Given
            AddProductoDTO addProductoDTO = Datos.crearAddProductoDTO();
            Producto producto = Datos.crearProducto1();

            when(mapper.mapDTOToProducto(any(AddProductoDTO.class))).thenReturn(producto);
            when(productoRepository.findAll()).thenReturn(Datos.crearProductos());

            //When
            productoService.addProducto(addProductoDTO);

            //Then
            assertTrue(productoService.productoExistente(addProductoDTO.getNombre(), addProductoDTO.getMarca()), () -> "El producto debería existir previamente");

            verify(mapper, atMostOnce()).mapDTOToProducto(addProductoDTO);
            verify(productoRepository, never()).save(any(Producto.class));
            verify(mapper, never()).mapProductoToDTO(any(Producto.class));
            verify(productoRepository, atLeastOnce()).findAll();
        }, ErrorMsgs.PRODUCTO_YA_INGRESADO);
    }

    @Test
    void eliminarProductoExistente() {
        //Given
        Producto productoAEliminar = Datos.crearProducto1();
        when(productoRepository.findById(any(Long.class))).thenReturn(Optional.of(productoAEliminar));
        when(mapper.mapProductoToDTO(any(Producto.class))).thenReturn(Datos.crearProductoResponseDTO());
        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);

        //When
        ProductoResponseDTO productoResponseDTO = productoService.deleteProducto(1L);

        //Then
        verify(productoRepository).delete(captor.capture());
        assertSame(captor.getValue(), productoAEliminar, () -> "El producto capturado debería ser el mismo que el producto a eliminar");
        verify(productoRepository, atMostOnce()).delete(productoAEliminar);
        assertEquals(productoResponseDTO.getProductoId(), productoAEliminar.getProductoId());
        assertEquals(productoResponseDTO.getNombre(), productoAEliminar.getNombre());
        assertEquals(productoResponseDTO.getMarca(), productoAEliminar.getMarca());
        assertEquals(productoResponseDTO.getCosto(), productoAEliminar.getCosto());
        assertEquals(productoResponseDTO.getCantidadDisponible(), productoAEliminar.getCantidadDisponible());
    }

    @Test
    void deleteProductoInexistenteArrojaExcepcion() {
        //Given
        when(productoRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        //When
        assertThrows(ResourceNotFoundException.class, () -> productoService.deleteProducto(1L));
        //Then
        verify(productoRepository, never()).delete(any(Producto.class));
        verify(mapper, never()).mapProductoToDTO(any(Producto.class));
    }

    @Test
    void deleteProductoConVentasArrojaExcepcion() {
        //Given
        Producto producto = Datos.crearProducto1();
        producto.setVentas(new LinkedList<>(Datos.VENTAS));
        when(productoRepository.findById(any(Long.class))).thenReturn(Optional.of(producto));
        //When
        assertThrows(RestrictException.class, () -> productoService.deleteProducto(1L), ErrorMsgs.DELETE_PRODUCTO_RESTRICCION_FK);
        //Then
        verify(productoRepository, never()).delete(any(Producto.class));
        verify(mapper, never()).mapProductoToDTO(any(Producto.class));
    }

    @Test
    void updateProducto() {
        //Given
        Producto productoDb = Datos.crearProducto1();
        when(productoRepository.findById(any(Long.class))).thenReturn(Optional.of(productoDb));
        when(mapper.mapProductoToDTO(productoDb)).thenReturn(new ProductoResponseDTO(
                1L,
                "Procesador 2",
                "Intel Core i7 13700K",
                550000.0,
                10.0
        ));
        UpdateProductoDTO updateProductoDTO = new UpdateProductoDTO(
                "Procesador 2",
                "Intel Core i7 13700K",
                550000.0,
                null
        );
        //When
        ProductoResponseDTO productoResponseDTO = productoService.updateProducto(1L, updateProductoDTO);
        //Then
        verify(productoRepository, times(1)).save(productoDb);
        verify(mapper, atMostOnce()).mapProductoToDTO(productoDb);
        assertEquals(productoResponseDTO.getProductoId(), Datos.crearProducto1().getProductoId());
        assertEquals(productoResponseDTO.getNombre(), updateProductoDTO.getNombre());
        assertEquals(productoResponseDTO.getMarca(), updateProductoDTO.getMarca());
        assertEquals(productoResponseDTO.getCosto(), updateProductoDTO.getCosto());
        assertEquals(productoResponseDTO.getCantidadDisponible(), productoDb.getCantidadDisponible());
    }

    @Test
    void cuando_un_atributo_no_sea_modificado_debe_devolver_el_mismo() {
        //Given
        Producto productoDb = Datos.crearProducto1();
        when(productoRepository.findById(any(Long.class))).thenReturn(Optional.of(productoDb));
        when(mapper.mapProductoToDTO(productoDb)).thenReturn(new ProductoResponseDTO(
                productoDb.getProductoId(),
                productoDb.getNombre(),
                productoDb.getMarca(),
                550000.0,
                20.0
        ));
        UpdateProductoDTO updateProductoDTO = new UpdateProductoDTO(
                null,
                null,
                550000.0,
                20.0
        );
        //When
        ProductoResponseDTO productoResponseDTO = productoService.updateProducto(1L, updateProductoDTO);
        //Then
        verify(productoRepository, times(1)).save(any(Producto.class));
        assertEquals(productoResponseDTO.getProductoId(), productoDb.getProductoId());
        assertEquals(productoResponseDTO.getNombre(), productoDb.getNombre());
        assertEquals(productoResponseDTO.getMarca(), productoDb.getMarca());
        assertEquals(productoResponseDTO.getCosto(), updateProductoDTO.getCosto());
        assertEquals(productoResponseDTO.getCantidadDisponible(), updateProductoDTO.getCantidadDisponible());
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

    @Test
    void getProductosByIds() {
        when(productoRepository.findAllById(anyList())).thenReturn(Datos.PRODUCTOS_CON_Y_SIN_VENTAS);

        var productos = productoService.getProductosByIds(List.of(1L, 2L));

        assertEquals(Datos.PRODUCTOS_CON_Y_SIN_VENTAS.size(), productos.size());
    }

    @Test
    void getProductosByIdsNotFound() {
        when(productoRepository.findAllById(anyList())).thenReturn(new ArrayList<>());

        var productos = productoService.getProductosByIds(List.of(1L, 2L));

        assertTrue(productos.isEmpty());
    }

    @Test
    void getProductosConStockBajo() {
        when(productoRepository.findByCantidadDisponibleLessThan(anyInt())).thenReturn(Datos.PRODUCTOS_STOCK_BAJO);
        when(mapper.mapProductosToDTO(anyList())).thenReturn(Datos.PRODUCTOS_RESPONSE_DTO_STOCK_BAJO);

        var productos = productoService.getProductosConStockBajo();

        assertEquals(2, productos.size());
    }
}