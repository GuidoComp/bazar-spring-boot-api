package com.example.demo.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Entity(name = "Clientes")
@Data
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cliente_id")
    private Long clienteId;
    private String nombre;
    private String apellido;
    @Column(unique = true)
    private String dni;
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<Venta> ventas;

    public Cliente() {
        this.ventas = new ArrayList<>();
    }

    public Cliente(Long clienteId, String nombre, String apellido, String dni) {
        this();
        this.clienteId = clienteId;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
    }

    public void agregarVenta(Venta venta) {
        this.ventas.add(venta);
    }

    public void borrarVenta(Venta venta) {
        if (ventas != null) {
            this.ventas.remove(venta);
        }
    }
}
