package com.example.demo.services;

import com.example.demo.dtos.requestDTOs.clienteDTOs.AddClienteDTO;
import com.example.demo.dtos.requestDTOs.clienteDTOs.UpdateClienteDTO;
import com.example.demo.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.RestrictException;
import com.example.demo.models.Cliente;
import com.example.demo.models.Venta;
import com.example.demo.repositories.IClienteRepository;
import com.example.demo.utils.ErrorMsgs;
import com.example.demo.utils.IModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService implements IClienteService {
    private final IClienteRepository clienteRepository;
    private final IModelMapper mapper;

    public ClienteService(IClienteRepository clienteRepository, IModelMapper modelMapper) {
        this.mapper = modelMapper;
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> getClientes() {
        return mapper.mapClientesToDTO(clienteRepository.findAll());
    }

    @Override
    @Transactional
    public ClienteResponseDTO addCliente(AddClienteDTO addClienteDTO) {
        Cliente cliente = mapper.mapAddClienteDTOToCliente(addClienteDTO);
        checkDniCliente(cliente.getDni());
        Cliente clienteDb = clienteRepository.save(cliente);
        return mapper.mapClienteToDTO(clienteDb);
    }

    private void checkDniCliente(String dniCliente) {
        if (this.getClienteByDni(dniCliente).isPresent()) {
            throw new RestrictException(ErrorMsgs.CREAR_CLIENTE_DNI_FK);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> getClienteByDni(String dni) {
        return clienteRepository.findClienteByDni(dni);
    }

    @Override
    @Transactional
    public void deleteAllClientes() {
        this.clienteRepository.deleteAll();
    }

    @Override
    public void agregarVenta(Cliente cliente, Venta venta) {
        cliente.agregarVenta(venta);
        clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public ClienteResponseDTO deleteCliente(Long id) {
        Cliente cliente = this.getClienteById(id);
        if (cliente.getVentas() != null) {
            throw new RestrictException(ErrorMsgs.DELETE_CLIENTE_RESTRICCION_FK);
        }
        clienteRepository.delete(cliente);

        return mapper.mapClienteToDTO(cliente);
    }

    @Override
    @Transactional
    public ClienteResponseDTO updateCliente(Long id, UpdateClienteDTO updateClienteDTO) {
        Cliente clienteEnDb = this.getClienteById(id);

        if (updateClienteDTO.getNombre() != null) {
            clienteEnDb.setNombre(updateClienteDTO.getNombre());
        }
        if (updateClienteDTO.getApellido() != null) {
            clienteEnDb.setApellido(updateClienteDTO.getApellido());
        }
        if (updateClienteDTO.getDni() != null) {
            clienteEnDb.setDni(updateClienteDTO.getDni());
        }

        clienteRepository.save(clienteEnDb);
        return mapper.mapClienteToDTO(clienteEnDb);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente getClienteById(Long idCliente) {
        return clienteRepository.findById(idCliente).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMsgs.CLIENTE_NOT_FOUND_ID, idCliente)));
    }
}
