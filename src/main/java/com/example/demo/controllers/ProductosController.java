package com.example.demo.controllers;

import com.example.demo.dtos.requestDTOs.productoDTOs.AddProductoDTO;
import com.example.demo.dtos.requestDTOs.productoDTOs.UpdateProductoDTO;
import com.example.demo.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
import com.example.demo.services.IClienteService;
import com.example.demo.services.IProductoService;
import com.example.demo.services.IVentaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductosController {

    private final IProductoService productoService;
    private final IVentaService ventaService;
    private final IClienteService clienteService;

    public ProductosController(IProductoService productoService, IVentaService ventaService, IClienteService clienteService) {
        this.productoService = productoService;
        this.ventaService = ventaService;
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> getProductos() {
        return ResponseEntity.ok(productoService.getProductos());
    }

    @PostMapping("/add")
    public ResponseEntity<ProductoResponseDTO> addProducto(@Valid @RequestBody AddProductoDTO addProductoDTO) {
        return ResponseEntity.ok(productoService.addProducto(addProductoDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProducto(@PathVariable Long id) {
        productoService.deleteProducto(id);
        return ResponseEntity.ok("Producto eliminado correctamente");
    }

    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<?> updateProducto(@PathVariable Long id, @Valid @RequestBody UpdateProductoDTO updateProductoDTO) {
        ProductoResponseDTO productoResponseDTO = productoService.updateProducto(id, updateProductoDTO);
        return ResponseEntity.ok(productoResponseDTO);
    }

    @GetMapping("/falta_stock")
    public ResponseEntity<?> getProductosConStockBajo() {
        return ResponseEntity.ok(productoService.getProductosConStockBajo());
    }
}
