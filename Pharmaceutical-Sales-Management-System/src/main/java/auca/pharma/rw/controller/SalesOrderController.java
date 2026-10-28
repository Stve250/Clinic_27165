package auca.pharma.rw.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import auca.pharma.rw.domain.SalesOrder;
import auca.pharma.rw.domain.SalesOrderStatus;
import auca.pharma.rw.service.SalesOrderService;

@RestController
@RequestMapping(value = "/api/sales-orders")
public class SalesOrderController {

    @Autowired
    private SalesOrderService salesOrderService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveSalesOrder(@RequestBody SalesOrder salesOrder) {

        String returnedMessage = salesOrderService.saveSalesOrder(salesOrder);
        return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllSalesOrders() {

        List<SalesOrder> salesOrders = salesOrderService.getAllSalesOrders();
        return new ResponseEntity<>(salesOrders, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSalesOrderById(@PathVariable UUID id) {

        Optional<SalesOrder> salesOrder = salesOrderService.getSalesOrderById(id);

        if (salesOrder.isEmpty()) {
            return new ResponseEntity<>("The sales order with that id does not exist", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(salesOrder.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateSalesOrder(@PathVariable UUID id, @RequestBody SalesOrder salesOrder) {

        String returnedMessage = salesOrderService.updateSalesOrder(id, salesOrder);

        if (returnedMessage.equals("The sales order with that id does not exist")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteSalesOrder(@PathVariable UUID id) {

        String returnedMessage = salesOrderService.deleteSalesOrder(id);

        if (returnedMessage.equals("The sales order with that id does not exist")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }
    }

}
