package com.example.demo.services;

import com.example.demo.dtos.requestDTOs.clienteDTOs.AddClienteDTO;
import com.example.demo.dtos.requestDTOs.clienteDTOs.UpdateClienteDTO;
import com.example.demo.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.example.demo.models.Cliente;
import com.example.demo.models.Venta;

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
