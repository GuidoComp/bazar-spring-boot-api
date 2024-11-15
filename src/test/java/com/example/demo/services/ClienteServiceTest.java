package com.example.demo.services;

import com.example.demo.Datos;
import com.example.demo.dtos.requestDTOs.clienteDTOs.AddClienteDTO;
import com.example.demo.dtos.requestDTOs.clienteDTOs.UpdateClienteDTO;
import com.example.demo.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.RestrictException;
import com.example.demo.models.Cliente;
import com.example.demo.repositories.IClienteRepository;
import com.example.demo.utils.ErrorMsgs;
import com.example.demo.utils.IModelMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClienteServiceTest {

    @Mock
    IClienteRepository clienteRepository;

    @Mock
    IModelMapper mapper;

    @InjectMocks
    ClienteService clienteService;

    @Test
    @Order(1)
    void addCliente() {
        //Given
        when(mapper.mapAddClienteDTOToCliente(any())).thenReturn(Datos.CLIENTE_SIN_VENTAS);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(Datos.CLIENTE_SIN_VENTAS);
        when(mapper.mapClienteToDTO(any(Cliente.class))).thenReturn(new ClienteResponseDTO(1L, "Juan", "Perez", "36158155"));

        //When
        ClienteResponseDTO clienteAgregadoDTO = clienteService.addCliente(Datos.ADD_CLIENTE_DTO);

        //Then
        assertNotNull(clienteAgregadoDTO);
        assertEquals(Datos.CLIENTE_SIN_VENTAS.getNombre(), clienteAgregadoDTO.getNombre());
        assertEquals(Datos.CLIENTE_SIN_VENTAS.getApellido(), clienteAgregadoDTO.getApellido());
        assertEquals(Datos.CLIENTE_SIN_VENTAS.getDni(), clienteAgregadoDTO.getDni());
    }

    @Test
    @Order(2)
    void getClienteByDni() {
        String dni = "36158155";
        when(clienteRepository.findClienteByDni(any())).thenReturn(Optional.of(Datos.CLIENTE_SIN_VENTAS));

        Optional<Cliente> clienteByDni = clienteService.getClienteByDni(dni);

        assertNotNull(clienteByDni);
        assertEquals("Juan", clienteByDni.orElseThrow().getNombre());
        assertEquals("Perez", clienteByDni.orElseThrow().getApellido());
        assertEquals("36158155", clienteByDni.orElseThrow().getDni());
    }

    @Test
    @Order(3)
    void getClientInexistenteByDni() {
        String dni = "22333333";
        when(clienteRepository.findClienteByDni(any())).thenReturn(Optional.empty());

        Optional<Cliente> clienteByDni = clienteService.getClienteByDni(dni);

        assertNotNull(clienteByDni);
        assertEquals(Optional.empty(), clienteByDni);
    }

    @Test
    @Order(4)
    void getClientes() {
        //Given
        List<ClienteResponseDTO> clienteResponseDTOS = List.of(
                new ClienteResponseDTO(1L, "Juan", "Perez", "36158155"),
                new ClienteResponseDTO(2L, "Marcelo", "Troncho", "36158156")
        );
        when(clienteRepository.findAll()).thenReturn(Datos.CLIENTES_SIN_VENTAS);
        when(mapper.mapClientesToDTO(Datos.CLIENTES_SIN_VENTAS)).thenReturn(clienteResponseDTOS);

        //When
        List<ClienteResponseDTO> clientesDTO = clienteService.getClientes();

        //Then
        assertNotNull(clientesDTO);
        assertEquals(2, clientesDTO.size());
        assertEquals("Juan", clientesDTO.get(0).getNombre());
        assertEquals("Perez", clientesDTO.get(0).getApellido());
        assertEquals("36158155", clientesDTO.get(0).getDni());
        assertEquals("Marcelo", clientesDTO.get(1).getNombre());
        assertEquals("Troncho", clientesDTO.get(1).getApellido());
        assertEquals("36158156", clientesDTO.get(1).getDni());
    }

    @Test
    @Order(5)
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
    @Order(6)
    void deleteCliente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(Datos.CLIENTE_SIN_VENTAS));
        when(mapper.mapClienteToDTO(Datos.CLIENTE_SIN_VENTAS)).thenReturn(new ClienteResponseDTO(1L, "Juan", "Perez", "36158155"));

        ClienteResponseDTO clienteDTO = clienteService.deleteCliente(1L);

        assertNotNull(clienteDTO);
        assertEquals(Datos.CLIENTE_SIN_VENTAS.getClienteId(), clienteDTO.getClienteId());
        assertEquals(Datos.CLIENTE_SIN_VENTAS.getNombre(), clienteDTO.getNombre());
        assertEquals(Datos.CLIENTE_SIN_VENTAS.getApellido(), clienteDTO.getApellido());
        assertEquals(Datos.CLIENTE_SIN_VENTAS.getDni(), clienteDTO.getDni());
    }

    @Test
    @Order(7)
    void deleteClienteConVentasArrojaExcepcion() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(Datos.CLIENTE_CON_VENTAS));
        assertThrows(RestrictException.class, () -> clienteService.deleteCliente(1L), ErrorMsgs.DELETE_CLIENTE_RESTRICCION_FK);
    }

    @Test
    @Order(8)
    void shouldThrowExceptionWhenDeleteClienteInexistente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clienteService.deleteCliente(1L), ErrorMsgs.CLIENTE_NOT_FOUND);
    }

    @Test
    @Order(9)
    void updateCliente() {
        // Given
        UpdateClienteDTO updateClienteDTO = new UpdateClienteDTO("Sebastian", "Royanos", "22333111");
        Cliente clienteEnDb = Datos.CLIENTE_SIN_VENTAS;
        Cliente clienteActualizado = new Cliente(1L, "Sebastian", "Royanos", "22333111");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEnDb));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteActualizado);
        when(mapper.mapClienteToDTO(any(Cliente.class))).thenReturn(new ClienteResponseDTO(1L, "Sebastian", "Royanos", "22333111"));

        // When
        ClienteResponseDTO clienteResponseDTO = clienteService.updateCliente(1L, updateClienteDTO);

        // Then
        assertNotNull(clienteResponseDTO);
        assertEquals("Sebastian", clienteResponseDTO.getNombre());
        assertEquals("Royanos", clienteResponseDTO.getApellido());
        assertEquals("22333111", clienteResponseDTO.getDni());
    }
}