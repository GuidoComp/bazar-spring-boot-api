package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Ventas")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "venta_id")
    private Long ventaId;
    @Column(name = "fecha_venta")
    private LocalDate fechaVenta;
    private Double total;
    @OneToMany(mappedBy = "venta", fetch = FetchType.LAZY)
    private List<Producto> productos = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "cliente_id")
    private Cliente cliente;

    public void agregarCliente(Cliente clienteById) {
        this.cliente = clienteById;
        cliente.agregarVenta(this);
    }

    public void agregarProductos(List<Producto> productosByIds) {
        this.productos = productosByIds;
        productos.forEach(producto -> producto.setVenta(this));
    }
}
