package com.example.demo.dtos.requestDTOs.ventaDTOs;

import com.example.demo.utils.ErrorMsgs;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateVentaDTO {

    private LocalDate fechaVenta;

    @Positive(message = ErrorMsgs.VENTA_TOTAL_POSITIVE)
    private Double total;
}
