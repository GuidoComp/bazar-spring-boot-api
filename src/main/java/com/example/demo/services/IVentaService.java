package com.example.demo.services;

import com.example.demo.dtos.requestDTOs.ventaDTOs.AddVentaDTO;
import com.example.demo.dtos.requestDTOs.ventaDTOs.UpdateVentaDTO;
import com.example.demo.dtos.responseDTOs.ventaDTOs.VentaResponseDTO;

import java.util.List;

public interface IVentaService {
    List<VentaResponseDTO> getVentas();

    VentaResponseDTO addVenta(AddVentaDTO addVentaDTO);

    VentaResponseDTO deleteVenta(Long id);

    VentaResponseDTO updateVenta(Long id, UpdateVentaDTO updateVentaDTO);
}
