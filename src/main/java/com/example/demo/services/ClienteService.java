package com.example.demo.services;

import com.example.demo.dtos.requestDTOs.clienteDTOs.AddClienteDTO;
import com.example.demo.dtos.requestDTOs.clienteDTOs.UpdateClienteDTO;
import com.example.demo.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.RestrictException;
import com.example.demo.models.Cliente;
import com.example.demo.repositories.IClienteRepository;
import com.example.demo.utils.ErrorMsgs;
import com.example.demo.utils.GenericModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        checkDniCliente(cliente.getDni());
        Cliente clienteDb = clienteRepository.save(cliente);
        return modelMapper.mapClienteToDTO(clienteDb);
    }

    private void checkDniCliente(String dniCliente) {
        if (this.getClienteByDni(dniCliente).isPresent()) {
            throw new RestrictException(ErrorMsgs.CREAR_CLIENTE_DNI_FK);
        }
    }

    @Override
    public Optional<Cliente> getClienteByDni(String dni) {
        List<Cliente> clientesDb = this.clienteRepository.findAll();
        Optional<Cliente> cliente = Optional.empty();
        int index = 0;
        while(cliente.isEmpty() && index < clientesDb.size()) {
            if (clientesDb.get(index).getDni().equals(dni)) {
                cliente = Optional.ofNullable(clientesDb.get(index));
            }
            index++;
        }
        return cliente;
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

        String nombre = updateClienteDTO.getNombre();
        if (nombre != null) {
            cliente.setNombre(nombre);
        }
        String apellido = updateClienteDTO.getApellido();
        if (apellido != null) {
            cliente.setApellido(apellido);
        }
        String dni = updateClienteDTO.getDni();
        if (dni != null) {
            this.checkDniCliente(dni);
            cliente.setDni(dni);
        }
        this.clienteRepository.save(cliente);
        return modelMapper.mapClienteToDTO(cliente);
    }

    @Override
    public Cliente getClienteById(Long idCliente) {
        return clienteRepository.findById(idCliente).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMsgs.CLIENTE_NOT_FOUND, idCliente)));
    }

    @Override
    public void save(Cliente clienteAnterior) {
        this.clienteRepository.save(clienteAnterior);
    }
}
