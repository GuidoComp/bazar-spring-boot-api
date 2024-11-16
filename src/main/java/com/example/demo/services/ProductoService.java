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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
        return productoRepository.findAllById(idsProductos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> getProductosConStockBajo() {
        return mapper.mapProductosToDTO(productoRepository.findByCantidadDisponibleLessThan(AppVariables.STOCK_BAJO));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean productoExistente(String nombre, String marca) {
        List<Producto> productos = this.productoRepository.findAll();
        boolean exists = false;
        int index = 0;
        while (!exists && index < productos.size()) {
            if (productos.get(index).getNombre().equals(nombre) && productos.get(index).getMarca().equals(marca)) {
                exists = true;
            }
            index++;
        }
        return exists;
    }

    @Override
    public void checkStock(Producto producto) {
        if (producto.getCantidadDisponible() == 0) {
            throw new NoStockException(String.format(ErrorMsgs.PRODUCTO_SIN_STOCK, producto.getProductoId(), producto.getNombre(), producto.getMarca()));
        }
    }
}
