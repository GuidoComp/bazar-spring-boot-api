package com.example.demo.services;

import com.example.demo.dtos.requestDTOs.ventaDTOs.AddVentaDTO;
import com.example.demo.dtos.requestDTOs.ventaDTOs.UpdateVentaDTO;
import com.example.demo.dtos.responseDTOs.ventaDTOs.VentaResponseDTO;
import com.example.demo.models.Cliente;
import com.example.demo.models.Producto;
import com.example.demo.models.Venta;
import com.example.demo.repositories.IVentaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    private IClienteService clienteService;

    @Mock
    private IProductoService productoService;

    @Mock
    private IVentaRepository ventaRepository;

    @InjectMocks
    private VentaService ventaService;

//    @InjectMocks
//    private GenericModelMapper modelMapper = GenericModelMapper.getModelMapper();

    @Test
    void getVentas() {
        List<Venta> ventas = new ArrayList<>();
        List<Producto> productos = new ArrayList<>();
        productos.add(new Producto(1L, "Producto 1","Marca 1", 100.0, 10.0, null));
        Cliente cliente = new Cliente(1L, "Juan", "Perez", "36158155", null);

        ventas.add(new Venta(1L, LocalDate.now(), 1000.0, productos, cliente));

        when(ventaRepository.findAll()).thenReturn(ventas);

        List<VentaResponseDTO> ventasDTO = ventaService.getVentas();

        assertNotNull(ventasDTO);
        assertEquals(ventas.get(0).getVentaId(), ventasDTO.get(0).getVentaId());
        assertEquals(ventas.get(0).getFechaVenta(), ventasDTO.get(0).getFechaVenta());
        assertEquals(ventas.get(0).getTotal(), ventasDTO.get(0).getTotal());
        assertEquals(ventas.get(0).getProductos().get(0).getProductoId(), ventasDTO.get(0).getProductos().get(0).getId());
        assertEquals(ventas.get(0).getCliente().getDni(), ventasDTO.get(0).getCliente().getDni());
        assertEquals(ventas.get(0).getCliente().getNombre(), ventasDTO.get(0).getCliente().getNombre());
    }

    @Test
    void addVenta() {
        //Given
        //preparamos el addVentaDTO
        List<Long> idsProductos = new ArrayList<>();
        idsProductos.add(1L);
        idsProductos.add(2L);

        Long idCliente = 1L;

        AddVentaDTO addVentaDTO = new AddVentaDTO(LocalDate.now(), 15263.0, idsProductos, idCliente);

        //preparamos el cliente y los productos que se van a agregar a la venta
        List<Producto> productos = new ArrayList<>();
        productos.add(new Producto(1L, "Producto 1","Marca 1", 100.0, 10.0, null));
        productos.add(new Producto(2L, "Producto 2","Marca 2", 200.0, 20.0, null));
        Cliente cliente = new Cliente(1L, "Juan", "Perez", "36158155", null);

        Venta ventaEntity = new Venta(1L, LocalDate.now(), 15263.0, productos, cliente);
        when(ventaRepository.save(any(Venta.class))).thenReturn(ventaEntity);
        when(clienteService.getClienteById(any(Long.class))).thenReturn(cliente);
        when(productoService.getProductosByIds(any(List.class))).thenReturn(productos);

        //When
        VentaResponseDTO ventaAgregadaDTO = ventaService.addVenta(addVentaDTO);

        //Then
        assertNotNull(ventaAgregadaDTO);
        assertEquals(addVentaDTO.getFechaVenta(), ventaAgregadaDTO.getFechaVenta());
        assertEquals(addVentaDTO.getTotal(), ventaAgregadaDTO.getTotal());
        assertEquals(addVentaDTO.getIdsProductos().get(0), ventaAgregadaDTO.getProductos().get(0).getId());
        assertEquals(addVentaDTO.getIdCliente(), ventaAgregadaDTO.getCliente().getClienteId());
        verify(ventaRepository).save(any(Venta.class));
        verify(clienteService).getClienteById(idCliente);
        verify(productoService).getProductosByIds(idsProductos);
    }

    @Test
    void deleteVenta() {
        List<Venta> ventas = new ArrayList<>();
        List<Producto> productos = new ArrayList<>();
        productos.add(new Producto(1L, "Producto 1","Marca 1", 100.0, 10.0, null));
        Cliente cliente = new Cliente(1L, "Juan", "Perez", "36158155", null);

        ventas.add(new Venta(1L, LocalDate.now(), 1000.0, productos, cliente));

        when(ventaRepository.findById(1L)).thenReturn(Optional.of(ventas.get(0)));

        VentaResponseDTO ventaEliminadaDTO = ventaService.deleteVenta(1L);

        assertNotNull(ventaEliminadaDTO);
        assertEquals(ventas.get(0).getVentaId(), ventaEliminadaDTO.getVentaId());
        assertEquals(ventas.get(0).getFechaVenta(), ventaEliminadaDTO.getFechaVenta());
        assertEquals(ventas.get(0).getTotal(), ventaEliminadaDTO.getTotal());
        assertEquals(ventas.get(0).getProductos().get(0).getProductoId(), ventaEliminadaDTO.getProductos().get(0).getId());
        assertEquals(ventas.get(0).getCliente().getDni(), ventaEliminadaDTO.getCliente().getDni());
        assertEquals(ventas.get(0).getCliente().getNombre(), ventaEliminadaDTO.getCliente().getNombre());
        verify(ventaRepository).delete(any(Venta.class));
    }

    @Test
    void updateVenta() {
        List<Producto> productos = new ArrayList<>();
        productos.add(new Producto(1L, "Producto 1","Marca 1", 100.0, 10.0, null));
        Cliente cliente = new Cliente(1L, "Juan", "Perez", "36158155", null);

        Venta venta = new Venta(1L, LocalDate.now(), 1000.0, productos, cliente);
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));

        VentaResponseDTO ventaDTO = null;

        ventaDTO = ventaService.updateVenta(1L, new UpdateVentaDTO(LocalDate.now(), 1500.0));

        assertNotNull(ventaDTO);
        assertEquals(venta.getVentaId(), ventaDTO.getVentaId());
        assertEquals(venta.getFechaVenta(), ventaDTO.getFechaVenta());
        assertEquals(1500.0, ventaDTO.getTotal());
        verify(ventaRepository).save(any(Venta.class));
    }
}