package auca.pharma.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.pharma.rw.domain.DeliveryNote;
import auca.pharma.rw.domain.SalesOrder;
import auca.pharma.rw.domain.SalesOrderStatus;
import auca.pharma.rw.repository.DeliveryNoteRepository;
import auca.pharma.rw.repository.SalesOrderRepository;

@Service
public class DeliveryNoteService {

    @Autowired
    private DeliveryNoteRepository deliveryNoteRepo;

    @Autowired
    private SalesOrderRepository salesOrderRepo;

    // E1
    public String saveDeliveryNote(UUID salesOrderId, DeliveryNote deliveryNote) {

        Optional<SalesOrder> existingSalesOrder = salesOrderRepo.findById(salesOrderId);

        if (existingSalesOrder.isEmpty()) {
            return "The sales order with that id does not exist";
        }

        SalesOrder salesOrder = existingSalesOrder.get();

        if (salesOrder.getStatus() != SalesOrderStatus.DELIVERED) {
            return "Delivery notes are only for DELIVERED sales orders";
        }

        deliveryNote.setDeliveredDate(LocalDate.now());
        deliveryNote.setSalesOrder(salesOrder);

        deliveryNoteRepo.save(deliveryNote);
        return "Delivery note saved successfully";
    }

    public List<DeliveryNote> getAllDeliveryNotes() {
        return deliveryNoteRepo.findAll();
    }

    // E2
    public List<DeliveryNote> getDeliveryNotesOfPharmacy(String licenceNumber) {
        return deliveryNoteRepo.findDeliveryNotesByLicenceNumber(licenceNumber);
    }

    // E3
    public String deleteDeliveryNote(UUID id) {

        Optional<DeliveryNote> existingDeliveryNote = deliveryNoteRepo.findById(id);

        if (existingDeliveryNote.isEmpty()) {
            return "The delivery note with that id does not exist";
        }

        deliveryNoteRepo.deleteById(id);
        return "Delivery note deleted successfully";
    }

}
