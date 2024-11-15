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

import java.util.List;

public interface IModelMapper {
    ProductoResponseDTO mapProductoToDTO(Producto producto);
    Producto mapDTOToProducto(AddProductoDTO addProductoDTO);
    List<ClienteResponseDTO> mapClientesToDTO(List<Cliente> allClients);
    Cliente mapAddClienteDTOToCliente(AddClienteDTO addClienteDTO);
    ClienteResponseDTO mapClienteToDTO(Cliente cliente);
    List<VentaResponseDTO> mapVentasToDTO(List<Venta> allVentas);
    Venta mapAddVentaDTOToVenta(AddVentaDTO addVentaDTO);
    VentaResponseDTO mapVentaToDTO(Venta ventaDb);
    List<ProductoResponseDTO> mapProductosToDTO(List<Producto> productos);
}
