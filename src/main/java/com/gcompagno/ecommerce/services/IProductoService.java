package com.gcompagno.ecommerce.services;

import com.gcompagno.ecommerce.dtos.requestDTOs.productoDTOs.AddProductoDTO;
import com.gcompagno.ecommerce.dtos.requestDTOs.productoDTOs.UpdateProductoDTO;
import com.gcompagno.ecommerce.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
import com.gcompagno.ecommerce.models.Producto;

import java.util.List;

public interface IProductoService {
    List<ProductoResponseDTO> getProductos();

    ProductoResponseDTO addProducto(AddProductoDTO addProductoDTO);

    ProductoResponseDTO deleteProducto(Long id);

    ProductoResponseDTO updateProducto(Long id, UpdateProductoDTO updateProductoDTO);

    List<Producto> getProductosByIds(List<Long> idsProductos);

    List<ProductoResponseDTO> getProductosConStockBajo();

    boolean productoExistente(String nombre, String marca);

    void checkStock(Producto p);
}
