package auca.pharma.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import auca.pharma.rw.domain.DeliveryNote;
import auca.pharma.rw.domain.SalesOrder;

@Repository
public interface DeliveryNoteRepository extends JpaRepository<DeliveryNote, UUID> {

    boolean existsBySalesOrder(SalesOrder salesOrder);

    @Query("SELECT d FROM DeliveryNote d " +
            "WHERE d.salesOrder.pharmacy.licenceNumber = :licenceNumber " +
            "ORDER BY d.deliveredDate DESC")
    List<DeliveryNote> findDeliveryNotesByLicenceNumber(@Param("licenceNumber") String licenceNumber);

}
