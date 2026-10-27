package auca.pharma.rw.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.pharma.rw.domain.SalesOrderStatus;
import auca.pharma.rw.repository.DeliveryNoteRepository;
import auca.pharma.rw.repository.PharmacyRepository;
import auca.pharma.rw.repository.SalesOrderRepository;
import auca.pharma.rw.repository.SalesRepresentativeRepository;

@Service
public class DashboardService {

    @Autowired
    private PharmacyRepository pharmacyRepo;

    @Autowired
    private SalesRepresentativeRepository salesRepresentativeRepo;

    @Autowired
    private TerritoryService territoryService;

    @Autowired
    private SalesOrderRepository salesOrderRepo;

    @Autowired
    private DeliveryNoteRepository deliveryNoteRepo;

    public Map<String, Long> getSummary() {

        Map<String, Long> summary = new LinkedHashMap<>();

        summary.put("pharmacies", pharmacyRepo.count());
        summary.put("salesRepresentatives", salesRepresentativeRepo.count());
        summary.put("freeTerritories", (long) territoryService.getFreeTerritories().size());
        summary.put("ordersToday", salesOrderRepo.countByOrderDate(LocalDate.now()));
        summary.put("placed", salesOrderRepo.countByStatus(SalesOrderStatus.PLACED));
        summary.put("deliveryNotes", deliveryNoteRepo.count());

        return summary;
    }

}
