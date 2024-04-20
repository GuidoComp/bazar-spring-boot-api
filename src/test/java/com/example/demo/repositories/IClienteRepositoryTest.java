package com.example.demo.repositories;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class IClienteRepositoryTest {

    @Autowired
    private IClienteRepository iClienteRepository;

    @Test
    void shouldFindAllAndReturnNoOne() {
        List<Cliente> clientes = iClienteRepository.findAll();

        assertNotNull(clientes);
        assertEquals(0, clientes.size());
    }

    @Test
    void shouldAddAClientAndReturnIt() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Test");
        Cliente savedCliente = iClienteRepository.save(cliente);

        Cliente clienteEnDb = iClienteRepository.findById(savedCliente.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente not found for this id :: " + savedCliente.getClienteId()));

        assertNotNull(clienteEnDb);
        assertEquals(1, iClienteRepository.findAll().size());
        assertEquals("Test", clienteEnDb.getNombre());
    }

    @Test
    void shouldThrowAnExceptionWhenTryingToFindAClientThatDoesNotExist() {
        assertThrows(ResourceNotFoundException.class, () -> {
            iClienteRepository.findById(1L)
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente not found for this id :: " + 1L));
        });
    }

    @Test
    void shouldDeleteAClient() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Test");
        Cliente savedCliente = iClienteRepository.save(cliente);

        iClienteRepository.deleteById(savedCliente.getClienteId());

        assertEquals(0, iClienteRepository.findAll().size());
    }

    @Test
    void shouldUpdateAClient() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Test");
        Cliente savedCliente = iClienteRepository.save(cliente);

        Cliente clienteEnDb = iClienteRepository.findById(savedCliente.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente not found for this id :: " + savedCliente.getClienteId()));

        clienteEnDb.setNombre("Test2");
        iClienteRepository.save(clienteEnDb);

        Cliente clienteUpdated = iClienteRepository.findById(savedCliente.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente not found for this id :: " + savedCliente.getClienteId()));

        assertEquals("Test2", clienteUpdated.getNombre());
    }
}