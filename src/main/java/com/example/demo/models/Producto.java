package com.example.demo.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_id")
    private Long productoId;
    @Column(columnDefinition = "varchar(200)", nullable = false)
    private String nombre;
    @Column(columnDefinition = "varchar(200)", nullable = false)
    private String marca;
    @Column(nullable = false)
    private Double costo;
    @Column(name = "cantidad_disponible", nullable = false)
    private Double cantidadDisponible;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "productos_ventas",
            joinColumns = @JoinColumn(name = "producto_id"),
            inverseJoinColumns = @JoinColumn(name = "venta_id"))
    private List<Venta> ventas = new ArrayList<>();

    public void agregarVenta(Venta venta) {
        this.ventas.add(venta);
    }
}