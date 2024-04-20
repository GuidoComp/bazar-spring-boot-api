package com.example.demo.services;

import com.example.demo.dtos.requestDTOs.clienteDTOs.AddClienteDTO;
import com.example.demo.dtos.requestDTOs.clienteDTOs.UpdateClienteDTO;
import com.example.demo.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.example.demo.models.Cliente;
import com.example.demo.repositories.IClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private IClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void getClientes() {
        //Given
        Cliente cliente = new Cliente(1L, "Juan", "Perez", "36158155", null);
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(cliente);
        when(clienteRepository.findAll()).thenReturn(clientes);

        //When
        List<ClienteResponseDTO> clientesDTO = clienteService.getClientes();

        //Then
        assertNotNull(clientesDTO);
        assertEquals(clientes.get(0).getClienteId(), clientesDTO.get(0).getClienteId());
        assertEquals(clientes.get(0).getNombre(), clientesDTO.get(0).getNombre());
        assertEquals(clientes.get(0).getApellido(), clientesDTO.get(0).getApellido());
        assertEquals(clientes.get(0).getDni(), clientesDTO.get(0).getDni());
    }

    @Test
    void shouldReturnAnEmptyList() {
        //Given
        List<Cliente> clientes = new ArrayList<>();
        when(clienteRepository.findAll()).thenReturn(clientes);

        //When
        List<ClienteResponseDTO> clientesDTO = clienteService.getClientes();

        //Then
        assertNotNull(clientesDTO);
        assertTrue(clientesDTO.isEmpty());
    }

    @Test
    void addCliente() {
        //Given
        AddClienteDTO cliente = new AddClienteDTO("Juan", "Perez", "36158155");
        Cliente clienteEntity = new Cliente(1L, "Juan", "Perez", "36158155", null);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteEntity);

        //When
        ClienteResponseDTO clienteAgregadoDTO = clienteService.addCliente(cliente);

        //Then
        assertNotNull(clienteAgregadoDTO);
        assertEquals(cliente.getNombre(), clienteAgregadoDTO.getNombre());
        assertEquals(cliente.getApellido(), clienteAgregadoDTO.getApellido());
        assertEquals(cliente.getDni(), clienteAgregadoDTO.getDni());
    }

    @Test
    void deleteCliente() {
        Cliente cliente = new Cliente(1L, "Juan", "Perez", "36158155", null);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        ClienteResponseDTO clienteDTO = null;

        clienteDTO = clienteService.deleteCliente(1L);

        assertNotNull(clienteDTO);
        assertEquals(cliente.getClienteId(), clienteDTO.getClienteId());
        assertEquals(cliente.getNombre(), clienteDTO.getNombre());
        assertEquals(cliente.getApellido(), clienteDTO.getApellido());
        assertEquals(cliente.getDni(), clienteDTO.getDni());
    }

    @Test
    void shouldThrowExceptionWhenDeleteCliente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> clienteService.deleteCliente(1L));
    }

    @Test
    void updateCliente() {
        Cliente cliente = new Cliente(1L, "Juan", "Perez", "36158155", null);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        ClienteResponseDTO clienteDTO = null;

        clienteDTO = clienteService.updateCliente(1L, new UpdateClienteDTO("Marcos", "Perez", "36158155"));

        assertNotNull(clienteDTO);
        assertEquals(cliente.getClienteId(), clienteDTO.getClienteId());
        assertEquals(cliente.getApellido(), clienteDTO.getApellido());
        assertEquals(cliente.getDni(), clienteDTO.getDni());
        assertEquals("Marcos", clienteDTO.getNombre());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }
}