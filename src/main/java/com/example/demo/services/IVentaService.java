package com.example.demo.services;

import com.example.demo.dtos.requestDTOs.ventaDTOs.AddVentaDTO;
import com.example.demo.dtos.requestDTOs.ventaDTOs.UpdateVentaDTO;
import com.example.demo.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
import com.example.demo.dtos.responseDTOs.ventaDTOs.InfoMayorVenta;
import com.example.demo.dtos.responseDTOs.ventaDTOs.MontoYCantidadTotalDTO;
import com.example.demo.dtos.responseDTOs.ventaDTOs.VentaResponseDTO;
import com.example.demo.models.Producto;
import com.example.demo.models.Venta;

import java.time.LocalDate;
import java.util.List;

public interface IVentaService {
    List<VentaResponseDTO> getVentas();

    VentaResponseDTO addVenta(AddVentaDTO addVentaDTO);

    VentaResponseDTO deleteVenta(Long id);

    VentaResponseDTO updateVenta(Long id, UpdateVentaDTO updateVentaDTO);

    List<ProductoResponseDTO> getProductosDTODeVenta(Long idVenta);

    Venta getVentaById(Long idVenta);

    MontoYCantidadTotalDTO getMontoYCantidadTotales(LocalDate fechaVenta);

    List<Venta> getVentasByDate(LocalDate fecha);

    InfoMayorVenta getInfoMayorVenta();
}
