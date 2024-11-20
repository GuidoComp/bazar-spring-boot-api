package com.example.demo.utils;

import com.example.demo.dtos.requestDTOs.clienteDTOs.AddClienteDTO;
import com.example.demo.dtos.requestDTOs.clienteDTOs.UpdateClienteDTO;
import com.example.demo.dtos.requestDTOs.productoDTOs.AddProductoDTO;
import com.example.demo.dtos.requestDTOs.ventaDTOs.AddVentaDTO;
import com.example.demo.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.example.demo.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
import com.example.demo.dtos.responseDTOs.ventaDTOs.VentaResponseDTO;
import com.example.demo.models.Cliente;
import com.example.demo.models.Producto;
import com.example.demo.models.Venta;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenericModelMapper implements IModelMapper {
    private final ModelMapper mapper;

    public GenericModelMapper(ModelMapper modelMapper) {
        this.mapper = modelMapper;
    }

    @Override
    public ProductoResponseDTO mapProductoToDTO(Producto producto) {
        checkNull(producto);
        return mapper.map(producto, ProductoResponseDTO.class);
    }

    @Override
    public Producto mapDTOToProducto(AddProductoDTO addProductoDTO) {
        checkNull(addProductoDTO);
        return mapper.map(addProductoDTO, Producto.class);
    }

    @Override
    public List<ClienteResponseDTO> mapClientesToDTO(List<Cliente> allClients) {
        checkNull(allClients);
        return allClients.stream()
                .map(cli -> mapper.map(cli, ClienteResponseDTO.class))
                .toList();
    }

    @Override
    public Cliente mapAddClienteDTOToCliente(AddClienteDTO addClienteDTO) {
        checkNull(addClienteDTO);
        return mapper.map(addClienteDTO, Cliente.class);
    }

    @Override
    public ClienteResponseDTO mapClienteToDTO(Cliente cliente) {
        checkNull(cliente);
        return mapper.map(cliente, ClienteResponseDTO.class);
    }

    private static <T> void checkNull(T object) {
        if (object == null) {
            throw new IllegalArgumentException(String.format(ErrorMsgs.PARAMETRO_NULO));
        }
    }

    @Override
    public List<VentaResponseDTO> mapVentasToDTO(List<Venta> allVentas) {
        checkNull(allVentas);
        return allVentas.stream()
                .map(venta -> mapper.map(venta, VentaResponseDTO.class))
                .toList();
    }

    @Override
    public Venta mapAddVentaDTOToVenta(AddVentaDTO addVentaDTO) {
        checkNull(addVentaDTO);
        TypeMap<AddVentaDTO, Venta> typeMap = mapper.createTypeMap(AddVentaDTO.class, Venta.class);
        typeMap.addMapping(AddVentaDTO::getFechaVenta, Venta::setFechaVenta);
        return mapper.map(addVentaDTO, Venta.class);
    }

    @Override
    public VentaResponseDTO mapVentaToDTO(Venta ventaDb) {
        checkNull(ventaDb);
        mapper.typeMap(Venta.class, VentaResponseDTO.class).addMappings(mapper -> {
            mapper.map(Venta::getProductos, VentaResponseDTO::setProductos);
            mapper.map(Venta::getCliente, VentaResponseDTO::setCliente);
        });

        VentaResponseDTO ventaResponseDTO = mapper.map(ventaDb, VentaResponseDTO.class);

        return ventaResponseDTO;
    }

    @Override
    public List<ProductoResponseDTO> mapProductosToDTO(List<Producto> productos) {
        checkNull(productos);
        return productos.stream()
                .map(producto -> mapper.map(producto, ProductoResponseDTO.class))
                .toList();
    }
}
