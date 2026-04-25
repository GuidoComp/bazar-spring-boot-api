package com.gcompagno.ecommerce.controllers;

import com.gcompagno.ecommerce.dtos.requestDTOs.ventaDTOs.AddVentaDTO;
import com.gcompagno.ecommerce.dtos.requestDTOs.ventaDTOs.UpdateVentaDTO;
import com.gcompagno.ecommerce.dtos.responseDTOs.ventaDTOs.VentaResponseDTO;
import com.gcompagno.ecommerce.services.IClienteService;
import com.gcompagno.ecommerce.services.IProductoService;
import com.gcompagno.ecommerce.services.IVentaService;
import com.gcompagno.ecommerce.utils.ResponseWithMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/ventas")
@Tag(name = "Ventas", description = "Operaciones sobre ventas y reportes asociados. Todos los endpoints requieren rol ADMIN.")
//@PreAuthorize("hasRole('ADMIN')")
@PreAuthorize("hasAuthority('ADMIN')")
public class VentasController {
    private final IProductoService productoService;
    private final IVentaService ventaService;
    private final IClienteService clienteService;

    public VentasController(IProductoService productoService, IVentaService ventaService, IClienteService clienteService) {
        this.productoService = productoService;
        this.ventaService = ventaService;
        this.clienteService = clienteService;
    }

    @Operation(summary = "Listar todas las ventas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de ventas"),
            @ApiResponse(responseCode = "401", description = "Falta token"),
            @ApiResponse(responseCode = "403", description = "Sin rol ADMIN")
    })
    //@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<VentaResponseDTO>> getVentas() {
        return ResponseEntity.ok(ventaService.getVentas());
    }

    @Operation(summary = "Crear una venta", description = "Descuenta stock de los productos referenciados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venta creada"),
            @ApiResponse(responseCode = "400", description = "Body inválido"),
            @ApiResponse(responseCode = "401", description = "Falta token"),
            @ApiResponse(responseCode = "403", description = "Sin rol ADMIN"),
            @ApiResponse(responseCode = "404", description = "Cliente o producto referenciado no existe")
    })
    //@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> addVenta(@Valid @RequestBody AddVentaDTO ventaRequestDTO) {
        VentaResponseDTO ventaResponseDTO = this.ventaService.addVenta(ventaRequestDTO);
        ResponseWithMessage<VentaResponseDTO> response = new ResponseWithMessage<>(ventaResponseDTO, "Venta agregada exitosamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Eliminar una venta por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venta eliminada"),
            @ApiResponse(responseCode = "401", description = "Falta token"),
            @ApiResponse(responseCode = "403", description = "Sin rol ADMIN"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    //@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVenta(@PathVariable Long id) {
        VentaResponseDTO ventaDTO = this.ventaService.deleteVenta(id);
        ResponseWithMessage<VentaResponseDTO> response = new ResponseWithMessage<>(ventaDTO, "Venta eliminada exitosamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Editar una venta por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venta actualizada"),
            @ApiResponse(responseCode = "400", description = "Body inválido"),
            @ApiResponse(responseCode = "401", description = "Falta token"),
            @ApiResponse(responseCode = "403", description = "Sin rol ADMIN"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    //@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editVenta(@PathVariable Long id, @Valid @RequestBody UpdateVentaDTO requestVentaDTO) {
        VentaResponseDTO ventaResponseDTO = this.ventaService.updateVenta(id, requestVentaDTO);
        ResponseWithMessage<VentaResponseDTO> response = new ResponseWithMessage<>(ventaResponseDTO, "Venta editada exitosamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Listar productos de una venta", description = "Devuelve los productos asociados a la venta indicada.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de productos de la venta"),
            @ApiResponse(responseCode = "401", description = "Falta token"),
            @ApiResponse(responseCode = "403", description = "Sin rol ADMIN"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    //@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/productos/{idVenta}") //hacer la validación del idVenta
    public ResponseEntity<?> getProductosDeVenta(@PathVariable Long idVenta) {
        return ResponseEntity.ok(this.ventaService.getProductosDTODeVenta(idVenta));
    }

    @Operation(summary = "Monto y cantidad totales de ventas en una fecha", description = "Formato de fecha: ISO yyyy-MM-dd.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Totales calculados"),
            @ApiResponse(responseCode = "401", description = "Falta token"),
            @ApiResponse(responseCode = "403", description = "Sin rol ADMIN"),
            @ApiResponse(responseCode = "404", description = "No hay ventas para esa fecha")
    })
    //@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{fecha_venta}")
    public ResponseEntity<?> getMontoYCantidadTotales(@PathVariable LocalDate fecha_venta) {
        return ResponseEntity.ok(this.ventaService.getMontoYCantidadTotales(fecha_venta));
    }

    @Operation(summary = "Información de la mayor venta", description = "Devuelve datos de la venta de mayor monto registrada.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Datos de la mayor venta"),
            @ApiResponse(responseCode = "401", description = "Falta token"),
            @ApiResponse(responseCode = "403", description = "Sin rol ADMIN"),
            @ApiResponse(responseCode = "404", description = "No hay ventas registradas")
    })
    //@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/mayor_venta")
    public ResponseEntity<?> getInfoMayorVenta() {
        return ResponseEntity.ok(this.ventaService.getInfoMayorVenta());
    }
}
