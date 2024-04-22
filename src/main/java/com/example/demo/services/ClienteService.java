package com.example.demo.services;

import com.example.demo.dtos.requestDTOs.clienteDTOs.AddClienteDTO;
import com.example.demo.dtos.requestDTOs.clienteDTOs.UpdateClienteDTO;
import com.example.demo.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.RestrictException;
import com.example.demo.models.Cliente;
import com.example.demo.models.Producto;
import com.example.demo.repositories.IClienteRepository;
import com.example.demo.utils.ErrorMsgs;
import com.example.demo.utils.GenericModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService implements IClienteService {

    private final IClienteRepository clienteRepository;
    private final GenericModelMapper modelMapper = GenericModelMapper.getModelMapper();

    public ClienteService(IClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<ClienteResponseDTO> getClientes() {
        return modelMapper.mapClientesToDTO(clienteRepository.findAll());
    }

    @Override
    public ClienteResponseDTO addCliente(AddClienteDTO addClienteDTO) {
        Cliente cliente = modelMapper.mapAddClienteDTOToCliente(addClienteDTO);
        Cliente clienteDb = this.clienteRepository.save(cliente);
        return modelMapper.mapClienteToDTO(clienteDb);
    }

    @Override
    public ClienteResponseDTO deleteCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMsgs.CLIENTE_NOT_FOUND, id)));
        if (!cliente.getVentas().isEmpty()) {
            throw new RestrictException(ErrorMsgs.DELETE_CLIENTE_RESTRICCION_FK);
        }
        clienteRepository.delete(cliente);

        return modelMapper.mapClienteToDTO(cliente);
    }

    @Override
    public ClienteResponseDTO updateCliente(Long id, UpdateClienteDTO updateClienteDTO) {
        Cliente cliente = this.clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMsgs.CLIENTE_NOT_FOUND, id)));

        if (updateClienteDTO.getNombre() != null) {
            cliente.setNombre(updateClienteDTO.getNombre());
        }
        if (updateClienteDTO.getApellido() != null) {
            cliente.setApellido(updateClienteDTO.getApellido());
        }
        if (updateClienteDTO.getDni() != null) {
            cliente.setDni(updateClienteDTO.getDni());
        }
        this.clienteRepository.save(cliente);
        return modelMapper.mapClienteToDTO(cliente);
    }

    @Override
    public Cliente getClienteById(Long idCliente) throws ResourceNotFoundException {
        return clienteRepository.findById(idCliente).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMsgs.CLIENTE_NOT_FOUND, idCliente)));
    }

    @Override
    public void save(Cliente clienteAnterior) {
        this.clienteRepository.save(clienteAnterior);
    }
}
