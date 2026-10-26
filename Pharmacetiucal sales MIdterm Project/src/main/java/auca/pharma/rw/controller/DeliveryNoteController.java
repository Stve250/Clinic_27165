package auca.pharma.rw.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import auca.pharma.rw.domain.DeliveryNote;
import auca.pharma.rw.service.DeliveryNoteService;

@RestController
@RequestMapping(value = "/api/delivery-notes")
public class DeliveryNoteController {

    @Autowired
    private DeliveryNoteService deliveryNoteService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveDeliveryNote(
            @RequestParam UUID salesOrderId,
            @RequestBody DeliveryNote deliveryNote) {

        String returnedMessage = deliveryNoteService.saveDeliveryNote(salesOrderId, deliveryNote);

        if (returnedMessage.equals("The sales order with that id does not exist")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        } else if (returnedMessage.equals("Delivery notes are only for DELIVERED sales orders")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
        }
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllDeliveryNotes() {

        List<DeliveryNote> deliveryNotes = deliveryNoteService.getAllDeliveryNotes();
        return new ResponseEntity<>(deliveryNotes, HttpStatus.OK);
    }


    @GetMapping(value = "/pharmacy/{licenceNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDeliveryNotesOfPharmacy(@PathVariable String licenceNumber) {

        List<DeliveryNote> deliveryNotes = deliveryNoteService.getDeliveryNotesOfPharmacy(licenceNumber);
        return new ResponseEntity<>(deliveryNotes, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteDeliveryNote(@PathVariable UUID id) {

        String returnedMessage = deliveryNoteService.deleteDeliveryNote(id);

        if (returnedMessage.equals("The delivery note with that id does not exist")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }
    }

}
