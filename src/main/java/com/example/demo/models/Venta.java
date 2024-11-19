package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Entity(name = "Ventas")
@Builder
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "venta_id")
    private Long ventaId;
    @Column(name = "fecha_venta")
    private LocalDate fechaVenta;
    private Double total;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // si guardo o actualizo una venta, se guardan o actualizan los productos asociados
    @JoinTable(name = "productos_ventas",
            joinColumns = @JoinColumn(name = "venta_id"),
            inverseJoinColumns = @JoinColumn(name = "producto_id"))
    private List<Producto> productos;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // si guardo o actualizo una venta, se guardan o actualizan los clientes asociados
    @JoinColumn(name = "cliente_id", referencedColumnName = "cliente_id")
    private Cliente cliente;

    public Venta() {
        this.productos = new ArrayList<>();
    }

    public Venta(Cliente cliente, Double total, LocalDate fechaVenta, Long ventaId) {
        this();
        this.cliente = cliente;
        this.total = total;
        this.fechaVenta = fechaVenta;
        this.ventaId = ventaId;
    }

//    public void borrarProductos() {
//        setTotal(0.0);
//        for(Producto p: productos) {
//            p.setCantidadDisponible(p.getCantidadDisponible() + 1);
//            p.quitarVenta(this);
//        }
//        productos.clear();
//    }
}
