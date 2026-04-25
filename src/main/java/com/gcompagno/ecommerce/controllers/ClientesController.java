package com.gcompagno.ecommerce.controllers;

import com.gcompagno.ecommerce.dtos.requestDTOs.clienteDTOs.AddClienteDTO;
import com.gcompagno.ecommerce.dtos.requestDTOs.clienteDTOs.UpdateClienteDTO;
import com.gcompagno.ecommerce.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
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

import java.util.List;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "Gestión de clientes del bazar. Lectura abierta a usuarios autenticados; escritura solo ADMIN.")
public class ClientesController {

    private final IProductoService productoService;
    private final IVentaService ventaService;
    private final IClienteService clienteService;

    public ClientesController(IProductoService productoService, IVentaService ventaService, IClienteService clienteService) {
        this.productoService = productoService;
        this.ventaService = ventaService;
        this.clienteService = clienteService;
    }

    @Operation(summary = "Listar todos los clientes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de clientes"),
            @ApiResponse(responseCode = "401", description = "Falta token"),
            @ApiResponse(responseCode = "403", description = "Token sin permisos suficientes")
    })
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> getClientes() {
        return ResponseEntity.ok(clienteService.getClientes());
    }

    @Operation(summary = "Crear un cliente", description = "Requiere rol ADMIN. El DNI debe ser único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente creado"),
            @ApiResponse(responseCode = "400", description = "Body inválido"),
            @ApiResponse(responseCode = "401", description = "Falta token"),
            @ApiResponse(responseCode = "403", description = "Sin rol ADMIN"),
            @ApiResponse(responseCode = "409", description = "DNI ya registrado")
    })
    //@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> addCliente(@Valid @RequestBody AddClienteDTO clienteRequestDTO) {
        ClienteResponseDTO clienteResponseDTO = this.clienteService.addCliente(clienteRequestDTO);
        ResponseWithMessage<ClienteResponseDTO> response = new ResponseWithMessage<>(clienteResponseDTO, "Cliente agregado exitosamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Eliminar un cliente por id", description = "Requiere rol ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente eliminado"),
            @ApiResponse(responseCode = "401", description = "Falta token"),
            @ApiResponse(responseCode = "403", description = "Sin rol ADMIN"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    //@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCliente(@PathVariable Long id) {
        ClienteResponseDTO cliDTO = this.clienteService.deleteCliente(id);
        ResponseWithMessage<ClienteResponseDTO> response = new ResponseWithMessage<>(cliDTO, "Cliente eliminado exitosamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Editar un cliente por id", description = "Requiere rol ADMIN. Solo se actualizan los campos enviados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente actualizado"),
            @ApiResponse(responseCode = "400", description = "Body inválido"),
            @ApiResponse(responseCode = "401", description = "Falta token"),
            @ApiResponse(responseCode = "403", description = "Sin rol ADMIN"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    //@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editCliente(@PathVariable Long id, @Valid @RequestBody UpdateClienteDTO requestClientDTO) {
        ClienteResponseDTO clienteResponseDTO = this.clienteService.updateCliente(id, requestClientDTO);
        ResponseWithMessage<ClienteResponseDTO> response = new ResponseWithMessage<>(clienteResponseDTO, "Cliente editado exitosamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
