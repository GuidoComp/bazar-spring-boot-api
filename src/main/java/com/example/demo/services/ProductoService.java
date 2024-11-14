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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class ProductoService implements IProductoService {

    private final IProductoRepository productoRepository;
//    private static final GenericModelMapper modelMapper = GenericModelMapper.getModelMapper();

    public ProductoService(IProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<ProductoResponseDTO> getProductos() {
        return List.of();
    }

    @Override
    public ProductoResponseDTO addProducto(AddProductoDTO addProductoDTO) {
        return null;
    }

    @Override
    public ProductoResponseDTO deleteProducto(Long id) {
        return null;
    }

    @Override
    public ProductoResponseDTO updateProducto(Long id, UpdateProductoDTO updateProductoDTO) {
        return null;
    }

    @Override
    public List<Producto> getProductosByIds(List<Long> idsProductos) {
        return List.of();
    }

    @Override
    public List<ProductoResponseDTO> getProductosConStockBajo() {
        return List.of();
    }

    @Override
    public boolean productoExistente(String nombre, String marca) {
        return false;
    }

    @Override
    public void checkStock(Producto p) {

    }

//    @Override
//    public List<ProductoResponseDTO> getProductos() {
//        List<Producto> productos = this.productoRepository.findAll();
//        List<ProductoResponseDTO> productosDTO = new LinkedList<>();
//
//        for (Producto producto : productos) {
//            productosDTO.add(modelMapper.mapProductoToDTO(producto));
//        }
//        return productosDTO;
//    }
//
//    @Override
//    public ProductoResponseDTO addProducto(AddProductoDTO addProductoDTO) {
//        Producto producto = modelMapper.mapDTOToProducto(addProductoDTO);
//        if (this.productoExistente(addProductoDTO.getNombre(), addProductoDTO.getMarca())) {
//            throw new RestrictException(ErrorMsgs.PRODUCTO_YA_INGRESADO);
//        }
//        Producto p = productoRepository.save(producto);
//        return modelMapper.mapProductoToDTO(p);
//    }
//
//    @Override
//    public ProductoResponseDTO deleteProducto(Long id) {
//        Producto producto = productoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorMsgs.PRODUCTO_NOT_FOUND + " con el id " + id));
//        if (!producto.getVentas().isEmpty()) {
//            throw new RestrictException(ErrorMsgs.DELETE_PRODUCTO_RESTRICCION_FK);
//        }
//        productoRepository.delete(producto);
//        return modelMapper.mapProductoToDTO(producto);
//    }
//
//    @Override
//    public ProductoResponseDTO updateProducto(Long id, UpdateProductoDTO updateProductoDTO) {
//        Producto p = this.productoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorMsgs.PRODUCTO_NOT_FOUND + " con el id " + id));
//
//        String nombre = updateProductoDTO.getNombre();
//        if (nombre != null) {
//            p.setNombre(nombre);
//        }
//        String marca = updateProductoDTO.getMarca();
//        if (marca != null) {
//            p.setMarca(marca);
//        }
//        Double costo = updateProductoDTO.getCosto();
//        if (costo != null) {
//            p.setCosto(costo);
//        }
//        Double cantidadDisponible = updateProductoDTO.getCantidadDisponible();
//        if (cantidadDisponible != null) {
//            p.setCantidadDisponible(cantidadDisponible);
//        }
//        productoRepository.save(p);
//        return modelMapper.mapProductoToDTO(p);
//    }
//
//    @Override
//    public List<Producto> getProductosByIds(List<Long> idsProductos) {
//        return productoRepository.findAllById(idsProductos);
//    }
//
//    @Override
//    public List<ProductoResponseDTO> getProductosConStockBajo() {
//        List<Producto> productos = productoRepository.findAll();
//        List<ProductoResponseDTO> productosConStockBajo = new LinkedList<>();
//
//        for (Producto producto: productos) {
//            if (producto.getCantidadDisponible() < AppVariables.STOCK_BAJO) {
//                productosConStockBajo.add(modelMapper.mapProductoToDTO(producto));
//            }
//        }
//        return productosConStockBajo;
//    }
//
//    @Override
//    public boolean productoExistente(String nombre, String marca) {
//        List<Producto> productos = this.productoRepository.findAll();
//        boolean exists = false;
//        int index = 0;
//        while (!exists && index < productos.size()) {
//            if (productos.get(index).getNombre().equals(nombre) && productos.get(index).getMarca().equals(marca)) {
//                exists = true;
//            }
//            index++;
//        }
//        return exists;
//    }
//
//    @Override
//    public void checkStock(Producto producto) {
//        if (producto.getCantidadDisponible() == 0) {
//            throw new NoStockException(String.format(ErrorMsgs.PRODUCTO_SIN_STOCK, producto.getProductoId(), producto.getNombre(), producto.getMarca()));
//        }
//    }
}
