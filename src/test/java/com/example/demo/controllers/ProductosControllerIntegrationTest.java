package com.example.demo.controllers;

import com.example.demo.utils.ErrorMsgs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc //Spring configura MockMVC sin necesidad de crear Mocks
class ProductosControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getProductos() throws Exception {
        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void addProducto() throws Exception {
        String jsonBody = "{\"nombre\": \"nombre 1\", \"marca\": \"marca 1\", \"costo\": 100.0, \"cantidadDisponible\": 10.0}";
        mockMvc.perform(post("/productos/add")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre"  ).value("nombre 1"))
                .andExpect(jsonPath("$.marca"  ).value("marca 1"))
                .andExpect(jsonPath("$.costo"  ).value(100.0))
                .andExpect(jsonPath("$.cantidadDisponible"  ).value(10.0));
    }

    @Test
    void addProductoFail() throws Exception {
        String jsonBody = "{\"nombre\": \"\", \"marca\": null, \"costo\": -100.0, \"cantidadDisponible\": 10.0}";
        mockMvc.perform(post("/productos/add")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[*].field").value(containsInAnyOrder("nombre", "marca", "costo")))
                .andExpect(jsonPath("$.errors[*].message").value(containsInAnyOrder(
                        ErrorMsgs.COSTO_NEGATIVO,
                        ErrorMsgs.NOMBRE_PRODUCTO_LENGTH,
                        ErrorMsgs.MARCA_REQUIRED
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

    @Test
    void updateProductFailNotFound() throws Exception {
        String jsonBody = "{\"nombre\": \"nombre 1\", \"marca\": \"marca 1\", \"costo\": 100.0, \"cantidadDisponible\": 10.0}";
        //String jsonBody = "{\"nombre\": \"\", \"marca\": null, \"costo\": -100.0, \"cantidadDisponible\": 10.0}";
        Long id = 1L;
        mockMvc.perform(put("/productos/edit/{id}", id)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    void shouldUpdateOnlyValidAttributes() throws Exception {
        String jsonBody = "{\"marca\": \"Ayudín\", \"costo\": 100.0}";
        Long id = 9L;
        mockMvc.perform(put("/productos/edit/{id}", id)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre"  ).exists())
                .andExpect(jsonPath("$.marca"  ).value("Ayudín"))
                .andExpect(jsonPath("$.costo"  ).value(100.0))
                .andExpect(jsonPath("$.cantidadDisponible"  ).exists());
    }
}