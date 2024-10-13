package com.example.demo.controllers;

import com.example.demo.dtos.requestDTOs.clienteDTOs.AddClienteDTO;
import com.example.demo.dtos.requestDTOs.productoDTOs.AddProductoDTO;
import com.example.demo.services.IClienteService;
import com.example.demo.services.IProductoService;
import com.example.demo.utils.ErrorMsgs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest //Pruebas de integración, carga componentes.
@AutoConfigureMockMvc(addFilters = false)
public class ClientesControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IClienteService clienteService;

    @BeforeEach
    void setUp() {
        clienteService.deleteAllClientes();
        AddClienteDTO addClienteDTO = new AddClienteDTO("nombre 1", "apellido 1", "36158199");
        clienteService.addCliente(addClienteDTO);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getClientes() throws Exception {
        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void addCliente() throws Exception {
        String jsonBody = "{\"nombre\": \"nombre 2\", \"apellido\": \"apellido 2\", \"dni\": \"36158200\"}";
        mockMvc.perform(post("/clientes/add")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nombre").value("nombre 2"))
                .andExpect(jsonPath("$.data.apellido").value("apellido 2"))
                .andExpect(jsonPath("$.data.dni").value("36158200"))
                .andExpect(jsonPath("$.message").value("Cliente agregado exitosamente"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void addClienteFail() throws Exception {
        String jsonBody = "{\"nombre\": \"\", \"apellido\": \"a\", \"dni\": \"\"}";
        mockMvc.perform(post("/clientes/add")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").exists());
//                .andExpect(jsonPath("$.errors[*].field").value(containsInAnyOrder("nombre", "apellido", "dni")))
//                .andExpect(jsonPath("$.errors[*].message").value(containsInAnyOrder(
//                        ErrorMsgs.NOMBRE_CLIENTE_NO_VACIO,
//                        ErrorMsgs.APELLIDO_CLIENTE_MIN_LENGTH,
//                        ErrorMsgs.DNI_CLIENTE_REQUERIDO
//                )));
    }

//    @Test
//    @WithMockUser(authorities = "ADMIN")
//    void deleteClient() throws Exception {
//        mockMvc.perform(delete("/clientes/delete/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Cliente eliminado exitosamente"));
//    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteClientFail() throws Exception {
        mockMvc.perform(delete("/clientes/delete/100"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format(ErrorMsgs.CLIENTE_NOT_FOUND, 100)));
    }

//    @Test
//    @WithMockUser(authorities = "ADMIN")
//    void updateClient() throws Exception {
//        String jsonBody = "{\"nombre\": \"nombre 1\", \"apellido\": \"apellido 1\", \"dni\": \"36158200\"}";
//        mockMvc.perform(put("/clientes/edit/1")
//                        .content(jsonBody)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.nombre").value("nombre 1"))
//                .andExpect(jsonPath("$.apellido").value("apellido 1"))
//                .andExpect(jsonPath("$.dni").value("36158199"));
//    }
}
