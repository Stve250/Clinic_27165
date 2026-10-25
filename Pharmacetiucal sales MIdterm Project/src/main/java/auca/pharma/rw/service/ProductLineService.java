package auca.pharma.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.pharma.rw.domain.ProductLine;
import auca.pharma.rw.repository.ProductLineRepository;

@Service
public class ProductLineService {

    @Autowired
    private ProductLineRepository productLineRepo;

    public String saveProductLine(ProductLine productLine) {
        productLineRepo.save(productLine);
        return "Product line saved successfully";
    }

    public List<ProductLine> getAllProductLines() {
        return productLineRepo.findAll();
    }

    public Optional<ProductLine> getProductLineById(UUID id) {
        return productLineRepo.findById(id);
    }

    public String updateProductLine(UUID id, ProductLine productLine) {

        Optional<ProductLine> existingProductLine = productLineRepo.findById(id);

        if (existingProductLine.isEmpty()) {
            return "The product line with that id does not exist";
        }

        ProductLine productLineToUpdate = existingProductLine.get();
        productLineToUpdate.setName(productLine.getName());

        productLineRepo.save(productLineToUpdate);
        return "Product line updated successfully";
    }

    public String deleteProductLine(UUID id) {

        Optional<ProductLine> existingProductLine = productLineRepo.findById(id);

        if (existingProductLine.isEmpty()) {
            return "The product line with that id does not exist";
        }

        productLineRepo.deleteById(id);
        return "Product line deleted successfully";
    }

    public List<ProductLine> getUnusedProductLines() {
        return productLineRepo.findUnusedProductLines();
    }

}
