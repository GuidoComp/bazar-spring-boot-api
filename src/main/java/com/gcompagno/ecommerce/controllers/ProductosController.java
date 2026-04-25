package com.gcompagno.ecommerce.controllers;

import com.gcompagno.ecommerce.dtos.requestDTOs.productoDTOs.AddProductoDTO;
import com.gcompagno.ecommerce.dtos.requestDTOs.productoDTOs.UpdateProductoDTO;
import com.gcompagno.ecommerce.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
import com.gcompagno.ecommerce.services.IClienteService;
import com.gcompagno.ecommerce.services.IProductoService;
import com.gcompagno.ecommerce.services.IVentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
@Tag(name = "Productos", description = "Catálogo de productos. Lectura abierta; alta/edición/baja y reportes solo ADMIN.")
public class ProductosController {

    private final IProductoService productoService;
    private final IVentaService ventaService;
    private final IClienteService clienteService;

    public ProductosController(IProductoService productoService, IVentaService ventaService, IClienteService clienteService) {
        this.productoService = productoService;
        this.ventaService = ventaService;
        this.clienteService = clienteService;
    }

    @Operation(summary = "Listar todos los productos", description = "Endpoint público (no requiere token).")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de productos"))
    @SecurityRequirements
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> getProductos() {
        return ResponseEntity.ok(productoService.getProductos());
    }

    @Operation(summary = "Crear un producto", description = "Requiere rol ADMIN. La combinación nombre+marca debe ser única.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto creado"),
            @ApiResponse(responseCode = "400", description = "Body inválido"),
            @ApiResponse(responseCode = "401", description = "Falta token"),
            @ApiResponse(responseCode = "403", description = "Sin rol ADMIN"),
            @ApiResponse(responseCode = "409", description = "Producto (nombre+marca) ya existe")
    })
    //@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ProductoResponseDTO> addProducto(@Valid @RequestBody AddProductoDTO addProductoDTO) {
        return ResponseEntity.ok(productoService.addProducto(addProductoDTO));
    }

    @Operation(summary = "Eliminar un producto por id", description = "Requiere rol ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto eliminado"),
            @ApiResponse(responseCode = "401", description = "Falta token"),
            @ApiResponse(responseCode = "403", description = "Sin rol ADMIN"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    //@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProducto(@PathVariable Long id) {
        productoService.deleteProducto(id);
        return ResponseEntity.ok("Producto eliminado correctamente");
    }

    @Operation(summary = "Editar un producto por id", description = "Requiere rol ADMIN. Solo se actualizan los campos enviados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado"),
            @ApiResponse(responseCode = "400", description = "Body inválido"),
            @ApiResponse(responseCode = "401", description = "Falta token"),
            @ApiResponse(responseCode = "403", description = "Sin rol ADMIN"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    //@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<?> updateProducto(@PathVariable Long id, @Valid @RequestBody UpdateProductoDTO updateProductoDTO) {
        ProductoResponseDTO productoResponseDTO = productoService.updateProducto(id, updateProductoDTO);
        return ResponseEntity.ok(productoResponseDTO);
    }

    @Operation(summary = "Listar productos con stock bajo", description = "Requiere rol ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de productos con poco stock"),
            @ApiResponse(responseCode = "401", description = "Falta token"),
            @ApiResponse(responseCode = "403", description = "Sin rol ADMIN")
    })
    //@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/falta_stock")
    public ResponseEntity<?> getProductosConStockBajo() {
        return ResponseEntity.ok(productoService.getProductosConStockBajo());
    }
}
