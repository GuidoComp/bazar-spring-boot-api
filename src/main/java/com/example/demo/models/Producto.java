package com.example.demo.models;

import jakarta.persistence.*;
import lombok.*;

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
    @ManyToOne
    @JoinColumn(name = "venta_id")
    private Venta venta;
}