package auca.pharma.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import auca.pharma.rw.domain.ProductLine;
import auca.pharma.rw.service.ProductLineService;

@RestController
@RequestMapping(value = "/api/product-lines")
public class ProductLineController {

    @Autowired
    private ProductLineService productLineService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveProductLine(@RequestBody ProductLine productLine) {

        String returnedMessage = productLineService.saveProductLine(productLine);
        return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllProductLines() {

        List<ProductLine> productLines = productLineService.getAllProductLines();
        return new ResponseEntity<>(productLines, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProductLineById(@PathVariable UUID id) {

        Optional<ProductLine> productLine = productLineService.getProductLineById(id);

        if (productLine.isEmpty()) {
            return new ResponseEntity<>("The product line with that id does not exist", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(productLine.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProductLine(@PathVariable UUID id, @RequestBody ProductLine productLine) {

        String returnedMessage = productLineService.updateProductLine(id, productLine);

        if (returnedMessage.equals("The product line with that id does not exist")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteProductLine(@PathVariable UUID id) {

        String returnedMessage = productLineService.deleteProductLine(id);

        if (returnedMessage.equals("The product line with that id does not exist")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }
    }

    // C3
    @GetMapping(value = "/unused", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUnusedProductLines() {

        List<ProductLine> productLines = productLineService.getUnusedProductLines();
        return new ResponseEntity<>(productLines, HttpStatus.OK);
    }

}
