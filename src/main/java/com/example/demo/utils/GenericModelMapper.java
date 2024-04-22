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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GenericModelMapper {
    private final ModelMapper modelMapper;
    private static GenericModelMapper instance;

    private GenericModelMapper() {
        this.modelMapper = new ModelMapper();
    }

    public static GenericModelMapper getModelMapper() {
        if (instance == null) {
            instance = new GenericModelMapper();
        }
        return instance;
    }

    public ProductoResponseDTO mapProductoToDTO(Producto producto) {
        ProductoResponseDTO productoResponseDTO = null;
        if (producto != null) {
            productoResponseDTO = modelMapper.map(producto, ProductoResponseDTO.class);
        }
        return productoResponseDTO;
    }

    public Producto mapDTOToProducto(AddProductoDTO addProductoDTO) {
        Producto p = null;
        if (addProductoDTO != null) {
            p = modelMapper.map(addProductoDTO, Producto.class);
        }
        return p;
    }

    public List<ClienteResponseDTO> mapClientesToDTO(List<Cliente> allClients) {
        List<ClienteResponseDTO> clientesDTO = new ArrayList<>();

        for (Cliente cliente : allClients) {
            clientesDTO.add(modelMapper.map(cliente, ClienteResponseDTO.class));
        }
        return clientesDTO;
    }

    public Cliente mapAddClienteDTOToCliente(AddClienteDTO addClienteDTO) {
        Cliente cliente = null;

        if (addClienteDTO != null) {
            cliente = modelMapper.map(addClienteDTO, Cliente.class);
        }
        return cliente;
    }

    public ClienteResponseDTO mapClienteToDTO(Cliente cliente) {
        return modelMapper.map(cliente, ClienteResponseDTO.class);
    }

    public List<VentaResponseDTO> mapVentasToDTO(List<Venta> allVentas) {
        List<VentaResponseDTO> ventasDTO = new ArrayList<>();

        for (Venta venta : allVentas) {
            ventasDTO.add(modelMapper.map(venta, VentaResponseDTO.class));
        }
        return ventasDTO;
    }

    public Venta mapAddVentaDTOToVenta(AddVentaDTO addVentaDTO) {
        Venta venta = new Venta();

        if (addVentaDTO != null) {
            venta.setFechaVenta(addVentaDTO.getFechaVenta());
//            venta = this.modelMapper.map(addVentaDTO, Venta.class);
        }
        return venta;
    }

    public VentaResponseDTO mapVentaToDTO(Venta ventaDb) {
        List<ProductoResponseDTO> productosVenta = new ArrayList<>();
        if (ventaDb.getProductos() != null) {
            for (Producto producto : ventaDb.getProductos()) {
                productosVenta.add(modelMapper.map(producto, ProductoResponseDTO.class));
            }
        }

        VentaResponseDTO ventaResponseDTO = new VentaResponseDTO();
        ventaResponseDTO.setVentaId(ventaDb.getVentaId());
        ventaResponseDTO.setFechaVenta(ventaDb.getFechaVenta());
        ventaResponseDTO.setTotal(ventaDb.getTotal());
        ventaResponseDTO.setProductos(productosVenta);
        if (ventaDb.getCliente() != null) {
            ventaResponseDTO.setCliente(modelMapper.map(ventaDb.getCliente(), ClienteResponseDTO.class));
        }
        return ventaDb.getProductos() == null && ventaDb.getCliente() == null ? modelMapper.map(ventaDb, VentaResponseDTO.class) : ventaResponseDTO;
    }

    public List<ProductoResponseDTO> mapProductosToDTO(List<Producto> productos) {
        List<ProductoResponseDTO> productosDTO = new LinkedList<>();
        for (Producto p: productos) {
            productosDTO.add(this.modelMapper.map(p, ProductoResponseDTO.class));
        }
        return productosDTO;
    }
}
