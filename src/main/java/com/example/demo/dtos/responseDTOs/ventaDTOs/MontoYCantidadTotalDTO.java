package com.example.demo.dtos.responseDTOs.ventaDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MontoYCantidadTotalDTO {
    private LocalDate fecha;
    private Double montoTotal;
    private int cantidadVentas;
}
