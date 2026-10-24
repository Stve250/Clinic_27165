package auca.pharma.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import auca.pharma.rw.domain.SalesRepresentative;
import auca.pharma.rw.domain.Territory;

@Repository
public interface SalesRepresentativeRepository extends JpaRepository<SalesRepresentative, UUID> {
    @Query("SELECT DISTINCT s FROM SalesRepresentative s JOIN s.productLines p " +
            "WHERE LOWER(p.name) = LOWER(:name)")
    List<SalesRepresentative> findSalesRepresentativesByProductLineName(@Param("name") String name);

    boolean existsByTerritoryAndIdNot(Territory territory, UUID salesRepresentativeId);

}
