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

    // A2 + D1
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveSalesOrder(@RequestBody SalesOrder salesOrder) {

        String returnedMessage = salesOrderService.saveSalesOrder(salesOrder);

        if (returnedMessage.equals("Sales order date cannot be in the past")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
        } else if (returnedMessage.equals("Sales representative already has an order on that date")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
        }
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

    // A2 + E3
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteSalesOrder(@PathVariable UUID id) {

        String returnedMessage = salesOrderService.deleteSalesOrder(id);

        if (returnedMessage.equals("The sales order with that id does not exist")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        } else if (returnedMessage.equals("That sales order has delivery notes, delete them first")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }
    }

    // B2
    @GetMapping(value = "/by-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSalesOrdersByStatus(@RequestParam SalesOrderStatus status) {

        List<SalesOrder> salesOrders = salesOrderService.getSalesOrdersByStatus(status);
        return new ResponseEntity<>(salesOrders, HttpStatus.OK);
    }

    // B3
    @GetMapping(value = "/between", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSalesOrdersBetween(
            @RequestParam String start,
            @RequestParam String end) {

        List<SalesOrder> salesOrders = salesOrderService.getSalesOrdersBetween(
                LocalDate.parse(start), LocalDate.parse(end));
        return new ResponseEntity<>(salesOrders, HttpStatus.OK);
    }

    // D2
    @PatchMapping(value = "/{id}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeStatus(@PathVariable UUID id, @RequestParam SalesOrderStatus status) {

        String returnedMessage = salesOrderService.changeStatus(id, status);

        if (returnedMessage.equals("The sales order with that id does not exist")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        } else if (returnedMessage.startsWith("Cannot change status from")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }
    }

    // F1
    @GetMapping(value = "/stats/by-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSalesOrderCountPerStatus() {

        List<Object[]> counts = salesOrderService.getSalesOrderCountPerStatus();
        return new ResponseEntity<>(counts, HttpStatus.OK);
    }

    // F2
    @PatchMapping(value = "/cancel-day", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cancelSalesRepresentativeDay(
            @RequestParam UUID salesRepresentativeId,
            @RequestParam String date) {

        String returnedMessage = salesOrderService.cancelSalesRepresentativeDay(
                salesRepresentativeId, LocalDate.parse(date));
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

    // Bonus: sales orders page by page
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSalesOrdersPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "orderDate,desc") String sort) {

        String[] sortParts = sort.split(",");
        Sort.Direction direction = (sortParts.length > 1 && sortParts[1].equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParts[0]));
        Page<SalesOrder> salesOrders = salesOrderService.getSalesOrdersPage(pageable);
        return new ResponseEntity<>(salesOrders, HttpStatus.OK);
    }

}
