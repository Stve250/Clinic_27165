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
}
