package com.example.demo.services;

import com.example.demo.datos.ventas.VentaDatos;
import com.example.demo.dtos.requestDTOs.ventaDTOs.AddVentaDTO;
import com.example.demo.dtos.requestDTOs.ventaDTOs.UpdateVentaDTO;
import com.example.demo.dtos.responseDTOs.ventaDTOs.VentaResponseDTO;
import com.example.demo.models.Cliente;
import com.example.demo.models.Producto;
import com.example.demo.models.Venta;
import com.example.demo.repositories.IVentaRepository;
import com.example.demo.utils.IModelMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @Test
    void getVentas() {
        List<Venta> ventas = VentaDatos.crearVentasConProductosYClientes();
        InOrder inOrder = inOrder(ventaRepository, mapper);
        when(ventaRepository.findAll()).thenReturn(ventas);
        when(mapper.mapVentasToDTO(any(List.class))).thenReturn(VentaDatos.crearVentasConProductosYClientes());

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
        AddVentaDTO addVentaDTO = VentaDatos.crearVentaDTO();
        InOrder inOrder = inOrder(ventaRepository, clienteService, productoService, mapper);
    }

    @Test
    void deleteVenta() {
        List<Venta> ventas = new ArrayList<>();
        List<Producto> productos = new ArrayList<>();
        productos.add(new Producto(1L, "Producto 1","Marca 1", 100.0, 10.0, null));
        Cliente cliente = new Cliente(1L, "Juan", "Perez", "36158155", null);

        ventas.add(new Venta(1L, LocalDate.now(), 1000.0, productos, cliente));

        when(ventaRepository.findById(1L)).thenReturn(Optional.ofNullable(ventas.get(0)));

        VentaResponseDTO ventaEliminadaDTO = ventaService.deleteVenta(1L);

        assertNotNull(ventaEliminadaDTO);
        assertEquals(ventas.get(0).getVentaId(), ventaEliminadaDTO.getVentaId());
        assertEquals(ventas.get(0).getFechaVenta(), ventaEliminadaDTO.getFechaVenta());
        assertEquals(ventas.get(0).getTotal(), ventaEliminadaDTO.getTotal());
        //assertEquals(ventas.get(0).getProductos().get(0).getProductoId(), ventaEliminadaDTO.getProductos().get(0).getProductoId());
        assertEquals(ventas.get(0).getCliente().getDni(), ventaEliminadaDTO.getCliente().getDni());
        assertEquals(ventas.get(0).getCliente().getNombre(), ventaEliminadaDTO.getCliente().getNombre());
        verify(ventaRepository).delete(any(Venta.class));
    }

    @Test
    void updateVenta() {
        List<Producto> productos = new ArrayList<>();
        productos.add(new Producto(1L, "Producto 1","Marca 1", 100.0, 10.0));
        Cliente clienteDb = new Cliente(1L, "Juan", "Perez", "36158155");
        Venta ventaDb = new Venta(1L, LocalDate.now(), 1000.0, productos, clienteDb);

        Cliente clienteNuevo = new Cliente(2L, "Marcelo", "Gallardo", "1592663");

        when(ventaRepository.findById(1L)).thenReturn(Optional.of(ventaDb));
        when(clienteService.getClienteById(any())).thenReturn(clienteNuevo);

        VentaResponseDTO ventaDTO = null;

        ventaDTO = ventaService.updateVenta(1L, new UpdateVentaDTO(LocalDate.now(), null, 1L));

        assertNotNull(ventaDTO);
        assertEquals(ventaDb.getVentaId(), ventaDTO.getVentaId());
        assertEquals(ventaDb.getFechaVenta(), ventaDTO.getFechaVenta());
        assertEquals(1000, ventaDTO.getTotal());
        verify(ventaRepository).save(any(Venta.class));
    }
}