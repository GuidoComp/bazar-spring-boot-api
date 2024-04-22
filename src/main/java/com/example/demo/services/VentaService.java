package com.example.demo.services;

import com.example.demo.dtos.requestDTOs.ventaDTOs.AddVentaDTO;
import com.example.demo.dtos.requestDTOs.ventaDTOs.UpdateVentaDTO;
import com.example.demo.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
import com.example.demo.dtos.responseDTOs.ventaDTOs.InfoMayorVenta;
import com.example.demo.dtos.responseDTOs.ventaDTOs.MontoYCantidadTotalDTO;
import com.example.demo.dtos.responseDTOs.ventaDTOs.VentaResponseDTO;
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
    private final GenericModelMapper modelMapper = GenericModelMapper.getModelMapper();

    public VentaService(IVentaRepository ventaRepository, IClienteService clienteService, IProductoService productoService) {
        this.ventaRepository = ventaRepository;
        this.clienteService = clienteService;
        this.productoService = productoService;
    }

    @Override
    public List<VentaResponseDTO> getVentas() {
        return modelMapper.mapVentasToDTO(ventaRepository.findAll());
    }

    @Override
    @Transactional
    public VentaResponseDTO addVenta(AddVentaDTO addVentaDTO) throws NoStockException, ResourceNotFoundException {
        Venta venta = modelMapper.mapAddVentaDTOToVenta(addVentaDTO);

        this.agregarCliente(addVentaDTO.getIdCliente(), venta);
        this.agregarProductos(addVentaDTO.getIdsProductos(), venta);

        Venta ventaDb = this.ventaRepository.save(venta);
        return modelMapper.mapVentaToDTO(ventaDb);
    }

    private void agregarCliente(Long idCliente, Venta venta) throws ResourceNotFoundException {
        Cliente nuevoCliente = clienteService.getClienteById(idCliente);
        Cliente clienteAnterior = venta.getCliente();

        if (clienteAnterior != null) {
            clienteAnterior.borrarVenta(venta);
            clienteService.save(clienteAnterior);
        }
        nuevoCliente.agregarVenta(venta);
        clienteService.save(nuevoCliente);

        venta.agregarCliente(nuevoCliente);
    }

    private void agregarProductos(List<Long> idsProductos, Venta venta) throws ResourceNotFoundException, NoStockException{
        List<Producto> productos = productoService.getProductosByIds(idsProductos);
        double monto = 0;
        for(Producto p: productos) {
            venta.agregarProducto(p);
            monto += p.getCosto();
        }
        venta.setTotal(monto);
    }

    @Override
    @Transactional
    public VentaResponseDTO deleteVenta(Long id) {
        Venta venta = ventaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMsgs.VENTA_NOT_FOUND, id)));
        venta.borrarProductos();
        ventaRepository.delete(venta);

        return modelMapper.mapVentaToDTO(venta);
    }

    @Override
    public VentaResponseDTO updateVenta(Long id, UpdateVentaDTO updateVentaDTO) {
        Venta venta = this.ventaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMsgs.VENTA_NOT_FOUND, id)));

        if (updateVentaDTO.getFechaVenta() != null) {
            venta.setFechaVenta(updateVentaDTO.getFechaVenta());
        }
        if (updateVentaDTO.getIdsProductos() != null) {
            this.actualizarProductos(updateVentaDTO.getIdsProductos(), venta);
        }
        if (updateVentaDTO.getIdCliente() != null) {
            this.agregarCliente(updateVentaDTO.getIdCliente(), venta);
        }

        this.ventaRepository.save(venta);
        return modelMapper.mapVentaToDTO(venta);
    }

    private void actualizarProductos(List<Long> idsProductos, Venta venta) throws ResourceNotFoundException {
        venta.borrarProductos();
        this.agregarProductos(idsProductos, venta);
    }

    @Override
    public List<ProductoResponseDTO> getProductosDTODeVenta(Long idVenta) throws ResourceNotFoundException {
        List<ProductoResponseDTO> productosDTO;
        Venta venta = this.getVentaById(idVenta);
        productosDTO = this.modelMapper.mapProductosToDTO(venta.getProductos());
        return productosDTO;
    }

    @Override
    public Venta getVentaById(Long idVenta) throws ResourceNotFoundException {
        return this.ventaRepository.findById(idVenta).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMsgs.VENTA_NOT_FOUND, idVenta)));
    }

    @Override
    public MontoYCantidadTotalDTO getMontoYCantidadTotales(LocalDate fechaVenta) throws ResourceNotFoundException {
        List<Venta> ventasOfDate = this.getVentasByDate(fechaVenta);
        double monto = 0;
        for (Venta ventas: ventasOfDate) {
            monto += ventas.getTotal();
        }
        return new MontoYCantidadTotalDTO(fechaVenta, monto, ventasOfDate.size());
    }

    @Override
    public List<Venta> getVentasByDate(LocalDate fecha) throws ResourceNotFoundException {
        List<Venta> ventas = this.ventaRepository.findAll();
        List<Venta> ventasOfDate = new LinkedList<>();

        for (Venta venta: ventas) {
            if (venta.getFechaVenta().equals(fecha)) {
                ventasOfDate.add(venta);
            }
        }
        if (ventasOfDate.isEmpty()) throw new ResourceNotFoundException(String.format(ErrorMsgs.NO_SALES_FOR_THAT_DATE, fecha));
        return ventasOfDate;
    }

    @Override
    public InfoMayorVenta getInfoMayorVenta() throws ResourceNotFoundException {
        double montoMayor = 0;
        List<Venta> ventas = this.ventaRepository.findAll();
        if (ventas.isEmpty()) throw new ResourceNotFoundException(ErrorMsgs.NO_SALES_YET);
        Venta ventaMayor = new Venta();

        for(Venta venta: ventas) {
            if (venta.getTotal() > montoMayor) {
                montoMayor = venta.getTotal();
                ventaMayor = venta;
            }
        }
        return new InfoMayorVenta(ventaMayor.getVentaId(), ventaMayor.getTotal(), ventaMayor.getProductos().size(), ventaMayor.getCliente().getNombre(), ventaMayor.getCliente().getApellido());
    }
}
