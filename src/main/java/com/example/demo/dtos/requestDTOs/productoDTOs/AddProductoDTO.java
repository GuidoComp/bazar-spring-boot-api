package com.example.demo.dtos.requestDTOs.productoDTOs;

import com.example.demo.utils.ErrorMsgs;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddProductoDTO {
    @NotNull(message = ErrorMsgs.NOMBRE_PRODUCTO_REQUIRED)
    @Length(min = 3, max = 200, message = ErrorMsgs.NOMBRE_PRODUCTO_LENGTH)
    private String nombre;
    @NotNull(message = ErrorMsgs.MARCA_REQUIRED)
    @Length(min = 3, max = 200, message = ErrorMsgs.MARCA_LENGTH)
    private String marca;
    @NotNull(message = ErrorMsgs.COSTO_REQUIRED)
    @Positive(message = ErrorMsgs.COSTO_NEGATIVO)
    private Double costo;
    @NotNull(message = ErrorMsgs.CANTIDAD_REQUIRED)
    @PositiveOrZero(message = ErrorMsgs.CANTIDAD_NEGATIVA)
    private Double cantidadDisponible;
}
