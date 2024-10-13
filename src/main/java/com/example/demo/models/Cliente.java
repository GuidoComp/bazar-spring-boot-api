package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity(name = "Clientes")
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
