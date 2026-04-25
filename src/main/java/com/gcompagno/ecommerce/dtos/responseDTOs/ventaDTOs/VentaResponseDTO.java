package com.gcompagno.ecommerce.dtos.responseDTOs.ventaDTOs;

import com.gcompagno.ecommerce.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.gcompagno.ecommerce.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
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
