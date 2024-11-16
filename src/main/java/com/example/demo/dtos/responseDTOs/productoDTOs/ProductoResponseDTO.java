package com.example.demo.dtos.responseDTOs.productoDTOs;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponseDTO {
    private Long productoId;
    private String nombre;
    private String marca;
    private Double costo;
    private Double cantidadDisponible;
}
