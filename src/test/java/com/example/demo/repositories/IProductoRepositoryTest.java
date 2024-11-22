package com.example.demo.repositories;

import com.example.demo.models.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class IProductoRepositoryTest {
    @Autowired
    IProductoRepository repository;

    @BeforeEach
    void setUp() {
        repository.saveAll(List.of(
                new Producto(1L, "Procesador", "Intel Core i5 14600KF", 400000.0, 1.0),
                new Producto(2L, "Placa de video", "Nvidia RTX 3060", 500000.0, 4.0),
                new Producto(3L, "Placa de video", "Nvidia RTX 3070", 600000.0, 5.0),
                new Producto(4L, "Placa de video", "Nvidia RTX 3080", 700000.0, 7.0),
                new Producto(5L, "Placa de video", "Nvidia RTX 3090", 800000.0, 10.0)
        ));
    }

    @Test
    void findByCantidadDisponibleLessThan() {
        List<Producto> productos = repository.findByCantidadDisponibleLessThan(5);
        assertEquals(2, productos.size());
    }

    @Test
    void findAllByIds() {
        List<Producto> productos = repository.findAllById(List.of(1L, 10L, 3L));
        assertEquals(2, productos.size());
        assertEquals(1L, productos.get(0).getProductoId());
        assertEquals(3L, productos.get(1).getProductoId());
    }
}