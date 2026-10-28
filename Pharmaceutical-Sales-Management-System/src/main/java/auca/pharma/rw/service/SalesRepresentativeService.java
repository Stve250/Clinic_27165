package auca.pharma.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import auca.pharma.rw.domain.ProductLine;
import auca.pharma.rw.domain.SalesRepresentative;
import auca.pharma.rw.domain.Territory;
import auca.pharma.rw.repository.ProductLineRepository;
import auca.pharma.rw.repository.SalesRepresentativeRepository;
import auca.pharma.rw.repository.TerritoryRepository;

@Service
public class SalesRepresentativeService {

    @Autowired
    private SalesRepresentativeRepository salesRepresentativeRepo;

    @Autowired
    private ProductLineRepository productLineRepo;

    @Autowired
    private TerritoryRepository territoryRepo;

    public String saveSalesRepresentative(SalesRepresentative salesRepresentative) {
        salesRepresentativeRepo.save(salesRepresentative);
        return "Sales representative saved successfully";
    }

    public List<SalesRepresentative> getAllSalesRepresentatives() {
        return salesRepresentativeRepo.findAll();
    }

    public Optional<SalesRepresentative> getSalesRepresentativeById(UUID id) {
        return salesRepresentativeRepo.findById(id);
    }

    public String updateSalesRepresentative(UUID id, SalesRepresentative salesRepresentative) {

        Optional<SalesRepresentative> existingSalesRepresentative = salesRepresentativeRepo.findById(id);

        if (existingSalesRepresentative.isEmpty()) {
            return "The sales representative with that id does not exist";
        }

        SalesRepresentative salesRepresentativeToUpdate = existingSalesRepresentative.get();
        salesRepresentativeToUpdate.setFirstName(salesRepresentative.getFirstName());
        salesRepresentativeToUpdate.setLastName(salesRepresentative.getLastName());
        salesRepresentativeToUpdate.setPhone(salesRepresentative.getPhone());
        salesRepresentativeToUpdate.setTerritory(salesRepresentative.getTerritory());

        salesRepresentativeRepo.save(salesRepresentativeToUpdate);
        return "Sales representative updated successfully";
    }

    public String deleteSalesRepresentative(UUID id) {

        Optional<SalesRepresentative> existingSalesRepresentative = salesRepresentativeRepo.findById(id);

        if (existingSalesRepresentative.isEmpty()) {
            return "The sales representative with that id does not exist";
        }

        salesRepresentativeRepo.deleteById(id);
        return "Sales representative deleted successfully";
    }

}
