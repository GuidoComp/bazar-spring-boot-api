package com.example.demo.services;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Service
public class VentaService implements IVentaService {
    private final IVentaRepository ventaRepository;
    private final IClienteService clienteService;
    private final IProductoService productoService;
//    private final GenericModelMapper modelMapper = GenericModelMapper.getModelMapper();

    public VentaService(IVentaRepository ventaRepository, IClienteService clienteService, IProductoService productoService) {
        this.ventaRepository = ventaRepository;
        this.clienteService = clienteService;
        this.productoService = productoService;
    }

    @Override
    public List<VentaResponseDTO> getVentas() {
        return List.of();
    }

    @Override
    public VentaResponseDTO addVenta(AddVentaDTO addVentaDTO) {
        return null;
    }

    @Override
    public VentaResponseDTO deleteVenta(Long id) {
        return null;
    }

    @Override
    public VentaResponseDTO updateVenta(Long id, UpdateVentaDTO updateVentaDTO) {
        return null;
    }

    @Override
    public List<ProductoResponseDTO> getProductosDTODeVenta(Long idVenta) {
        return List.of();
    }

    @Override
    public Venta getVentaById(Long idVenta) {
        return null;
    }

    @Override
    public MontoYCantidadTotalDTO getMontoYCantidadTotales(LocalDate fechaVenta) {
        return null;
    }

    @Override
    public List<Venta> getVentasByDate(LocalDate fecha) {
        return List.of();
    }

    @Override
    public InfoMayorVenta getInfoMayorVenta() {
        return null;
    }

//    @Override
//    public List<VentaResponseDTO> getVentas() {
//        List<Venta> ventas = ventaRepository.findAll();
//        return modelMapper.mapVentasToDTO(ventas);
//    }
//
//    @Override
//    @Transactional
//    public VentaResponseDTO addVenta(AddVentaDTO addVentaDTO) {
//        Venta venta = modelMapper.mapAddVentaDTOToVenta(addVentaDTO);
//
//        agregarCliente(addVentaDTO.getIdCliente(), venta);
//        agregarProductos(addVentaDTO.getIdsProductos(), venta);
//
//        Venta ventaDb = ventaRepository.save(venta);
//        return modelMapper.mapVentaToDTO(ventaDb);
//    }
//
//    private void agregarCliente(Long idCliente, Venta venta) {
//        Cliente nuevoCliente = clienteService.getClienteById(idCliente);
//        Cliente clienteAnterior = venta.getCliente();
//
//        if (clienteAnterior != null) {
//            clienteAnterior.borrarVenta(venta);
//            clienteService.save(clienteAnterior);
//        }
//        nuevoCliente.agregarVenta(venta);
//        clienteService.save(nuevoCliente);
//
//        venta.agregarCliente(nuevoCliente);
//    }
//
//    private void agregarProductos(List<Long> idsProductos, Venta venta) {
//        List<Producto> productos = productoService.getProductosByIds(idsProductos);
//        double monto = 0;
//        for(Producto p: productos) {
//            productoService.checkStock(p);
//            venta.agregarProducto(p);
//            monto += p.getCosto();
//        }
//        venta.setTotal(monto);
//    }
//
//    @Override
//    @Transactional
//    public VentaResponseDTO deleteVenta(Long id) {
//        Venta venta = ventaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMsgs.VENTA_NOT_FOUND, id)));
//        venta.borrarProductos();
//        ventaRepository.delete(venta);
//
//        return modelMapper.mapVentaToDTO(venta);
//    }
//
//    @Override
//    public VentaResponseDTO updateVenta(Long id, UpdateVentaDTO updateVentaDTO) {
//        Venta venta = ventaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMsgs.VENTA_NOT_FOUND, id)));
//
//        LocalDate fechaVenta = updateVentaDTO.getFechaVenta();
//        if (fechaVenta != null) {
//            venta.setFechaVenta(fechaVenta);
//        }
//        List<Long> idsProductos = updateVentaDTO.getIdsProductos();
//        if (idsProductos != null) {
//            actualizarProductos(idsProductos, venta);
//        }
//        Long idCliente = updateVentaDTO.getIdCliente();
//        if (idCliente != null) {
//            agregarCliente(idCliente, venta);
//        }
//        ventaRepository.save(venta);
//        return modelMapper.mapVentaToDTO(venta);
//    }
//
//    private void actualizarProductos(List<Long> idsProductosNuevos, Venta venta) {
//        checkProductos(idsProductosNuevos, venta);
//        venta.borrarProductos();
//        agregarProductos(idsProductosNuevos, venta);
//    }
//
//    private void checkProductos(List<Long> idsProductos, Venta venta) {
//        List<Long> idsProductosVentaDb = getIdsDeProductos(venta);
//        if (idsProductosVentaDb.equals(idsProductos)) {
//            throw new EqualProductsIds(ErrorMsgs.UPDATE_PRODUCTOS_NO_PERMITIDO);
//        }
//    }
//
//    private List<Long> getIdsDeProductos(Venta venta) {
//        List<Long> idsProductos = new LinkedList<>();
//        for(Producto producto: venta.getProductos()) {
//            idsProductos.add(producto.getProductoId());
//        }
//        return idsProductos;
//    }
//
//    @Override
//    public List<ProductoResponseDTO> getProductosDTODeVenta(Long idVenta) {
//        List<ProductoResponseDTO> productosDTO;
//        Venta venta = getVentaById(idVenta);
//        productosDTO = modelMapper.mapProductosToDTO(venta.getProductos());
//        return productosDTO;
//    }
//
//    @Override
//    public Venta getVentaById(Long idVenta)  {
//        return ventaRepository.findById(idVenta).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMsgs.VENTA_NOT_FOUND, idVenta)));
//    }
//
//    @Override
//    public MontoYCantidadTotalDTO getMontoYCantidadTotales(LocalDate fechaVenta) {
//        List<Venta> ventasOfDate = getVentasByDate(fechaVenta);
//        double monto = 0;
//        for (Venta ventas: ventasOfDate) {
//            monto += ventas.getTotal();
//        }
//        return new MontoYCantidadTotalDTO(fechaVenta, monto, ventasOfDate.size());
//    }
//
//    @Override
//    public List<Venta> getVentasByDate(LocalDate fecha) {
//        List<Venta> ventas = ventaRepository.findAll();
//        List<Venta> ventasOfDate = new LinkedList<>();
//
//        for (Venta venta: ventas) {
//            if (venta.getFechaVenta().equals(fecha)) {
//                ventasOfDate.add(venta);
//            }
//        }
//        if (ventasOfDate.isEmpty()) throw new ResourceNotFoundException(String.format(ErrorMsgs.NO_SALES_FOR_THAT_DATE, fecha));
//        return ventasOfDate;
//    }
//
//    @Override
//    public InfoMayorVenta getInfoMayorVenta() {
//        double montoMayor = 0;
//        List<Venta> ventas = ventaRepository.findAll();
//        if (ventas.isEmpty()) throw new ResourceNotFoundException(ErrorMsgs.NO_SALES_YET);
//        Venta ventaMayor = new Venta();
//
//        for(Venta venta: ventas) {
//            if (venta.getTotal() > montoMayor) {
//                montoMayor = venta.getTotal();
//                ventaMayor = venta;
//            }
//        }
//        return new InfoMayorVenta(ventaMayor.getVentaId(), ventaMayor.getTotal(), ventaMayor.getProductos().size(), ventaMayor.getCliente().getNombre(), ventaMayor.getCliente().getApellido());
//    }
}
