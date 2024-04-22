package com.example.demo.dtos.requestDTOs.ventaDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateVentaDTO {
    private LocalDate fechaVenta;

    private List<Long> idsProductos;
    private Long idCliente;
}
