package com.example.demo.dtos.requestDTOs.ventaDTOs;
import com.example.demo.models.Cliente;
import com.example.demo.models.Producto;
import com.example.demo.utils.ErrorMsgs;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    @NotNull(message = ErrorMsgs.VENTA_TOTAL_NOT_NULL)
    @Positive(message = ErrorMsgs.VENTA_TOTAL_POSITIVE)
    private Double total;
    @NotNull(message = ErrorMsgs.VENTA_PRODUCTOS_NOT_NULL)
    private List<Long> idsProductos;
    @NotNull(message = ErrorMsgs.VENTA_CLIENTE_NOT_NULL)
    private Long idCliente;
}
