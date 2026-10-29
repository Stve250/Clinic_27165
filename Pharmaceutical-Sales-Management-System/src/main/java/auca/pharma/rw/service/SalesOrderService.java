package auca.pharma.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import auca.pharma.rw.domain.SalesOrder;
import auca.pharma.rw.domain.SalesOrderStatus;
import auca.pharma.rw.repository.SalesOrderRepository;

@Service
public class SalesOrderService {

    @Autowired
    private SalesOrderRepository salesOrderRepo;

    public String saveSalesOrder(SalesOrder salesOrder) {
        salesOrderRepo.save(salesOrder);
        return "Sales order saved successfully";
    }

    public List<SalesOrder> getAllSalesOrders() {
        return salesOrderRepo.findAll();
    }

    public Optional<SalesOrder> getSalesOrderById(UUID id) {
        return salesOrderRepo.findById(id);
    }

    public String updateSalesOrder(UUID id, SalesOrder salesOrder) {

        Optional<SalesOrder> existingSalesOrder = salesOrderRepo.findById(id);

        if (existingSalesOrder.isEmpty()) {
            return "The sales order with that id does not exist";
        }

        SalesOrder salesOrderToUpdate = existingSalesOrder.get();
        salesOrderToUpdate.setOrderDate(salesOrder.getOrderDate());
        salesOrderToUpdate.setTotalAmount(salesOrder.getTotalAmount());
        salesOrderToUpdate.setPharmacy(salesOrder.getPharmacy());
        salesOrderToUpdate.setSalesRepresentative(salesOrder.getSalesRepresentative());

        salesOrderRepo.save(salesOrderToUpdate);
        return "Sales order updated successfully";
    }

    public String deleteSalesOrder(UUID id) {

        Optional<SalesOrder> existingSalesOrder = salesOrderRepo.findById(id);

        if (existingSalesOrder.isEmpty()) {
            return "The sales order with that id does not exist";
        }

        salesOrderRepo.deleteById(id);
        return "Sales order deleted successfully";
    }

    public List<SalesOrder> getSalesOrdersByStatus(SalesOrderStatus status) {
        return salesOrderRepo.findByStatusOrderByOrderDateAsc(status);
    }

    public List<SalesOrder> getSalesOrdersBetween(LocalDate start, LocalDate end) {
        return salesOrderRepo.findByOrderDateBetweenOrderByOrderDateAsc(start, end);
    }

}
