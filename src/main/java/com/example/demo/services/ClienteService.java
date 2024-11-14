package com.example.demo.services;

import com.example.demo.dtos.requestDTOs.clienteDTOs.AddClienteDTO;
import com.example.demo.dtos.requestDTOs.clienteDTOs.UpdateClienteDTO;
import com.example.demo.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.RestrictException;
import com.example.demo.models.Cliente;
import com.example.demo.repositories.IClienteRepository;
import com.example.demo.utils.ErrorMsgs;
import com.example.demo.utils.IModelMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService implements IClienteService {
    private final IClienteRepository clienteRepository;
//    private final IModelMapper mapper;
    private final ModelMapper mapper;

//    public ClienteService(IClienteRepository clienteRepository, IModelMapper modelMapper) {
//        this.mapper = modelMapper;
//        this.clienteRepository = clienteRepository;
//    }

    public ClienteService(IClienteRepository clienteRepository, ModelMapper modelMapper) {
        this.mapper = modelMapper;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<ClienteResponseDTO> getClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(cli -> mapper.map(cli, ClienteResponseDTO.class))
                .toList();
    }

    @Override
    public ClienteResponseDTO addCliente(AddClienteDTO addClienteDTO) {
        return null;
    }

    @Override
    public ClienteResponseDTO deleteCliente(Long id) {
        return null;
    }

    @Override
    public ClienteResponseDTO updateCliente(Long id, UpdateClienteDTO updateClienteDTO) {
        return null;
    }

    @Override
    public Cliente getClienteById(Long idCliente) {
        return null;
    }

    @Override
    public void save(Cliente clienteAnterior) {

    }

    @Override
    public Optional<Cliente> getClienteByDni(String dni) {
        return Optional.empty();
    }

    @Override
    public void deleteAllClientes() {

    }

//    @Override
//    public ClienteResponseDTO addCliente(AddClienteDTO addClienteDTO) {
//        Cliente cliente = mapper.mapAddClienteDTOToCliente(addClienteDTO);
//        checkDniCliente(cliente.getDni());
//        Cliente clienteDb = clienteRepository.save(cliente);
//        return mapper.mapClienteToDTO(clienteDb);
//    }
//
//    private void checkDniCliente(String dniCliente) {
//        if (this.getClienteByDni(dniCliente).isPresent()) {
//            throw new RestrictException(ErrorMsgs.CREAR_CLIENTE_DNI_FK);
//        }
//    }
//
//    @Override
//    public Optional<Cliente> getClienteByDni(String dni) {
//        List<Cliente> clientesDb = this.clienteRepository.findAll();
//        Optional<Cliente> cliente = Optional.empty();
//        int index = 0;
//        while(cliente.isEmpty() && index < clientesDb.size()) {
//            if (clientesDb.get(index).getDni().equals(dni)) {
//                cliente = Optional.ofNullable(clientesDb.get(index));
//            }
//            index++;
//        }
//        return cliente;
//    }
//
//    @Override
//    public void deleteAllClientes() {
//        this.clienteRepository.deleteAll();
//    }
//
//    @Override
//    public ClienteResponseDTO deleteCliente(Long id) {
//        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMsgs.CLIENTE_NOT_FOUND, id)));
//        if (cliente.getVentas() != null) {
//            throw new RestrictException(ErrorMsgs.DELETE_CLIENTE_RESTRICCION_FK);
//        }
//        clienteRepository.delete(cliente);
//
//        return mapper.mapClienteToDTO(cliente);
//    }
//
//    @Override
//    public ClienteResponseDTO updateCliente(Long id, UpdateClienteDTO updateClienteDTO) {
//        Cliente cliente = this.clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMsgs.CLIENTE_NOT_FOUND, id)));
//
//        String nombre = updateClienteDTO.getNombre();
//        if (nombre != null) {
//            cliente.setNombre(nombre);
//        }
//        String apellido = updateClienteDTO.getApellido();
//        if (apellido != null) {
//            cliente.setApellido(apellido);
//        }
//        String dni = updateClienteDTO.getDni();
//        if (dni != null) {
//            this.checkDniCliente(dni);
//            cliente.setDni(dni);
//        }
//        this.clienteRepository.save(cliente);
//        return mapper.mapClienteToDTO(cliente);
//    }
//
//    @Override
//    public Cliente getClienteById(Long idCliente) {
//        return clienteRepository.findById(idCliente).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMsgs.CLIENTE_NOT_FOUND, idCliente)));
//    }
//
//    @Override
//    public void save(Cliente clienteAnterior) {
//        this.clienteRepository.save(clienteAnterior);
//    }
}
