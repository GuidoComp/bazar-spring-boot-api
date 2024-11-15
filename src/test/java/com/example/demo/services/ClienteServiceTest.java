package com.example.demo.services;

import com.example.demo.dtos.requestDTOs.clienteDTOs.AddClienteDTO;
import com.example.demo.dtos.requestDTOs.clienteDTOs.UpdateClienteDTO;
import com.example.demo.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.example.demo.models.Cliente;
import com.example.demo.repositories.IClienteRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    IClienteRepository clienteRepository;

    @InjectMocks
    ClienteService clienteService;

    List<Cliente> clientes;

    @BeforeEach
    void setUp() {
        List<Cliente> clientes = List.of(
                new Cliente(1L, "Marcelo", "Troncho", "36158188"),
                new Cliente(2L, "Laura", "Perez", "36185926"),
                new Cliente(3L, "Pedro", "Sable", "59623555")
        );
        this.clientes = clientes;
    }

    @Test
    void getClienteByDni() {
        String dni = "36158188";
        when(clienteRepository.findAll()).thenReturn(clientes);

        Optional<Cliente> clienteByDni = clienteService.getClienteByDni(dni);

        assertNotNull(clienteByDni);
        assertEquals("Marcelo", clienteByDni.orElseThrow().getNombre());
        assertEquals("Troncho", clienteByDni.orElseThrow().getApellido());
        assertEquals("36158188", clienteByDni.orElseThrow().getDni());
    }

    @Test
    void getClientInexistenteByDni() {
        String dni = "22333333";
        when(clienteRepository.findAll()).thenReturn(clientes);

        Optional<Cliente> clienteByDni = clienteService.getClienteByDni(dni);

        assertNotNull(clienteByDni);
        assertEquals(Optional.empty(), clienteByDni);
    }

    //    @Nested
//    @Tag("MapperTest")
//    @DisplayName("Probando mapper")
//    class MapperTest {
//        @Test
//        void getAllClientesTest() {
//            List<Cliente> clientes = List.of(
//                    new Cliente(1L, "Marcelo", "Troncho", "36158188"),
//                    new Cliente(2L, "Laura", "Perez", "36185926"),
//                    new Cliente(3L, "Pedro", "Sable", "59623555")
//            );
//            when(clienteRepository.findAll()).thenReturn(clientes);
//            when(mapper.map(any(Cliente.class), any(Class.class))).thenAnswer(inv -> {
//                Cliente cliente = inv.getArgument(0);
//                ClienteResponseDTO dto = new ClienteResponseDTO();
//                dto.setClienteId(cliente.getClienteId());
//                dto.setNombre(cliente.getNombre());
//                dto.setApellido(cliente.getApellido());
//                dto.setDni(cliente.getDni());
//                return dto;
//            });
//
//            List<ClienteResponseDTO> clientesDb = clienteService.getClientes();
//
//            assertNotNull(clientesDb);
//            System.out.println(clientesDb);
//            assertEquals(clientesDb.get(0).getClienteId(), clientes.get(0).getClienteId());
//            assertEquals(clientesDb.get(0).getNombre(), clientes.get(0).getNombre());
//            assertEquals(clientesDb.get(0).getApellido(), clientes.get(0).getApellido());
//            assertEquals(clientesDb.get(0).getDni(), clientes.get(0).getDni());
//
//            assertEquals(clientesDb.get(1).getClienteId(), clientes.get(1).getClienteId());
//            assertEquals(clientesDb.get(1).getNombre(), clientes.get(1).getNombre());
//            assertEquals(clientesDb.get(1).getApellido(), clientes.get(1).getApellido());
//            assertEquals(clientesDb.get(1).getDni(), clientes.get(1).getDni());
//        }
//    }

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