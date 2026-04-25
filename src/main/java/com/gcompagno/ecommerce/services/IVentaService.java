package com.gcompagno.ecommerce.services;

import com.gcompagno.ecommerce.dtos.requestDTOs.ventaDTOs.AddVentaDTO;
import com.gcompagno.ecommerce.dtos.requestDTOs.ventaDTOs.UpdateVentaDTO;
import com.gcompagno.ecommerce.dtos.responseDTOs.productoDTOs.ProductoResponseDTO;
import com.gcompagno.ecommerce.dtos.responseDTOs.ventaDTOs.InfoMayorVenta;
import com.gcompagno.ecommerce.dtos.responseDTOs.ventaDTOs.MontoYCantidadTotalDTO;
import com.gcompagno.ecommerce.dtos.responseDTOs.ventaDTOs.VentaResponseDTO;
import com.gcompagno.ecommerce.models.Producto;
import com.gcompagno.ecommerce.models.Venta;

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

    void borrarProductos(Venta venta);
}
