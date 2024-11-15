package com.example.demo.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "Productos")
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"nombre", "marca"})
        }
)
@Builder
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
    @ManyToMany(mappedBy = "productos")
    private List<Venta> ventas;

    public Producto() {
        this.ventas = new ArrayList<>();
    }

    public Producto(Long productoId, String nombre, String marca, Double costo, Double cantidadDisponible) {
        this();
        this.productoId = productoId;
        this.nombre = nombre;
        this.marca = marca;
        this.costo = costo;
        this.cantidadDisponible = cantidadDisponible;
    }

    public void agregarVenta(Venta venta) {
        this.ventas.add(venta);
    }

    public void quitarVenta(Venta venta) {
        if (this.ventas != null) {
            this.ventas.remove(venta);
        }
    }
}