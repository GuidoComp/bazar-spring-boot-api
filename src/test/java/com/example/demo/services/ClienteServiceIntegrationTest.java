package com.example.demo.services;

import com.example.demo.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.example.demo.models.Cliente;
import com.example.demo.models.Producto;
import com.example.demo.models.Venta;
import com.example.demo.repositories.IClienteRepository;
import com.example.demo.utils.GenericModelMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
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
class ClienteServiceIntegrationTest {
    @MockBean
    private IClienteRepository clienteRepository;

    @Autowired
    private ClienteService clienteService;

    @Nested
    @Tag("IntegrationTest")
    @DisplayName("Pruebas de integración entre ClienteService y ModelMapper")
    class IntegrationTest {
        @Test
        void getAllClientesIntegrationTest() {
            List<Cliente> clientes = List.of(
                    new Cliente(1L, "Marcelo", "Troncho", "36158188", List.of(
                            new Venta(new Cliente(),
                                    156.2,
                                    LocalDate.now(),
                                    1L
                            )
                    )),
                    new Cliente(2L, "Laura", "Perez", "36185926"),
                    new Cliente(3L, "Pedro", "Sable", "59623555")
            );
            when(clienteRepository.findAll()).thenReturn(clientes);

            List<ClienteResponseDTO> clientesDb = clienteService.getClientes();

            assertNotNull(clientesDb);
            assertEquals(clientes.size(), clientesDb.size());

            for (int i = 0; i < clientes.size(); i++) {
                assertEquals(clientes.get(i).getClienteId(), clientesDb.get(i).getClienteId());
                assertEquals(clientes.get(i).getNombre(), clientesDb.get(i).getNombre());
                assertEquals(clientes.get(i).getApellido(), clientesDb.get(i).getApellido());
                assertEquals(clientes.get(i).getDni(), clientesDb.get(i).getDni());
            }
        }
    }
}