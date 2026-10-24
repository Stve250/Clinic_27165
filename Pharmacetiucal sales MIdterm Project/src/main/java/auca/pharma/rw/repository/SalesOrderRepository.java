package auca.pharma.rw.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import auca.pharma.rw.domain.SalesOrder;
import auca.pharma.rw.domain.SalesOrderStatus;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, UUID> {
    List<SalesOrder> findByStatusOrderByOrderDateAsc(SalesOrderStatus status);

    List<SalesOrder> findByOrderDateBetweenOrderByOrderDateAsc(LocalDate start, LocalDate end);

    boolean existsBySalesRepresentativeIdAndOrderDateAndStatusNot(UUID salesRepresentativeId,
            LocalDate orderDate, SalesOrderStatus status);

    @Query("SELECT s.status, COUNT(s) FROM SalesOrder s GROUP BY s.status")
    List<Object[]> countSalesOrdersByStatus();
    @Modifying(clearAutomatically = true)
    @Query("UPDATE SalesOrder s " +
            "SET s.status = auca.pharma.rw.domain.SalesOrderStatus.CANCELLED " +
            "WHERE s.salesRepresentative.id = :salesRepresentativeId AND s.orderDate = :date " +
            "AND s.status <> auca.pharma.rw.domain.SalesOrderStatus.DELIVERED")
    int cancelSalesRepresentativeDay(@Param("salesRepresentativeId") UUID salesRepresentativeId,
            @Param("date") LocalDate date);

    long countByStatus(SalesOrderStatus status);

    long countByOrderDate(LocalDate date);

}
