package com.robostore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MyManufacturersRepository extends JpaRepository<myManufacturers, Long> {

    List<myManufacturers> findByStatus(String status);

    Optional<myManufacturers> findByTaxId(String taxId);

    boolean existsByTaxId(String taxId);
}
