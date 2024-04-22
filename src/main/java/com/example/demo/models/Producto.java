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
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"nombre", "marca"})
        }
)
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
    @ManyToMany(mappedBy = "productos", cascade = CascadeType.ALL)
    private List<Venta> ventas = new ArrayList<>();

    public void agregarVenta(Venta venta) {
        this.ventas.add(venta);
    }

    public void quitarVenta(Venta venta) {
        this.ventas.remove(venta);
    }
}