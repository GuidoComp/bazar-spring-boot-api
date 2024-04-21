package com.example.demo.dtos.responseDTOs.productoDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponseDTO {
    private Long productoId;
    private String nombre;
    private String marca;
    private Double costo;
    private Double cantidadDisponible;
}
