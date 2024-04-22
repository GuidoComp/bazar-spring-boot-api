package com.example.demo.dtos.responseDTOs.ventaDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InfoMayorVenta {
    private Long idVenta;
    private Double total;
    private int cantProductos;
    private String nombreCliente;
    private String apellidoCliente;
}
