package com.example.demo.services;

import com.example.demo.dtos.requestDTOs.productoDTOs.UpdateProductoDTO;
import com.example.demo.dtos.requestDTOs.ventaDTOs.AddVentaDTO;
import com.example.demo.dtos.requestDTOs.ventaDTOs.UpdateVentaDTO;
import com.example.demo.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
import com.example.demo.dtos.responseDTOs.ventaDTOs.InfoMayorVenta;
import com.example.demo.dtos.responseDTOs.ventaDTOs.MontoYCantidadTotalDTO;
import com.example.demo.dtos.responseDTOs.ventaDTOs.VentaResponseDTO;
import com.example.demo.exceptions.EqualProductsIds;
import com.example.demo.exceptions.NoStockException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Cliente;
import com.example.demo.models.Producto;
import com.example.demo.models.Venta;
import com.example.demo.repositories.IVentaRepository;
import com.example.demo.utils.ErrorMsgs;
import com.example.demo.utils.GenericModelMapper;
import com.example.demo.utils.IModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService implements IVentaService {
    private final IVentaRepository ventaRepository;
    private final IClienteService clienteService;
    private final IProductoService productoService;
    private final IModelMapper mapper;

    public VentaService(IVentaRepository ventaRepository, IClienteService clienteService, IProductoService productoService, IModelMapper mapper) {
        this.ventaRepository = ventaRepository;
        this.clienteService = clienteService;
        this.productoService = productoService;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> getVentas() {
        List<Venta> ventas = ventaRepository.findAll();
        return mapper.mapVentasToDTO(ventas);
    }

    @Override
    @Transactional
    public VentaResponseDTO addVenta(AddVentaDTO addVentaDTO) {
        Venta venta = new Venta();
        venta.setFechaVenta(addVentaDTO.getFechaVenta());
        this.agregarProductos(addVentaDTO.getIdsProductos(), venta);
        this.agregarCliente(addVentaDTO.getIdCliente(), venta);

        ventaRepository.save(venta);

        return mapper.mapVentaToDTO(venta);
    }

    private void agregarCliente(Long idCliente, Venta venta) {
        Cliente cliente = clienteService.getClienteById(idCliente);
        venta.setCliente(cliente);
    }

    private void agregarProductos(List<Long> idsProductos, Venta venta) {
        List<Producto> productos = productoService.getProductosByIds(idsProductos);
        productos.stream()
                .forEach(p -> {
                    productoService.checkStock(p);
                    this.agregarProducto(p, venta);
                });

        double total = productos.stream()
                .mapToDouble(Producto::getCosto)
                .sum();
        venta.setTotal(total);
    }

    private void agregarProducto(Producto p, Venta venta) {
        productoService.updateProducto(p.getProductoId(), new UpdateProductoDTO(null, null, null, p.getCantidadDisponible() - 1));
        venta.getProductos().add(p); // Al agregar el producto a la venta (con el ventaRepository.save(venta)), por la cascada se agrega la venta al producto
    }

    @Override
    @Transactional
    public VentaResponseDTO deleteVenta(Long id) {
        Venta venta = ventaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMsgs.VENTA_NOT_FOUND, id)));
        this.borrarProductos(venta);
        ventaRepository.delete(venta);
        return mapper.mapVentaToDTO(venta);
    }

    @Override
    @Transactional
    public VentaResponseDTO updateVenta(Long id, UpdateVentaDTO updateVentaDTO) {
        Venta venta = ventaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMsgs.VENTA_NOT_FOUND, id)));

        LocalDate fechaVenta = updateVentaDTO.getFechaVenta();
        if (fechaVenta != null) {
            venta.setFechaVenta(fechaVenta);
        }
        List<Long> idsProductos = updateVentaDTO.getIdsProductos();
        if (idsProductos != null) {
            actualizarProductos(idsProductos, venta);
        }
        Long idCliente = updateVentaDTO.getIdCliente();
        if (idCliente != null) {
            agregarCliente(idCliente, venta);
        }
        ventaRepository.save(venta);
        return mapper.mapVentaToDTO(venta);
    }

    @Override
    public void borrarProductos(Venta venta) {
        venta.getProductos().forEach(p -> {
            productoService.updateProducto(p.getProductoId(), new UpdateProductoDTO(null, null, null, p.getCantidadDisponible() + 1));
        });
        venta.setTotal(0.0);
        venta.setProductos(new LinkedList<>());
    }

    private void actualizarProductos(List<Long> idsProductosNuevos, Venta venta) {
        this.checkProductos(idsProductosNuevos, venta);
        this.borrarProductos(venta);
        this.agregarProductos(idsProductosNuevos, venta);
    }

    private void checkProductos(List<Long> idsProductos, Venta venta) {
        List<Long> idsProductosVentaDb = this.getIdsDeProductos(venta);
        if (idsProductosVentaDb.equals(idsProductos)) {
            throw new EqualProductsIds(ErrorMsgs.UPDATE_PRODUCTOS_NO_PERMITIDO);
        }
    }

    private List<Long> getIdsDeProductos(Venta venta) {
        return venta.getProductos().stream()
                .map(Producto::getProductoId)
                .toList();
    }

    @Override
    public List<ProductoResponseDTO> getProductosDTODeVenta(Long idVenta) {
        return mapper.mapProductosToDTO(getVentaById(idVenta).getProductos());
    }

    @Override
    @Transactional(readOnly = true)
    public Venta getVentaById(Long idVenta)  {
        return ventaRepository.findById(idVenta).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMsgs.VENTA_NOT_FOUND, idVenta)));
    }

    @Override
    public MontoYCantidadTotalDTO getMontoYCantidadTotales(LocalDate fechaVenta) {
        List<Venta> ventasOfDate = getVentasByDate(fechaVenta);
        double monto = ventasOfDate.stream()
                .mapToDouble(Venta::getTotal)
                .sum();
        return new MontoYCantidadTotalDTO(fechaVenta, monto, ventasOfDate.size());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> getVentasByDate(LocalDate fecha) {
        List<Venta> ventasOfDate = ventaRepository.findByFechaVenta(fecha);
        if (ventasOfDate.isEmpty()) throw new ResourceNotFoundException(String.format(ErrorMsgs.NO_SALES_FOR_THAT_DATE, fecha));
        return ventasOfDate;
    }

    @Override
    @Transactional(readOnly = true)
    public InfoMayorVenta getInfoMayorVenta() {
        List<Venta> ventas = ventaRepository.findAll();
        if (ventas.isEmpty()) throw new ResourceNotFoundException(ErrorMsgs.NO_SALES_YET);
        Venta ventaMayor = ventas.stream()
                .max(Comparator.comparingDouble(Venta::getTotal))
                .get();

        return new InfoMayorVenta(
                ventaMayor.getVentaId(),
                ventaMayor.getTotal(),
                ventaMayor.getProductos().size(),
                ventaMayor.getCliente().getNombre(),
                ventaMayor.getCliente().getApellido()
        );
    }
}
