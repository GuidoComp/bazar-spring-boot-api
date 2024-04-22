package com.example.demo.services;

import com.example.demo.dtos.requestDTOs.productoDTOs.AddProductoDTO;
import com.example.demo.dtos.requestDTOs.productoDTOs.UpdateProductoDTO;
import com.example.demo.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
import com.example.demo.models.Producto;

import java.util.List;

public interface IProductoService {
    List<ProductoResponseDTO> getProductos();

    ProductoResponseDTO addProducto(AddProductoDTO addProductoDTO);

    ProductoResponseDTO deleteProducto(Long id);

    ProductoResponseDTO updateProducto(Long id, UpdateProductoDTO updateProductoDTO);

    List<Producto> getProductosByIds(List<Long> idsProductos);

    List<ProductoResponseDTO> getProductosConStockBajo();
}
