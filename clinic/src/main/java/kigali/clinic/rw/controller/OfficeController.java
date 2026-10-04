package kigali.clinic.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.service.AppointmentService;
import kigali.clinic.rw.service.OfficeService;
import org.springframework.web.bind.annotation.*;

@RestController 
@RequestMapping (value = {"/api/office", "/api/offices"})
public class OfficeController {
  
    @Autowired 
    private OfficeService officeService;

    @Autowired
    private AppointmentService appointmentService;

    
     @PostMapping(value="/save", consumes = MediaType.APPLICATION_JSON_VALUE, 
        produces = MediaType.APPLICATION_JSON_VALUE
     )
    public ResponseEntity<?> saveOffice(@RequestBody Office office){
        
      String returnedMessage =  officeService.saveOffice(office);
        return new ResponseEntity<>(returnedMessage,HttpStatus.CREATED );
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllOffices(){

      List<Office> offices = officeService.getAllOffices();
      return new ResponseEntity<>(offices, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getOfficeById(@PathVariable UUID id){

      Optional<Office> office = officeService.getOfficeById(id);

      if(office.isEmpty()){
        return new ResponseEntity<>("we don't have office with that id", HttpStatus.NOT_FOUND);
      }

      return new ResponseEntity<>(office.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateOffice(@PathVariable UUID id, @RequestBody Office office){

      String returnedMessage = officeService.updateOffice(id, office);
      return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{officeNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteOffice(@PathVariable int officeNumber){

      String returnedMessage = officeService.deleteOneOffice(officeNumber);
      return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }


    @GetMapping(value = "/busiest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBusiestOffice(){

      List<Object[]> busiest = appointmentService.getBusiestOffice();

      if(busiest.isEmpty()){
        return new ResponseEntity<>("No appointments yet", HttpStatus.OK);
      }

      return new ResponseEntity<>(busiest.get(0), HttpStatus.OK);
    }
}
