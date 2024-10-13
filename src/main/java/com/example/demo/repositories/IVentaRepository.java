package com.example.demo.repositories;

import com.example.demo.models.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IVentaRepository extends JpaRepository<Venta, Long> {
//    @Query("SELECT v FROM Venta v JOIN FETCH v.productos WHERE v.ventaId = :id")
//    Optional<Venta> findByIdWithProductos(@Param("id") Long id);
}
