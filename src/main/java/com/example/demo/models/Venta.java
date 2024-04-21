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
    @ManyToMany(mappedBy = "ventas")
    private List<Producto> productos = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "cliente_id")
    private Cliente cliente;

    public void agregarCliente(Cliente clienteById) {
        this.cliente = clienteById;
        cliente.agregarVenta(this); //No es necesario, con setear un lado de la relación es suficiente, aunque mejor explicitarlo
    }

    public void agregarProducto(Producto producto) throws NoStockException {
        this.checkStock(producto);
        producto.setCantidadDisponible(producto.getCantidadDisponible() - 1);
        this.productos.add(producto);
        producto.agregarVenta(this);
    }

    private void checkStock(Producto producto) {
        if (producto.getCantidadDisponible() == 0) {
            throw new NoStockException(String.format(ErrorMsgs.PRODUCTO_SIN_STOCK, producto.getProductoId(), producto.getNombre(), producto.getMarca()));
        }
    }
}
