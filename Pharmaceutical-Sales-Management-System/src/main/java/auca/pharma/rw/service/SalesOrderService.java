package auca.pharma.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // D2: the general update no longer touches the status
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

}
