package com.example.demo.dtos.requestDTOs.ventaDTOs;
import com.example.demo.utils.ErrorMsgs;
import jakarta.validation.constraints.NotNull;
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
public class AddVentaDTO {
    @NotNull(message = ErrorMsgs.VENTA_FECHA_VENTA_NOT_NULL)
    private LocalDate fechaVenta;
    @NotNull(message = ErrorMsgs.VENTA_PRODUCTOS_NOT_NULL)
    private List<Long> idsProductos;
    @NotNull(message = ErrorMsgs.VENTA_CLIENTE_NOT_NULL)
    private Long idCliente;
}
