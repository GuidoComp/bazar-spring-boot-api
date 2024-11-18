package com.example.demo.repositories;

import com.example.demo.datos.clientes.ClienteDatos;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Cliente;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest //las transacciones se revierten al final de cada test
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IClienteRepositoryTest {

    @Autowired
    private IClienteRepository repository;

    @Test
    @Order(1)
    void shouldFindAllAndReturnNoOne() {
        List<Cliente> clientes = repository.findAll();

        assertNotNull(clientes);
        assertEquals(0, clientes.size());
    }

    @Test
//    @Transactional(propagation = Propagation.NOT_SUPPORTED) // activar para commitear la transaccion en bd
    @Order(2)
    void shouldAddAClient() {
        Cliente savedCliente = repository.save(ClienteDatos.crearClienteSinVentas());

        Cliente clienteEnDb = repository.findById(savedCliente.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente not found for this id: " + savedCliente.getClienteId()));

        assertNotNull(clienteEnDb);
        assertEquals(1, repository.findAll().size());
        assertEquals("Juan", clienteEnDb.getNombre());
        assertDoesNotThrow(() -> repository.findById(savedCliente.getClienteId()));
    }

    @Test
    @DisplayName("Arrojar ResourceNotFoundException al buscar un cliente inexistente por id")
    @Order(3)
    void findByIdClienteInexistente() {
        assertThrows(ResourceNotFoundException.class, () -> {
            repository.findById(1L)
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente not found for this id: " + 1L));
        });
    }

    @Test
    @Order(4)
    void shouldDeleteAClient() {
        Cliente savedCliente = repository.save(ClienteDatos.crearClienteSinVentas());

        repository.deleteById(savedCliente.getClienteId());

        assertEquals(0, repository.findAll().size());
    }

    @Test
    @Order(5)
    void shouldUpdateAClient() {
        Cliente savedCliente = repository.save(ClienteDatos.crearClienteSinVentas());

        Cliente clienteEnDb = repository.findById(savedCliente.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente not found for this id: " + savedCliente.getClienteId()));

        clienteEnDb.setNombre("Test2");
        repository.save(clienteEnDb);

        Cliente clienteUpdated = repository.findById(savedCliente.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente not found for this id: " + savedCliente.getClienteId()));

        assertEquals("Test2", clienteUpdated.getNombre());
    }

    @Test
    @Order(6)
    void findClienteByDni() {
        repository.saveAll(ClienteDatos.crearClientesSinVentas());

        Optional<Cliente> clienteDb = repository.findClienteByDni("36158155");

        assertNotNull(clienteDb);
        assertEquals("Juan", clienteDb.get().getNombre());
    }

    @Test
    @Order(7)
    void findClienteInexistenteByDni() {
        repository.saveAll(ClienteDatos.crearClientesSinVentas());

        Optional<Cliente> clienteDb = repository.findClienteByDni("23222222");

        assertTrue(clienteDb.isEmpty());
    }
}