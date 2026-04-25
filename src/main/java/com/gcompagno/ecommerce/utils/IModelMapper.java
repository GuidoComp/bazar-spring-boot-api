package com.gcompagno.ecommerce.utils;

import com.gcompagno.ecommerce.dtos.requestDTOs.clienteDTOs.AddClienteDTO;
import com.gcompagno.ecommerce.dtos.requestDTOs.clienteDTOs.UpdateClienteDTO;
import com.gcompagno.ecommerce.dtos.requestDTOs.productoDTOs.AddProductoDTO;
import com.gcompagno.ecommerce.dtos.requestDTOs.ventaDTOs.AddVentaDTO;
import com.gcompagno.ecommerce.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.gcompagno.ecommerce.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
import com.gcompagno.ecommerce.dtos.responseDTOs.ventaDTOs.VentaResponseDTO;
import com.gcompagno.ecommerce.models.Cliente;
import com.gcompagno.ecommerce.models.Producto;
import com.gcompagno.ecommerce.models.Venta;

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
