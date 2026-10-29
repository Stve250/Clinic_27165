package auca.pharma.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import auca.pharma.rw.domain.ProductLine;

@Repository
public interface ProductLineRepository extends JpaRepository<ProductLine, UUID> {

    @Query("SELECT p FROM ProductLine p WHERE p.salesRepresentatives IS EMPTY")
    List<ProductLine> findUnusedProductLines();

}
