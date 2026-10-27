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
import auca.pharma.rw.repository.DeliveryNoteRepository;
import auca.pharma.rw.repository.SalesOrderRepository;

@Service
public class SalesOrderService {

    @Autowired
    private SalesOrderRepository salesOrderRepo;

    @Autowired
    private DeliveryNoteRepository deliveryNoteRepo;

    public String saveSalesOrder(SalesOrder salesOrder) {

        if (salesOrder.getOrderDate() != null && salesOrder.getOrderDate().isBefore(LocalDate.now())) {
            return "Sales order date cannot be in the past";
        }

        if (salesOrder.getSalesRepresentative() != null
                && salesOrder.getSalesRepresentative().getId() != null
                && salesOrder.getOrderDate() != null
                && salesOrderRepo.existsBySalesRepresentativeIdAndOrderDateAndStatusNot(
                        salesOrder.getSalesRepresentative().getId(),
                        salesOrder.getOrderDate(),
                        SalesOrderStatus.CANCELLED)) {
            return "Sales representative already has an order on that date";
        }

        salesOrder.setStatus(SalesOrderStatus.PLACED);
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

        if (deliveryNoteRepo.existsBySalesOrder(existingSalesOrder.get())) {
            return "That sales order has delivery notes, delete them first";
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

    public String changeStatus(UUID id, SalesOrderStatus newStatus) {

        Optional<SalesOrder> existingSalesOrder = salesOrderRepo.findById(id);

        if (existingSalesOrder.isEmpty()) {
            return "The sales order with that id does not exist";
        }

        SalesOrder salesOrder = existingSalesOrder.get();
        SalesOrderStatus currentStatus = salesOrder.getStatus();

        boolean allowed = false;

        if (currentStatus == SalesOrderStatus.PLACED) {
            allowed = newStatus == SalesOrderStatus.APPROVED
                    || newStatus == SalesOrderStatus.CANCELLED;
        } else if (currentStatus == SalesOrderStatus.APPROVED) {
            allowed = newStatus == SalesOrderStatus.DELIVERED
                    || newStatus == SalesOrderStatus.CANCELLED;
        }

        if (!allowed) {
            return "Cannot change status from " + currentStatus + " to " + newStatus;
        }

        salesOrder.setStatus(newStatus);
        salesOrderRepo.save(salesOrder);
        return "Sales order status updated successfully";
    }

    public List<Object[]> getSalesOrderCountPerStatus() {
        return salesOrderRepo.countSalesOrdersByStatus();
    }

    @Transactional
    public String cancelSalesRepresentativeDay(UUID salesRepresentativeId, LocalDate date) {
        int updated = salesOrderRepo.cancelSalesRepresentativeDay(salesRepresentativeId, date);
        return updated + " sales orders cancelled";
    }

    public Page<SalesOrder> getSalesOrdersPage(Pageable pageable) {
        return salesOrderRepo.findAll(pageable);
    }

}
