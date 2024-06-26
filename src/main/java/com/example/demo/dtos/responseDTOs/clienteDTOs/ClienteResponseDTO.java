package com.example.demo.dtos.responseDTOs.clienteDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {
    private Long clienteId;
    private String nombre;
    private String apellido;
    private String dni;
}
