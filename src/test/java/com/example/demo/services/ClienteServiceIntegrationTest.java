package com.example.demo.services;

import com.example.demo.datos.clientes.ClienteDatos;
import com.example.demo.dtos.requestDTOs.clienteDTOs.AddClienteDTO;
import com.example.demo.dtos.requestDTOs.clienteDTOs.UpdateClienteDTO;
import com.example.demo.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.RestrictException;
import com.example.demo.utils.ErrorMsgs;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Pruebas de integración de ClienteService -Repository y Mapper-")
class ClienteServiceIntegrationTest {
    @Autowired
    private ClienteService clienteService;

    @Test
    @DisplayName("Obtener cliente inexistente por id deberia lanzar excepción con mensaje adecuado")
    void getClienteInexistenteById() {
        assertThrows(ResourceNotFoundException.class, () -> clienteService.getClienteById(100L), ErrorMsgs.CLIENTE_NOT_FOUND_ID);
    }

    @Test
    void addCliente() {
        AddClienteDTO addClienteDTO = ClienteDatos.crearAddClienteDTO();

        ClienteResponseDTO clienteResponseDTO = clienteService.addCliente(addClienteDTO);

        assertNotNull(clienteResponseDTO);
        assertNotNull(clienteResponseDTO.getClienteId());
        assertEquals(addClienteDTO.getDni(), clienteResponseDTO.getDni());
        assertEquals(addClienteDTO.getNombre(), clienteResponseDTO.getNombre());
        assertEquals(addClienteDTO.getApellido(), clienteResponseDTO.getApellido());
    }

    @Test
    void getAllClientes() {
        AddClienteDTO addClienteDTO = ClienteDatos.crearAddClienteDTO();
        clienteService.addCliente(addClienteDTO);

        List<ClienteResponseDTO> clientesDb = clienteService.getClientes();

        assertNotNull(clientesDb);
        assertEquals(1, clientesDb.size());
        assertEquals(addClienteDTO.getDni(), clientesDb.get(0).getDni());
        assertEquals(addClienteDTO.getNombre(), clientesDb.get(0).getNombre());
        assertEquals(addClienteDTO.getApellido(), clientesDb.get(0).getApellido());
    }

    @Test
    void agregarClienteYaAgregadoDeberiaLanzarExcepcion() {
        AddClienteDTO addClienteDTO = ClienteDatos.crearAddClienteDTO();
        clienteService.addCliente(addClienteDTO);

        assertThrows(RestrictException.class, () -> clienteService.addCliente(addClienteDTO), ErrorMsgs.CREAR_CLIENTE_DNI_FK);
    }

    @Test
    void addClienteNuloDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> clienteService.addCliente(null), ErrorMsgs.PARAMETRO_NULO);
    }

    @Test
    void updateNombreCliente() {
        AddClienteDTO addClienteDTO = ClienteDatos.crearAddClienteDTO();
        ClienteResponseDTO creado = clienteService.addCliente(addClienteDTO);
        UpdateClienteDTO updateClienteDTO = new UpdateClienteDTO("Marcelo", null, null);

        ClienteResponseDTO clienteResponseDTO = clienteService.updateCliente(creado.getClienteId(), updateClienteDTO);

        assertNotNull(clienteResponseDTO);
        assertEquals(updateClienteDTO.getNombre(), clienteResponseDTO.getNombre());
        assertEquals(addClienteDTO.getApellido(), clienteResponseDTO.getApellido());
        assertEquals(addClienteDTO.getDni(), clienteResponseDTO.getDni());
    }
}
