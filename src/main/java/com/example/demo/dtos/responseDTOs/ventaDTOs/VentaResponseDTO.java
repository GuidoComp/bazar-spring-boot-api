package com.example.demo.dtos.responseDTOs.ventaDTOs;

import com.example.demo.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.example.demo.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VentaResponseDTO {
    private Long ventaId;
    private LocalDate fechaVenta;
    private Double total;
    private List<ProductoResponseDTO> productos;
    private ClienteResponseDTO cliente;
}
