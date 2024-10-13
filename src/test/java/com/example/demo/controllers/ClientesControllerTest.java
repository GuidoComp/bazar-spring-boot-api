package com.example.demo.controllers;

import com.example.demo.dtos.requestDTOs.clienteDTOs.AddClienteDTO;
import com.example.demo.dtos.requestDTOs.clienteDTOs.UpdateClienteDTO;
import com.example.demo.dtos.responseDTOs.clienteDTOs.ClienteResponseDTO;
import com.example.demo.services.*;
import com.example.demo.utils.GenericModelMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ClientesController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClientesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProductoService productoService;

    @MockBean
    private IVentaService ventaService;

    @MockBean
    private IClienteService clienteService;

    @Mock
    private GenericModelMapper modelMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getClientes() throws Exception {
        List<ClienteResponseDTO> clientesDTO = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            clientesDTO.add(new ClienteResponseDTO((long) i + 1, String.format("nombre %s", (i+1)), String.format("apellido %s", (i+1)), String.format("1526355%s", i)));
        }
        when(clienteService.getClientes()).thenReturn(clientesDTO);

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(5)));

        verify(clienteService).getClientes();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getVoidClients() {
        List<ClienteResponseDTO> clientesDTO = new ArrayList<>();
        when(clienteService.getClientes()).thenReturn(clientesDTO);

        List<ClienteResponseDTO> result = clienteService.getClientes();
        assertEquals(0, result.size());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void addCliente() throws Exception {
        AddClienteDTO clienteRequestDTO = new AddClienteDTO("Roberto", "Canessa", "26598441");
        ObjectMapper objectMapper = new ObjectMapper(); //biblioteca ObjectMapper de Jackson para procesar JSON, convierte un objecto en un json.
        String jsonBody = objectMapper.writeValueAsString(clienteRequestDTO);

        ClienteResponseDTO clienteResponseDTO = new ClienteResponseDTO(1L, "Roberto", "Canessa", "26598441");
        when(clienteService.addCliente(any(AddClienteDTO.class))).thenReturn(clienteResponseDTO);

        mockMvc.perform(post("/clientes/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.clienteId").value(clienteResponseDTO.getClienteId()))
                .andExpect(jsonPath("$.data.nombre").value(clienteResponseDTO.getNombre()))
                .andExpect(jsonPath("$.data.apellido").value(clienteResponseDTO.getApellido()))
                .andExpect(jsonPath("$.data.dni").value(clienteResponseDTO.getDni()))
                .andExpect(jsonPath("$.message").value("Cliente agregado exitosamente"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteCliente() throws Exception {
        Long id = 1L;
        ClienteResponseDTO cliDTO = new ClienteResponseDTO(1L, "Roberto", "Canessa", "26598441");
        when(clienteService.deleteCliente(id)).thenReturn(cliDTO);

        mockMvc.perform(delete("/clientes/delete/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.clienteId").value(cliDTO.getClienteId()))
                .andExpect(jsonPath("$.data.nombre").value(cliDTO.getNombre()))
                .andExpect(jsonPath("$.data.apellido").value(cliDTO.getApellido()))
                .andExpect(jsonPath("$.data.dni").value(cliDTO.getDni()))
                .andExpect(jsonPath("$.message").value("Cliente eliminado exitosamente"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void editCliente() throws Exception {
        UpdateClienteDTO cliReqDTO = new UpdateClienteDTO("Marcelo", "Canessa", "26598441");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(cliReqDTO);

        ClienteResponseDTO cliResDTO = new ClienteResponseDTO(1L, "Marcelo", "Canessa", "26598441");

        when(clienteService.updateCliente(any(Long.class), any(UpdateClienteDTO.class))).thenReturn(cliResDTO);

        mockMvc.perform(put("/clientes/edit/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.clienteId").value(cliResDTO.getClienteId()))
                .andExpect(jsonPath("$.data.nombre").value(cliReqDTO.getNombre()))
                .andExpect(jsonPath("$.data.apellido").value(cliReqDTO.getApellido()))
                .andExpect(jsonPath("$.data.dni").value(cliReqDTO.getDni()))
                .andExpect(jsonPath("$.message").value("Cliente editado exitosamente"));
    }
}