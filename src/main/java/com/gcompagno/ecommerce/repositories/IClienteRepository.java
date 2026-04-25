package com.gcompagno.ecommerce.repositories;

import com.gcompagno.ecommerce.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IClienteRepository extends JpaRepository<Cliente, Long>{
    Optional<Cliente> findClienteByDni(String dni);
}
