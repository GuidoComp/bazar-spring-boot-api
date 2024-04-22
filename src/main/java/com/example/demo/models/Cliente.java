package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
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
    private List<Venta> ventas = new ArrayList<>();

    public void agregarVenta(Venta venta) {
        //no es necesario quizas
        if (this.ventas == null) {
            this.ventas = new ArrayList<>();
        }
        this.ventas.add(venta);
    }

    public void borrarVenta(Venta venta) {
        this.ventas.remove(venta);
    }
}
