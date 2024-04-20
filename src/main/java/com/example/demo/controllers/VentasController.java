package com.example.demo.controllers;

import com.example.demo.dtos.requestDTOs.clienteDTOs.AddClienteDTO;
import com.example.demo.dtos.requestDTOs.clienteDTOs.UpdateClienteDTO;
import com.example.demo.dtos.requestDTOs.ventaDTOs.AddVentaDTO;
import com.example.demo.dtos.requestDTOs.ventaDTOs.UpdateVentaDTO;
import com.example.demo.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.example.demo.dtos.responseDTOs.ventaDTOs.VentaResponseDTO;
import com.example.demo.services.IClienteService;
import com.example.demo.services.IProductoService;
import com.example.demo.services.IVentaService;
import com.example.demo.utils.ResponseWithMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentasController {
    private final IProductoService productoService;
    private final IVentaService ventaService;
    private final IClienteService clienteService;

    public VentasController(IProductoService productoService, IVentaService ventaService, IClienteService clienteService) {
        this.productoService = productoService;
        this.ventaService = ventaService;
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<VentaResponseDTO>> getVentas() {
        return ResponseEntity.ok(ventaService.getVentas());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addVenta(@Valid @RequestBody AddVentaDTO ventaRequestDTO) {
        VentaResponseDTO ventaResponseDTO = this.ventaService.addVenta(ventaRequestDTO);
        ResponseWithMessage<VentaResponseDTO> response = new ResponseWithMessage<>(ventaResponseDTO, "Venta agregada exitosamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVenta(@PathVariable Long id) {
        VentaResponseDTO ventaDTO = this.ventaService.deleteVenta(id);
        ResponseWithMessage<VentaResponseDTO> response = new ResponseWithMessage<>(ventaDTO, "Venta eliminada exitosamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editVenta(@PathVariable Long id, @Valid @RequestBody UpdateVentaDTO requestVentaDTO) {
        VentaResponseDTO ventaResponseDTO = this.ventaService.updateVenta(id, requestVentaDTO);
        ResponseWithMessage<VentaResponseDTO> response = new ResponseWithMessage<>(ventaResponseDTO, "Venta editada exitosamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
