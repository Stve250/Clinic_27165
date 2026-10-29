package auca.pharma.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.pharma.rw.domain.Territory;
import auca.pharma.rw.repository.TerritoryRepository;

@Service
public class TerritoryService {

    @Autowired
    private TerritoryRepository territoryRepo;

    public String saveTerritory(Territory territory) {
        territoryRepo.save(territory);
        return "Territory saved successfully";
    }

    public List<Territory> getAllTerritories() {
        return territoryRepo.findAll();
    }

    public Optional<Territory> getTerritoryById(UUID id) {
        return territoryRepo.findById(id);
    }

    public String updateTerritory(UUID id, Territory territory) {

        Optional<Territory> existingTerritory = territoryRepo.findById(id);

        if (existingTerritory.isEmpty()) {
            return "The territory with that id does not exist";
        }

        Territory territoryToUpdate = existingTerritory.get();
        territoryToUpdate.setTerritoryNumber(territory.getTerritoryNumber());
        territoryToUpdate.setName(territory.getName());

        territoryRepo.save(territoryToUpdate);
        return "Territory updated successfully";
    }

    public String deleteTerritory(UUID id) {

        Optional<Territory> existingTerritory = territoryRepo.findById(id);

        if (existingTerritory.isEmpty()) {
            return "The territory with that id does not exist";
        }

        territoryRepo.deleteById(id);
        return "Territory deleted successfully";
    }

    public List<Territory> getFreeTerritories() {
        return territoryRepo.findFreeTerritories();
    }

}
