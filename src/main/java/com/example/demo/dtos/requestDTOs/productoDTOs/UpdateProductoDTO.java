package com.example.demo.dtos.requestDTOs.productoDTOs;

import com.example.demo.utils.ErrorMsgs;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductoDTO {
    @Length(min = 3, max = 200, message = ErrorMsgs.NOMBRE_PRODUCTO_LENGTH)
    private String nombre;
    @Length(min = 3, max = 200, message = ErrorMsgs.MARCA_LENGTH)
    private String marca;
    @Positive(message = ErrorMsgs.COSTO_NEGATIVO)
    private Double costo;
    @PositiveOrZero(message = ErrorMsgs.CANTIDAD_NEGATIVA)
    private Double cantidadDisponible;
}
