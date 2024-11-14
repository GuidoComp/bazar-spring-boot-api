package com.example.demo.utils;

import com.example.demo.dtos.requestDTOs.clienteDTOs.AddClienteDTO;
import com.example.demo.dtos.requestDTOs.productoDTOs.AddProductoDTO;
import com.example.demo.dtos.requestDTOs.ventaDTOs.AddVentaDTO;
import com.example.demo.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.example.demo.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
import com.example.demo.dtos.responseDTOs.ventaDTOs.VentaResponseDTO;
import com.example.demo.models.Cliente;
import com.example.demo.models.Producto;
import com.example.demo.models.Venta;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class GenericModelMapper implements IModelMapper {
    private final ModelMapper mapper;

    public GenericModelMapper(ModelMapper modelMapper) {
        this.mapper = modelMapper;
    }

    @Override
    public ProductoResponseDTO mapProductoToDTO(Producto producto) {
        ProductoResponseDTO productoResponseDTO = null;
        if (producto != null) {
            productoResponseDTO = mapper.map(producto, ProductoResponseDTO.class);
        }
        return productoResponseDTO;
    }

    @Override
    public Producto mapDTOToProducto(AddProductoDTO addProductoDTO) {
        Producto p = null;
        if (addProductoDTO != null) {
            p = mapper.map(addProductoDTO, Producto.class);
        }
        return p;
    }

    @Override
    public List<ClienteResponseDTO> mapClientesToDTO(List<Cliente> allClients) {
        return allClients.stream()
                .map(cli -> mapper.map(cli, ClienteResponseDTO.class))
                .toList();
    }

    @Override
    public Cliente mapAddClienteDTOToCliente(AddClienteDTO addClienteDTO) {
        Cliente cliente = null;

        if (addClienteDTO != null) {
            cliente = mapper.map(addClienteDTO, Cliente.class);
        }
        return cliente;
    }

    @Override
    public ClienteResponseDTO mapClienteToDTO(Cliente cliente) {
        return mapper.map(cliente, ClienteResponseDTO.class);
    }

    @Override
    public List<VentaResponseDTO> mapVentasToDTO(List<Venta> allVentas) {
        List<VentaResponseDTO> ventasDTO = new ArrayList<>();

        for (Venta venta : allVentas) {
            ventasDTO.add(mapper.map(venta, VentaResponseDTO.class));
        }
        return ventasDTO;
    }

    @Override
    public Venta mapAddVentaDTOToVenta(AddVentaDTO addVentaDTO) {
        Venta venta = new Venta();

        if (addVentaDTO != null) {
            venta.setFechaVenta(addVentaDTO.getFechaVenta());
//            venta = this.modelMapper.map(addVentaDTO, Venta.class);
        }
        return venta;
    }

    @Override
    public VentaResponseDTO mapVentaToDTO(Venta ventaDb) {
        List<ProductoResponseDTO> productosVenta = new ArrayList<>();
        if (ventaDb.getProductos() != null) {
            for (Producto producto : ventaDb.getProductos()) {
                productosVenta.add(mapper.map(producto, ProductoResponseDTO.class));
            }
        }

        VentaResponseDTO ventaResponseDTO = new VentaResponseDTO();
        ventaResponseDTO.setVentaId(ventaDb.getVentaId());
        ventaResponseDTO.setFechaVenta(ventaDb.getFechaVenta());
        ventaResponseDTO.setTotal(ventaDb.getTotal());
        ventaResponseDTO.setProductos(productosVenta);
        if (ventaDb.getCliente() != null) {
            ventaResponseDTO.setCliente(mapper.map(ventaDb.getCliente(), ClienteResponseDTO.class));
        }
        return ventaDb.getProductos() == null && ventaDb.getCliente() == null ? mapper.map(ventaDb, VentaResponseDTO.class) : ventaResponseDTO;
    }

    @Override
    public List<ProductoResponseDTO> mapProductosToDTO(List<Producto> productos) {
        List<ProductoResponseDTO> productosDTO = new LinkedList<>();
        for (Producto p: productos) {
            productosDTO.add(this.mapper.map(p, ProductoResponseDTO.class));
        }
        return productosDTO;
    }
}
