package com.example.demo.controllers;

import com.example.demo.utils.ErrorMsgs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest //Pruebas de integración, carga componentes.
@AutoConfigureMockMvc
public class ClientesControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getClientes() throws Exception {
        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void addCliente() throws Exception {
        String jsonBody = "{\"nombre\": \"nombre 1\", \"apellido\": \"apellido 1\", \"dni\": \"36158199\"}";
        mockMvc.perform(post("/clientes/add")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nombre").value("nombre 1"))
                .andExpect(jsonPath("$.data.apellido").value("apellido 1"))
                .andExpect(jsonPath("$.data.dni").value("36158199"))
                .andExpect(jsonPath("$.message").value("Cliente agregado exitosamente"));
    }

    @Test
    void addClienteFail() throws Exception {
        String jsonBody = "{\"nombre\": \"\", \"apellido\": \"a\", \"dni\": null}";
        mockMvc.perform(post("/productos/add")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[*].field").value(containsInAnyOrder("nombre", "apellido", "dni")))
                .andExpect(jsonPath("$.errors[*].message").value(containsInAnyOrder(
                        ErrorMsgs.NOMBRE_CLIENTE_NO_VACIO,
                        ErrorMsgs.APELLIDO_CLIENTE_MIN_LENGTH,
                        ErrorMsgs.DNI_CLIENTE_REQUERIDO
                )));
    }

    @Test
    void deleteProduct() throws Exception {
        mockMvc.perform(delete("/productos/delete/9"))
                .andExpect(status().isOk())
                .andExpect(content().string("Producto eliminado correctamente"));
    }

    @Test
    void deleteProductFail() throws Exception {
        mockMvc.perform(delete("/productos/delete/100"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ErrorMsgs.PRODUCTO_NOT_FOUND + " con el id " + 100));
    }

    @Test
    void updateProduct() throws Exception {
        String jsonBody = "{\"nombre\": \"nombre 1\", \"marca\": \"marca 1\", \"costo\": 100.0, \"cantidadDisponible\": 10.0}";
        mockMvc.perform(put("/productos/edit/9")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre"  ).value("nombre 1"))
                .andExpect(jsonPath("$.marca"  ).value("marca 1"))
                .andExpect(jsonPath("$.costo"  ).value(100.0))
                .andExpect(jsonPath("$.cantidadDisponible"  ).value(10.0));
    }
}
