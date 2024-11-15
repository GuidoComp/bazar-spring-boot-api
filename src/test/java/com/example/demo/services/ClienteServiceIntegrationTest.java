package com.example.demo.services;

import com.example.demo.Datos;
import com.example.demo.dtos.requestDTOs.clienteDTOs.AddClienteDTO;
import com.example.demo.dtos.requestDTOs.clienteDTOs.UpdateClienteDTO;
import com.example.demo.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.RestrictException;
import com.example.demo.models.Cliente;
import com.example.demo.models.Producto;
import com.example.demo.models.Venta;
import com.example.demo.repositories.IClienteRepository;
import com.example.demo.utils.ErrorMsgs;
import com.example.demo.utils.GenericModelMapper;
import org.hibernate.sql.Update;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClienteServiceIntegrationTest {
    @Autowired
    private ClienteService clienteService;

    @Nested
    @Tag("IntegrationTest")
    @DisplayName("Pruebas de integración de ClienteService -Repository y Mapper-")
    class IntegrationTest {
        @Test
        @Order(1)
        @DisplayName("Obtener cliente inexistente por id deberia lanzar excepción con mensaje adecuado")
        void getClienteInexistenteById() {
            assertThrows(ResourceNotFoundException.class, () -> clienteService.getClienteById(100L), ErrorMsgs.CLIENTE_NOT_FOUND_ID);
        }

        @Test
        @Order(2)
        void addCliente() {
            AddClienteDTO addClienteDTO = Datos.ADD_CLIENTE_DTO;
            ClienteResponseDTO clienteResponseDTO = clienteService.addCliente(addClienteDTO);

            assertNotNull(clienteResponseDTO);
            assertEquals(addClienteDTO.getDni(), clienteResponseDTO.getDni());
            assertEquals(addClienteDTO.getNombre(), clienteResponseDTO.getNombre());
            assertEquals(addClienteDTO.getApellido(), clienteResponseDTO.getApellido());
        }

        @Test
        @Order(3)
        void getAllClientes() {
            List<ClienteResponseDTO> clientesDb = clienteService.getClientes();

            assertNotNull(clientesDb);
            assertEquals(1, clientesDb.size());
            assertEquals(Datos.ADD_CLIENTE_DTO.getDni(), clientesDb.get(0).getDni());
            assertEquals(Datos.ADD_CLIENTE_DTO.getNombre(), clientesDb.get(0).getNombre());
            assertEquals(Datos.ADD_CLIENTE_DTO.getApellido(), clientesDb.get(0).getApellido());
        }

        @Test
        @Order(4)
        void agregarClienteYaAgregadoDeberiaLanzarExcepcion() {
            AddClienteDTO addClienteDTO = Datos.ADD_CLIENTE_DTO;

            assertThrows(RestrictException.class, () -> clienteService.addCliente(addClienteDTO), ErrorMsgs.CREAR_CLIENTE_DNI_FK);
        }

        @Test
        @Order(5)
        void addClienteNuloDeberiaLanzarExcepcion() {
            assertThrows(IllegalArgumentException.class, () -> clienteService.addCliente(null), ErrorMsgs.PARAMETRO_NULO);
        }
    }
}