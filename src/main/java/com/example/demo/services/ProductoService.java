package com.example.demo.services;

import com.example.demo.dtos.requestDTOs.productoDTOs.AddProductoDTO;
import com.example.demo.dtos.requestDTOs.productoDTOs.UpdateProductoDTO;
import com.example.demo.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
import com.example.demo.exceptions.NoStockException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.RestrictException;
import com.example.demo.models.Producto;
import com.example.demo.repositories.IProductoRepository;
import com.example.demo.utils.AppVariables;
import com.example.demo.utils.ErrorMsgs;
import com.example.demo.utils.GenericModelMapper;
import com.example.demo.utils.IModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductoService implements IProductoService {
    private final IProductoRepository productoRepository;
    private final IModelMapper mapper;

    public ProductoService(IProductoRepository productoRepository, IModelMapper modelMapper) {
        this.productoRepository = productoRepository;
        this.mapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> getProductos() {
        List<Producto> productos = this.productoRepository.findAll();
        return mapper.mapProductosToDTO(productos);
    }

    @Override
    @Transactional
    public ProductoResponseDTO addProducto(AddProductoDTO addProductoDTO) {
        Producto producto = mapper.mapDTOToProducto(addProductoDTO);
        if (this.productoExistente(addProductoDTO.getNombre(), addProductoDTO.getMarca())) {
            throw new RestrictException(ErrorMsgs.PRODUCTO_YA_INGRESADO);
        }
        Producto p = productoRepository.save(producto);
        return mapper.mapProductoToDTO(p);
    }

    @Override
    @Transactional
    public ProductoResponseDTO deleteProducto(Long id) {
        Producto producto = productoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorMsgs.PRODUCTO_NOT_FOUND + " con el id " + id));
        if (!producto.getVentas().isEmpty()) {
            throw new RestrictException(ErrorMsgs.DELETE_PRODUCTO_RESTRICCION_FK);
        }
        productoRepository.delete(producto);
        return mapper.mapProductoToDTO(producto);
    }

    @Override
    @Transactional
    public ProductoResponseDTO updateProducto(Long id, UpdateProductoDTO updateProductoDTO) {
        Producto p = this.productoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorMsgs.PRODUCTO_NOT_FOUND + " con el id " + id));

        String nombre = updateProductoDTO.getNombre();
        if (nombre != null) {
            p.setNombre(nombre);
        }
        String marca = updateProductoDTO.getMarca();
        if (marca != null) {
            p.setMarca(marca);
        }
        Double costo = updateProductoDTO.getCosto();
        if (costo != null) {
            p.setCosto(costo);
        }
        Double cantidadDisponible = updateProductoDTO.getCantidadDisponible();
        if (cantidadDisponible != null) {
            p.setCantidadDisponible(cantidadDisponible);
        }
        productoRepository.save(p);
        return mapper.mapProductoToDTO(p);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> getProductosByIds(List<Long> idsProductos) {
        List<Producto> productos = productoRepository.findAllById(idsProductos);
        checkProductos(idsProductos, productos);
        return productos;
    }

    private void checkProductos(List<Long> idsProductos, List<Producto> productos) {
        List<Long> idsProductosEncontrados = productos.stream()
                .mapToLong(Producto::getProductoId)
                .boxed()
                .collect(Collectors.toList());

        List<Long> productosNoEncontrados = idsProductos.stream()
                .filter(id -> !idsProductosEncontrados.contains(id))
                .collect(Collectors.toList());

        if (!productosNoEncontrados.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ErrorMsgs.PRODUCTOS_NOT_FOUND, productosNoEncontrados));
        }
    }


    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> getProductosConStockBajo() {
        return mapper.mapProductosToDTO(productoRepository.findByCantidadDisponibleLessThan(AppVariables.STOCK_BAJO));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean productoExistente(String nombre, String marca) {
        Optional<Producto> producto = productoRepository.findByNombreAndMarca(nombre, marca);
        return producto.isPresent();
    }

    @Override
    public void checkStock(Producto producto) {
        if (producto.getCantidadDisponible() == 0) {
            throw new NoStockException(String.format(ErrorMsgs.PRODUCTO_SIN_STOCK, producto.getProductoId(), producto.getNombre(), producto.getMarca()));
        }
    }
}
