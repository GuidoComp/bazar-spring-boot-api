package com.example.demo.datos.clientes;

import com.example.demo.datos.ventas.VentaDatos;
import com.example.demo.dtos.requestDTOs.clienteDTOs.AddClienteDTO;
import com.example.demo.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.example.demo.models.Cliente;
import com.example.demo.models.Venta;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClienteDatos {
    public static List<Cliente> crearClientesSinVentas() {
        return List.of(new Cliente(1L, "Juan", "Perez", "36158155"),
                new Cliente(2L, "Pedro", "Gomez", "36158156", null),
                new Cliente(3L, "Marcelo", "Troncho", "36158188")
        );
    }

    public static ClienteResponseDTO crearClienteResponseDTO() {
        return new ClienteResponseDTO(1L, "Juan", "Perez", "36158155");
    }

    public static AddClienteDTO crearAddClienteDTO() {
        return new AddClienteDTO("Juan", "Perez", "36158155");
    }

    public static Cliente crearClienteSinVentas() {
        return new Cliente(1L, "Juan", "Perez", "36158155", null);
    }

    public static List<Cliente> crearClientesConYSinVentas() {
        return List.of(
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
    }
    public static Cliente crearClienteConVentas() {
        return new Cliente(1L, "Juan", "Perez", "36158155", new ArrayList<>(VentaDatos.crearVentasConProductosYClientes()));
    }
}
