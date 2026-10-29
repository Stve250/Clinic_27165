package auca.pharma.rw.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import auca.pharma.rw.domain.Territory;

@Repository
public interface TerritoryRepository extends JpaRepository<Territory, UUID> {

    @Query("SELECT t FROM Territory t " +
            "WHERE NOT EXISTS (SELECT s FROM SalesRepresentative s WHERE s.territory = t) " +
            "ORDER BY t.territoryNumber")
    List<Territory> findFreeTerritories();

}
