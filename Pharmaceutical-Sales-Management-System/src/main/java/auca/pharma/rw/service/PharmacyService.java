package auca.pharma.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.pharma.rw.domain.Pharmacy;
import auca.pharma.rw.repository.PharmacyRepository;

@Service
public class PharmacyService {

    @Autowired
    private PharmacyRepository pharmacyRepo;

    public String savePharmacy(Pharmacy pharmacy) {
        pharmacyRepo.save(pharmacy);
        return "Pharmacy saved successfully";
    }

    public List<Pharmacy> getAllPharmacies() {
        return pharmacyRepo.findAll();
    }

    public Optional<Pharmacy> getPharmacyById(UUID id) {
        return pharmacyRepo.findById(id);
    }

    public String updatePharmacy(UUID id, Pharmacy pharmacy) {

        Optional<Pharmacy> existingPharmacy = pharmacyRepo.findById(id);

        if (existingPharmacy.isEmpty()) {
            return "The pharmacy with that id does not exist";
        }

        Pharmacy pharmacyToUpdate = existingPharmacy.get();
        pharmacyToUpdate.setPharmacyName(pharmacy.getPharmacyName());
        pharmacyToUpdate.setOwnerName(pharmacy.getOwnerName());
        pharmacyToUpdate.setLicenceNumber(pharmacy.getLicenceNumber());
        pharmacyToUpdate.setDistrict(pharmacy.getDistrict());
        pharmacyToUpdate.setPhone(pharmacy.getPhone());

        pharmacyRepo.save(pharmacyToUpdate);
        return "Pharmacy updated successfully";
    }

    public String deletePharmacy(UUID id) {

        Optional<Pharmacy> existingPharmacy = pharmacyRepo.findById(id);

        if (existingPharmacy.isEmpty()) {
            return "The pharmacy with that id does not exist";
        }

        pharmacyRepo.deleteById(id);
        return "Pharmacy deleted successfully";
    }

    public List<Pharmacy> getPharmaciesByDistrict(String district) {
        return pharmacyRepo.findByDistrictIgnoreCaseOrderByPharmacyNameAsc(district);
    }

}
