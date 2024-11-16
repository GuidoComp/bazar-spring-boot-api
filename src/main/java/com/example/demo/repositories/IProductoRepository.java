package com.example.demo.repositories;

import com.example.demo.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Long>{
    List<Producto> findByCantidadDisponibleLessThan(int cantidad);
    Optional<Producto> findByNombreAndMarca(String nombre, String marca);
}
