package com.gcompagno.ecommerce.services;

import com.gcompagno.ecommerce.dtos.requestDTOs.clienteDTOs.AddClienteDTO;
import com.gcompagno.ecommerce.dtos.requestDTOs.clienteDTOs.UpdateClienteDTO;
import com.gcompagno.ecommerce.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.gcompagno.ecommerce.models.Cliente;
import com.gcompagno.ecommerce.models.Venta;

import java.util.List;
import java.util.Optional;

public interface IClienteService {
    List<ClienteResponseDTO> getClientes();

    ClienteResponseDTO addCliente(AddClienteDTO addClienteDTO);

    ClienteResponseDTO deleteCliente(Long id);

    ClienteResponseDTO updateCliente(Long id, UpdateClienteDTO updateClienteDTO);

    Cliente getClienteById(Long idCliente);

    Optional<Cliente> getClienteByDni(String dni);

    void deleteAllClientes();

    void agregarVenta(Cliente nuevoCliente, Venta venta);
}
