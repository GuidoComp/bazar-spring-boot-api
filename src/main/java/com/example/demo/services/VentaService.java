package com.example.demo.services;

import com.example.demo.dtos.requestDTOs.ventaDTOs.AddVentaDTO;
import com.example.demo.dtos.requestDTOs.ventaDTOs.UpdateVentaDTO;
import com.example.demo.dtos.responseDTOs.ventaDTOs.VentaResponseDTO;
import com.example.demo.exceptions.NoStockException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Producto;
import com.example.demo.models.Venta;
import com.example.demo.repositories.IVentaRepository;
import com.example.demo.utils.ErrorMsgs;
import com.example.demo.utils.GenericModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        this.agregarCliente(addVentaDTO, venta);
        this.agregarProductos(addVentaDTO, venta);

        Venta ventaDb = this.ventaRepository.save(venta);
        return modelMapper.mapVentaToDTO(ventaDb);
    }

    private void agregarCliente(AddVentaDTO addVentaDTO, Venta venta) throws ResourceNotFoundException {
        Long idCliente = addVentaDTO.getIdCliente();
        venta.agregarCliente(clienteService.getClienteById(idCliente));
    }

    private void agregarProductos(AddVentaDTO addVentaDTO, Venta venta) throws ResourceNotFoundException, NoStockException{
        List<Producto> productos = productoService.getProductosByIds(addVentaDTO.getIdsProductos());
        for(Producto p: productos) {
            venta.agregarProducto(p);
        }
    }

    @Override
    @Transactional
    public VentaResponseDTO deleteVenta(Long id) {
        Venta venta = ventaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMsgs.VENTA_NOT_FOUND, id)));
        ventaRepository.delete(venta);

        return modelMapper.mapVentaToDTO(venta);
    }

    @Override
    public VentaResponseDTO updateVenta(Long id, UpdateVentaDTO updateVentaDTO) {
        Venta venta = this.ventaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMsgs.VENTA_NOT_FOUND, id)));

        if (updateVentaDTO.getFechaVenta() != null) {
            venta.setFechaVenta(updateVentaDTO.getFechaVenta());
        }
        if (updateVentaDTO.getTotal() != null) {
            venta.setTotal(updateVentaDTO.getTotal());
        }

        this.ventaRepository.save(venta);
        return modelMapper.mapVentaToDTO(venta);
    }
}
