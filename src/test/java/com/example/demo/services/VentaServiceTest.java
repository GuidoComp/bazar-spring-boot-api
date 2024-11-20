package com.example.demo.services;

import com.example.demo.datos.clientes.ClienteDatos;
import com.example.demo.datos.productos.ProductoDatos;
import com.example.demo.datos.ventas.VentaDatos;
import com.example.demo.dtos.requestDTOs.productoDTOs.UpdateProductoDTO;
import com.example.demo.dtos.requestDTOs.ventaDTOs.AddVentaDTO;
import com.example.demo.dtos.requestDTOs.ventaDTOs.UpdateVentaDTO;
import com.example.demo.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
import com.example.demo.dtos.responseDTOs.ventaDTOs.VentaResponseDTO;
import com.example.demo.models.Cliente;
import com.example.demo.models.Producto;
import com.example.demo.models.Venta;
import com.example.demo.repositories.IVentaRepository;
import com.example.demo.utils.IModelMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    IClienteService clienteService;

    @Mock
    IProductoService productoService;

    @Mock
    IVentaRepository ventaRepository;

    @Mock
    IModelMapper mapper;

    @InjectMocks
    VentaService ventaService;

    @Captor
    ArgumentCaptor<Venta> captor;

    @Test
    void getVentas() {
        List<Venta> ventas = VentaDatos.crearVentasConProductosYClientes();
        List<VentaResponseDTO> ventasDTOs = VentaDatos.crearVentasResponseDTOConProductosYClientes();

        InOrder inOrder = inOrder(ventaRepository, mapper);
        when(ventaRepository.findAll()).thenReturn(ventas);
        when(mapper.mapVentasToDTO(any(List.class))).thenReturn(ventasDTOs);

        List<VentaResponseDTO> ventasDTO = ventaService.getVentas();

        assertNotNull(ventasDTO);
        assertEquals(ventas.get(0).getVentaId(), ventasDTO.get(0).getVentaId());
        assertEquals(ventas.get(0).getFechaVenta(), ventasDTO.get(0).getFechaVenta());
        assertEquals(ventas.get(0).getTotal(), ventasDTO.get(0).getTotal());
        assertEquals(ventas.get(0).getProductos().get(0).getProductoId(), ventasDTO.get(0).getProductos().get(0).getProductoId());
        assertEquals(ventas.get(0).getCliente().getDni(), ventasDTO.get(0).getCliente().getDni());
        assertEquals(ventas.get(0).getCliente().getNombre(), ventasDTO.get(0).getCliente().getNombre());
        inOrder.verify(ventaRepository).findAll();
        inOrder.verify(mapper).mapVentasToDTO(ventas);
    }

    @Test
    void addVenta() {
        AddVentaDTO addVentaDTO = VentaDatos.crearVenta1DTO();
        Venta nuevaVenta = VentaDatos.crearVenta1ConProductosYCliente();
        InOrder inOrder = inOrder(ventaRepository, clienteService, productoService, mapper);

        when(productoService.getProductosByIds(any())).thenReturn(ProductoDatos.crearProductosConYSinVentas());
        when(clienteService.getClienteById(any())).thenReturn(ClienteDatos.crearClienteSinVentas());
        when(ventaRepository.save(any(Venta.class))).thenReturn(nuevaVenta);
        when(mapper.mapVentaToDTO(any())).thenReturn(VentaDatos.crearVenta1ResponseDTO());

        VentaResponseDTO ventaResponseDTO = ventaService.addVenta(addVentaDTO);

        assertNotNull(ventaResponseDTO);
        assertEquals(nuevaVenta.getVentaId(), ventaResponseDTO.getVentaId());
        assertEquals(nuevaVenta.getFechaVenta(), ventaResponseDTO.getFechaVenta());
        assertEquals(nuevaVenta.getTotal(), ventaResponseDTO.getTotal());
        assertEquals(nuevaVenta.getProductos().get(0).getProductoId(), ventaResponseDTO.getProductos().get(0).getProductoId());
        assertEquals(nuevaVenta.getCliente().getDni(), ventaResponseDTO.getCliente().getDni());
        assertEquals(nuevaVenta.getCliente().getNombre(), ventaResponseDTO.getCliente().getNombre());
        inOrder.verify(productoService).getProductosByIds(addVentaDTO.getIdsProductos());
        inOrder.verify(productoService).updateProducto(any(), any());
        inOrder.verify(clienteService).getClienteById(addVentaDTO.getIdCliente());
        inOrder.verify(ventaRepository).save(argThat(argument -> {
            assertEquals(null, argument.getVentaId());
            assertEquals(addVentaDTO.getFechaVenta(), argument.getFechaVenta());
            assertEquals(nuevaVenta.getTotal(), argument.getTotal());
            assertEquals(addVentaDTO.getIdsProductos().get(0), argument.getProductos().get(0).getProductoId());
            assertEquals(nuevaVenta.getProductos().get(0).getCantidadDisponible(), argument.getProductos().get(0).getCantidadDisponible());
            assertEquals(addVentaDTO.getIdsProductos().get(1), argument.getProductos().get(1).getProductoId());
            assertEquals(nuevaVenta.getProductos().get(1).getCantidadDisponible(), argument.getProductos().get(1).getCantidadDisponible());
            assertEquals(addVentaDTO.getIdCliente(), argument.getCliente().getClienteId());
            assertEquals(nuevaVenta.getCliente().getDni(), argument.getCliente().getDni());
            assertEquals(nuevaVenta.getCliente().getNombre(), argument.getCliente().getNombre());
            return true;
        }));
        inOrder.verify(mapper).mapVentaToDTO(any(Venta.class));

        verify(productoService, times(2)).checkStock(any(Producto.class));
        verify(productoService, times(2)).updateProducto(any(), any());
        verify(clienteService).getClienteById(any());
        verify(ventaRepository).save(captor.capture());
        Venta ventaGuardada = captor.getValue();
        assertEquals(null, ventaGuardada.getVentaId());
        assertEquals(addVentaDTO.getFechaVenta(), ventaGuardada.getFechaVenta());
        assertEquals(nuevaVenta.getTotal(), ventaGuardada.getTotal());
        assertEquals(addVentaDTO.getIdsProductos().get(0), ventaGuardada.getProductos().get(0).getProductoId());
        assertEquals(nuevaVenta.getProductos().get(0).getCantidadDisponible(), ventaGuardada.getProductos().get(0).getCantidadDisponible());
        assertEquals(addVentaDTO.getIdsProductos().get(1), ventaGuardada.getProductos().get(1).getProductoId());
        assertEquals(nuevaVenta.getProductos().get(1).getCantidadDisponible(), ventaGuardada.getProductos().get(1).getCantidadDisponible());
        assertEquals(addVentaDTO.getIdCliente(), ventaGuardada.getCliente().getClienteId());
        assertEquals(nuevaVenta.getCliente().getDni(), ventaGuardada.getCliente().getDni());
        assertEquals(nuevaVenta.getCliente().getNombre(), ventaGuardada.getCliente().getNombre());
    }

    @Test
    void deleteVenta() {
        Long ventaId = 1L;
        Venta venta = VentaDatos.crearVenta1ConProductosYCliente();
        when(ventaRepository.findById(ventaId)).thenReturn(Optional.of(venta));
        when(mapper.mapVentaToDTO(any(Venta.class))).thenReturn(any(VentaResponseDTO.class));

        ventaService.deleteVenta(ventaId);

        verify(ventaRepository).findById(ventaId);
        verify(productoService, times(2)).updateProducto(
                anyLong(),
                argThat(dto -> dto.getCantidadDisponible() != null)
        );
        verify(ventaRepository).delete(venta);
        assertTrue(venta.getProductos().isEmpty());
        assertEquals(0.0, venta.getTotal());
    }

    @Test
    void updateVenta() {
        Long ventaId = 1L;

        Venta venta = VentaDatos.crearVenta1ConProductosYCliente();

        LocalDate nuevaFecha = LocalDate.of(2024, 1, 1);
        List<Long> nuevosIdsProductos = Arrays.asList(3L, 4L);
        Long nuevoClienteId = 2L;

        UpdateVentaDTO updateVentaDTO = new UpdateVentaDTO(nuevaFecha, nuevosIdsProductos, nuevoClienteId);
        VentaResponseDTO expectedResponse = new VentaResponseDTO();

        Producto nuevoProducto1 = new Producto(3L, "Nuevo Producto 1", "Marca 1", 10000.0, 20.0);
        Producto nuevoProducto2 = new Producto(4L, "Nuevo Producto 2", "Marca 2", 50000.0, 15.0);
        List<Producto> nuevosProductos = Arrays.asList(nuevoProducto1, nuevoProducto2);

        Cliente nuevoCliente = new Cliente(2L, "Nuevo Cliente", "Apellido nuevo cliente", "12345678");

        when(ventaRepository.findById(ventaId)).thenReturn(Optional.of(venta));
        when(productoService.getProductosByIds(nuevosIdsProductos)).thenReturn(nuevosProductos);
        when(clienteService.getClienteById(nuevoClienteId)).thenReturn(nuevoCliente);
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);
        when(mapper.mapVentaToDTO(any(Venta.class))).thenReturn(expectedResponse);

        VentaResponseDTO response = ventaService.updateVenta(ventaId, updateVentaDTO);

        verify(ventaRepository).findById(ventaId);

        verify(productoService).getProductosByIds(nuevosIdsProductos);
        assertEquals(nuevosProductos, venta.getProductos());

        verify(clienteService).getClienteById(nuevoClienteId);
        assertEquals(nuevoCliente, venta.getCliente());

        assertEquals(nuevaFecha, venta.getFechaVenta());

        verify(ventaRepository).save(venta);

        assertEquals(expectedResponse, response);
    }
}