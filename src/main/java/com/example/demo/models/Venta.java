package com.example.demo.models;

import com.example.demo.exceptions.NoStockException;
import com.example.demo.utils.ErrorMsgs;
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
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(name = "productos_ventas",
            joinColumns = @JoinColumn(name = "venta_id"),
            inverseJoinColumns = @JoinColumn(name = "producto_id"))
    private List<Producto> productos = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "cliente_id")
    private Cliente cliente;

    public void agregarCliente(Cliente clienteById) {
        this.cliente = clienteById;
    }

    public void agregarProducto(Producto producto) {
        producto.setCantidadDisponible(producto.getCantidadDisponible() - 1);
        this.productos.add(producto);
        producto.agregarVenta(this);
    }

    public void borrarProductos() {
        setTotal(0.0);
        for(Producto p: productos) {
            p.setCantidadDisponible(p.getCantidadDisponible() + 1);
            p.quitarVenta(this);
        }
        productos.clear();
    }
}
