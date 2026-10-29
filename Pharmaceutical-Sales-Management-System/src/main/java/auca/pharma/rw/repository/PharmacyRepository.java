package auca.pharma.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import auca.pharma.rw.domain.Pharmacy;

@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy, UUID> {

    List<Pharmacy> findByDistrictIgnoreCaseOrderByPharmacyNameAsc(String district);

    @Query("SELECT DISTINCT o.pharmacy FROM SalesOrder o " +
            "WHERE o.salesRepresentative.id = :salesRepresentativeId")
    List<Pharmacy> findPharmaciesBySalesRepresentativeId(@Param("salesRepresentativeId") UUID salesRepresentativeId);

}
