package com.example.demo.controllers;

import com.example.demo.dtos.requestDTOs.clienteDTOs.AddClienteDTO;
import com.example.demo.dtos.requestDTOs.clienteDTOs.UpdateClienteDTO;
import com.example.demo.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.example.demo.services.IClienteService;
import com.example.demo.services.IProductoService;
import com.example.demo.services.IVentaService;
import com.example.demo.utils.ResponseWithMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClientesController {

    private final IProductoService productoService;
    private final IVentaService ventaService;
    private final IClienteService clienteService;

    public ClientesController(IProductoService productoService, IVentaService ventaService, IClienteService clienteService) {
        this.productoService = productoService;
        this.ventaService = ventaService;
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> getClientes() {
        return ResponseEntity.ok(clienteService.getClientes());
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> addCliente(@Valid @RequestBody AddClienteDTO clienteRequestDTO) {
        ClienteResponseDTO clienteResponseDTO = this.clienteService.addCliente(clienteRequestDTO);
        ResponseWithMessage<ClienteResponseDTO> response = new ResponseWithMessage<>(clienteResponseDTO, "Cliente agregado exitosamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCliente(@PathVariable Long id) {
        ClienteResponseDTO cliDTO = this.clienteService.deleteCliente(id);
        ResponseWithMessage<ClienteResponseDTO> response = new ResponseWithMessage<>(cliDTO, "Cliente eliminado exitosamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editCliente(@PathVariable Long id, @Valid @RequestBody UpdateClienteDTO requestClientDTO) {
        ClienteResponseDTO clienteResponseDTO = this.clienteService.updateCliente(id, requestClientDTO);
        ResponseWithMessage<ClienteResponseDTO> response = new ResponseWithMessage<>(clienteResponseDTO, "Cliente editado exitosamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
