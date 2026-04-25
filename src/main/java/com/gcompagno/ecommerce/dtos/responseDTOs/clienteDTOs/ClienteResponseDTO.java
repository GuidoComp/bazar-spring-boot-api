package com.gcompagno.ecommerce.dtos.responseDTOs.clienteDTOs;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClienteResponseDTO {
    private Long clienteId;
    private String nombre;
    private String apellido;
    private String dni;
}
