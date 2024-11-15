package com.example.demo.repositories;

import com.example.demo.Datos;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest //las transacciones se revierten al final de cada test
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class IClienteRepositoryTest {

    @Autowired
    private IClienteRepository repository;

    @Test
    void shouldFindAllAndReturnNoOne() {
        List<Cliente> clientes = repository.findAll();

        assertNotNull(clientes);
        assertEquals(0, clientes.size());
    }

    @Test
//    @Transactional(propagation = Propagation.NOT_SUPPORTED) // activar para commitear la transaccion en bd
    void shouldAddAClient() {
        Cliente savedCliente = repository.save(Datos.CLIENTE_SIN_VENTAS);

        Cliente clienteEnDb = repository.findById(savedCliente.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente not found for this id: " + savedCliente.getClienteId()));

        assertNotNull(clienteEnDb);
        assertEquals(1, repository.findAll().size());
        assertEquals("Juan", clienteEnDb.getNombre());
        assertDoesNotThrow(() -> repository.findById(savedCliente.getClienteId()));
    }

    @Test
    @DisplayName("Arrojar ResourceNotFoundException al buscar un cliente inexistente por id")
    void findByIdClienteInexistente() {
        assertThrows(ResourceNotFoundException.class, () -> {
            repository.findById(1L)
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente not found for this id: " + 1L));
        });
    }

    @Test
    void shouldDeleteAClient() {
        Cliente savedCliente = repository.save(Datos.CLIENTE_SIN_VENTAS);

        repository.deleteById(savedCliente.getClienteId());

        assertEquals(0, repository.findAll().size());
    }

    @Test
    void shouldUpdateAClient() {
        Cliente savedCliente = repository.save(Datos.CLIENTE_SIN_VENTAS);

        Cliente clienteEnDb = repository.findById(savedCliente.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente not found for this id: " + savedCliente.getClienteId()));

        clienteEnDb.setNombre("Test2");
        repository.save(clienteEnDb);

        Cliente clienteUpdated = repository.findById(savedCliente.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente not found for this id: " + savedCliente.getClienteId()));

        assertEquals("Test2", clienteUpdated.getNombre());
    }

    @Test
    void findClienteByDni() {
        repository.saveAll(Datos.CLIENTES_SIN_VENTAS);

        Optional<Cliente> clienteDb = repository.findClienteByDni("36158155");

        assertNotNull(clienteDb);
        assertEquals("Juan", clienteDb.get().getNombre());
    }

    @Test
    void findClienteInexistenteByDni() {
        repository.saveAll(Datos.CLIENTES_SIN_VENTAS);

        Optional<Cliente> clienteDb = repository.findClienteByDni("23222222");

        assertTrue(clienteDb.isEmpty());
    }
}