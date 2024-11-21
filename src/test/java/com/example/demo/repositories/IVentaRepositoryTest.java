package com.example.demo.repositories;

import com.example.demo.datos.clientes.ClienteDatos;
import com.example.demo.datos.ventas.VentaDatos;
import com.example.demo.models.Venta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class IVentaRepositoryTest {
    @Autowired
    IVentaRepository repository;
    List<Venta> ventas;

    @BeforeEach
    void setUp() {
        ventas = new ArrayList<>();
        ventas.addAll(VentaDatos.crearVentasConProductosYClientes());
        ventas.add(new Venta(null, LocalDate.of(1991, 5, 2), 100.0, null, null));
        ventas.add(new Venta(null, LocalDate.of(1991, 5, 2), 150.0, null, null));
        repository.saveAll(ventas);
    }

    @Test
    void findByFechaVenta() {
        List<Venta> ventasByDate = repository.findByFechaVenta(LocalDate.of(1991, 5, 2));
        assertEquals(2, ventasByDate.size());
        assertEquals(100.0, ventasByDate.get(0).getTotal());
    }
}