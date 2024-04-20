package com.example.demo.dtos.responseDTOs.ventaDTOs;

import com.example.demo.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.example.demo.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
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
public class VentaResponseDTO {
    private Long ventaId;
    private LocalDate fechaVenta;
    private Double total;
    private List<ProductoResponseDTO> productos;
    private ClienteResponseDTO cliente;
}
