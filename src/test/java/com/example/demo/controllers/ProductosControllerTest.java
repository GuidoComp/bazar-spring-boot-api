package com.example.demo.controllers;

import com.example.demo.dtos.requestDTOs.productoDTOs.AddProductoDTO;
import com.example.demo.dtos.requestDTOs.productoDTOs.UpdateProductoDTO;
import com.example.demo.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.services.IClienteService;
import com.example.demo.services.IProductoService;
import com.example.demo.services.IVentaService;
import com.example.demo.utils.ErrorMsgs;
import com.example.demo.utils.GenericModelMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//mock de la capa de ProductosControllers.
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProductosController.class)
class ProductosControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simula las peticiones HTTP

    @MockBean
    private IProductoService productoService; // Simula la capa de servicios (hay que mockear cada componente inyectado en el controller)

    @MockBean
    private IVentaService ventaService;

    @MockBean
    private IClienteService clienteService;

    @Mock
    private GenericModelMapper modelMapper;

    @Test
    void deberia_devolver_productos() throws Exception {
        List<ProductoResponseDTO> mockProductos = Arrays.asList(
                new ProductoResponseDTO(1L, "nombre 1", "marca 1", 100.0, 10.0),
                new ProductoResponseDTO(2L, "nombre 2", "marca 2", 200.0, 20.0),
                new ProductoResponseDTO(3L, "nombre 2", "marca 2", 300.0, 30.0)
        );
        when(productoService.getProductos()).thenReturn(mockProductos);

        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));

        verify(productoService).getProductos();
    }

    @Test
    @DisplayName("Si no hay Productos, debería devolver una lista vacía de productos")
    void getProductsEmptyList() throws Exception {
        when(productoService.getProductos()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.empty()));

        verify(productoService).getProductos();
    }

    @Test
    @DisplayName("Debería agregar un producto y devolver un ProductoDTO")
    void shouldAddProduct() throws Exception {
        AddProductoDTO addProductoDTO = new AddProductoDTO("nombre 1", "marca 1", 100.0, 10.0);
        ProductoResponseDTO productoResponseDTO = new ProductoResponseDTO(1L, "nombre 1", "marca 1", 100.0, 10.0);

        when(productoService.addProducto(any(AddProductoDTO.class))).thenReturn(productoResponseDTO);

        ArgumentCaptor<AddProductoDTO> addProductoDTOArgumentCaptor = ArgumentCaptor.forClass(AddProductoDTO.class);

        String jsonBody = "{\"nombre\":\"nombre 1\",\"marca\":\"marca 1\",\"costo\":100.0,\"cantidadDisponible\":10.0}";

        // Act
        mockMvc.perform(post("/productos/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("nombre 1"))
                .andExpect(jsonPath("$.marca").value("marca 1"))
                .andExpect(jsonPath("$.costo").value(100.0))
                .andExpect(jsonPath("$.cantidadDisponible").value(10.0));

        verify(productoService).addProducto(addProductoDTOArgumentCaptor.capture());
        AddProductoDTO addProductoDTOArgumentCaptorValue = addProductoDTOArgumentCaptor.getValue();
        assertEquals(addProductoDTOArgumentCaptorValue.getNombre(), addProductoDTO.getNombre());
    }

    @Test
    @DisplayName("Debería fallar cuando los datos del producto son inválidos")
    void shouldFailWhenProductDataIsInvalid() throws Exception {
        String jsonBody = "{\"nombre\":null,\"marca\":\"\",\"costo\":-100.0,\"cantidadDisponible\":-10.0}";

        mockMvc.perform(post("/productos/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[*].field").value(containsInAnyOrder("nombre", "marca", "costo", "cantidadDisponible")))
                .andExpect(jsonPath("$.errors[*].message").value(containsInAnyOrder(
                        ErrorMsgs.COSTO_NEGATIVO,
                        ErrorMsgs.MARCA_LENGTH,
                        ErrorMsgs.NOMBRE_PRODUCTO_REQUIRED,
                        ErrorMsgs.CANTIDAD_NEGATIVA
                )));

        verify(productoService, never()).addProducto(any(AddProductoDTO.class));
    }

    @Test
    @DisplayName("Debería eliminar un producto")
    void shouldDeleteAProduct() throws Exception {
        Long id = 7L;

        when(productoService.deleteProducto(id)).thenReturn(new ProductoResponseDTO(7L, "Cubiertos", "Rolito", 75000.0, 10.0));

        mockMvc.perform(delete("/productos/delete/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Producto eliminado correctamente"));

        verify(productoService).deleteProducto(id);
    }

    @Test
    @DisplayName("Debería devolver Not Found cuando el producto no existe")
    void shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {
        Long productId = 1L;

        when(productoService.deleteProducto(productId)).thenThrow(new ResourceNotFoundException(ErrorMsgs.PRODUCTO_NOT_FOUND + " con el id " + productId));

        mockMvc.perform(delete("/productos/delete/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ErrorMsgs.PRODUCTO_NOT_FOUND + " con el id " + productId));

        verify(productoService).deleteProducto(productId);
    }

    @Test
    @DisplayName("Debería actualizar un producto")
    void shouldUpdateProduct() throws Exception {
        Long id = 7L;
        UpdateProductoDTO updateProductoDTO = new UpdateProductoDTO("nombre 1", "marca 1", 100.0, 10.0);
        ProductoResponseDTO productoResponseDTO = new ProductoResponseDTO(1L, "nombre 1", "marca 1", 100.0, 10.0);

        when(productoService.updateProducto(eq(id), any(UpdateProductoDTO.class))).thenReturn(productoResponseDTO);

        ArgumentCaptor<UpdateProductoDTO> updateProductoDTOArgumentCaptor = ArgumentCaptor.forClass(UpdateProductoDTO.class);

        String jsonBody = "{\"nombre\":\"nombre 1\",\"marca\":\"marca 1\",\"costo\":100.0,\"cantidadDisponible\":10.0}";

        // Act
        mockMvc.perform(put("/productos/edit/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("nombre 1"))
                .andExpect(jsonPath("$.marca").value("marca 1"))
                .andExpect(jsonPath("$.costo").value(100.0))
                .andExpect(jsonPath("$.cantidadDisponible").value(10.0));

        verify(productoService).updateProducto(eq(id), updateProductoDTOArgumentCaptor.capture());

        UpdateProductoDTO productoCapturado = updateProductoDTOArgumentCaptor.getValue();
        assertEquals(updateProductoDTO.getNombre(), productoCapturado.getNombre());
    }

    @Test
    @DisplayName("Debería fallar cuando los datos del producto son inválidos")
    void shouldFailWhenProductDataIsInvalidInUpdate() throws Exception {
        String jsonBody = "{\"nombre\":\"a\", \"marca\":\"chispita\", \"costo\":-100.0, \"cantidadDisponible\":-10.0}";
        Long id = 7L;

        mockMvc.perform(put("/productos/edit/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[*].field").value(containsInAnyOrder("nombre", "costo", "cantidadDisponible")))
                .andExpect(jsonPath("$.errors[*].message").value(containsInAnyOrder(
                        ErrorMsgs.COSTO_NEGATIVO,
                        ErrorMsgs.NOMBRE_PRODUCTO_LENGTH,
                        ErrorMsgs.CANTIDAD_NEGATIVA
                )));

        verify(productoService, never()).addProducto(any(AddProductoDTO.class));
    }

    @Test
    @DisplayName("Al actualizar, deberia respetar las validaciones no nulleables")
    void shouldFailWhenProductDataIsInvalidInUpdatee() throws Exception {
        String jsonBody = "{\"nombre\":null,\"marca\":\"\",\"costo\":-100.0,\"cantidadDisponible\":-10.0}";
        Long id = 7L;

        mockMvc.perform(put("/productos/edit/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());

        verify(productoService, never()).addProducto(any(AddProductoDTO.class));
    }
}