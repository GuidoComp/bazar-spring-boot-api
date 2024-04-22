package com.example.demo.services;

import com.example.demo.dtos.requestDTOs.productoDTOs.AddProductoDTO;
import com.example.demo.dtos.requestDTOs.productoDTOs.UpdateProductoDTO;
import com.example.demo.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
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
    private static final GenericModelMapper modelMapper = GenericModelMapper.getModelMapper();

    public ProductoService(IProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<ProductoResponseDTO> getProductos() {
        List<Producto> productos = this.productoRepository.findAll();
        List<ProductoResponseDTO> productosDTO = new LinkedList<>();

        for (Producto producto : productos) {
            productosDTO.add(modelMapper.mapProductoToDTO(producto));
        }
        return productosDTO;
    }

    @Override
    public ProductoResponseDTO addProducto(AddProductoDTO addProductoDTO) {
        Producto p = this.productoRepository.save(modelMapper.mapDTOToProducto(addProductoDTO));
        return modelMapper.mapProductoToDTO(p);
    }

    @Override
    public ProductoResponseDTO deleteProducto(Long id) {
        Producto producto = this.productoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorMsgs.PRODUCTO_NOT_FOUND + " con el id " + id));
        if (!producto.getVentas().isEmpty()) {
            throw new RestrictException(ErrorMsgs.RESTRICCION_FK);
        }
        this.productoRepository.delete(producto);
        return modelMapper.mapProductoToDTO(producto);
    }

    @Override
    public ProductoResponseDTO updateProducto(Long id, UpdateProductoDTO updateProductoDTO) {
        Producto p = this.productoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorMsgs.PRODUCTO_NOT_FOUND + " con el id " + id));

        if (updateProductoDTO.getNombre() != null) {
            p.setNombre(updateProductoDTO.getNombre());
        }
        if (updateProductoDTO.getMarca() != null) {
            p.setMarca(updateProductoDTO.getMarca());
        }
        if (updateProductoDTO.getCosto() != null) {
            p.setCosto(updateProductoDTO.getCosto());
        }
        if (updateProductoDTO.getCantidadDisponible() != null) {
            p.setCantidadDisponible(updateProductoDTO.getCantidadDisponible());
        }
        this.productoRepository.save(p);
        return modelMapper.mapProductoToDTO(p);
    }

    @Override
    public List<Producto> getProductosByIds(List<Long> idsProductos) throws ResourceNotFoundException {
        List<Producto> productos = new ArrayList<>();
        for(Long id : idsProductos){
            productos.add(this.productoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorMsgs.PRODUCTO_NOT_FOUND + " con el id " + id)));
        }
        return productos;
    }

    @Override
    public List<ProductoResponseDTO> getProductosConStockBajo() {
        List<Producto> productos = this.productoRepository.findAll();
        List<ProductoResponseDTO> productosConStockBajo = new LinkedList<>();

        for (Producto producto: productos) {
            if (producto.getCantidadDisponible() < AppVariables.STOCK_BAJO) {
                productosConStockBajo.add(this.modelMapper.mapProductoToDTO(producto));
            }
        }
        return productosConStockBajo;
    }
}
