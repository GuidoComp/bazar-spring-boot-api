package com.example.demo.dtos.requestDTOs.clienteDTOs;

import com.example.demo.utils.ErrorMsgs;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClienteDTO {
    @Length(min = 3, message = ErrorMsgs.NOMBRE_CLIENTE_MIN_LENGTH)
    private String nombre;
    @Length(min = 2, message = ErrorMsgs.APELLIDO_CLIENTE_MIN_LENGTH)
    private String apellido;
    @Positive(message = ErrorMsgs.DNI_CLIENTE_NEGATIVO)
    @Length(min = 8, max = 8, message = ErrorMsgs.DNI_LENGTH)
    private String dni;
}
