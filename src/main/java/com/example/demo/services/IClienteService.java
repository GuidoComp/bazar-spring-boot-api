package com.example.demo.services;

import com.example.demo.dtos.requestDTOs.clienteDTOs.AddClienteDTO;
import com.example.demo.dtos.requestDTOs.clienteDTOs.UpdateClienteDTO;
import com.example.demo.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.example.demo.models.Cliente;

import java.util.List;

public interface IClienteService {
    List<ClienteResponseDTO> getClientes();

    ClienteResponseDTO addCliente(AddClienteDTO addClienteDTO);

    ClienteResponseDTO deleteCliente(Long id);

    ClienteResponseDTO updateCliente(Long id, UpdateClienteDTO updateClienteDTO);

    Cliente getClienteById(Long idCliente);

    void save(Cliente clienteAnterior);
}
