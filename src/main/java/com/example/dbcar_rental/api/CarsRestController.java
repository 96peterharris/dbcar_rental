package com.example.dbcar_rental.api;

import com.example.dbcar_rental.custom_response.CustomResponse;
import com.example.dbcar_rental.entities.Car;
import com.example.dbcar_rental.repositories.CarsRepository;
import com.example.dbcar_rental.repositories.ClientsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/car-rental", produces = { MediaType.APPLICATION_JSON_VALUE })
public class CarsRestController {

    private CarsRepository carsRepository;
    private ClientsRepository clientsRepository;
    private final CustomResponse customResponse = new CustomResponse();

    @Autowired
    public void setCarsRepository(CarsRepository carsRepository){
        this.carsRepository = carsRepository;
    }

    @Autowired
    public void setClientsRepository(ClientsRepository clientsRepository){
        this.clientsRepository = clientsRepository;
    }

    @GetMapping(value = "/cars")
    public List<Car> getAllCars(){
        return carsRepository.findAll();
    }

    @GetMapping(value = "/cars/{car_id}")
    ResponseEntity getCarById(@PathVariable Long car_id) throws JsonProcessingException {
        if(carsRepository.existsById(car_id)){
            return customResponse.getResponseOK_singleObject(carsRepository.findById(car_id).get());
        }else{
            return customResponse.getResponseBAD_REQUEST(car_id,"car", " get fail!!!","/car-rental/cars/" + car_id);
        }

    }

    @PostMapping("/cars")
    @ResponseBody
    ResponseEntity addCar(@RequestBody Car newCar){
        if(clientsRepository.existsById(newCar.getClientId()) || newCar.getClientId() == 0){
            carsRepository.save(newCar);
            return customResponse.getResponseCREATED(newCar.getCarId(), "car", "/car-rental/cars/" + newCar.getCarId());
        }else{
            return  customResponse.getResponseBAD_REQUEST(newCar.getClientId(), "client", " client not exist","/car-rental/cars/" + newCar.getCarId());
        }
    }

    @PutMapping("/cars/edit/{car_id}")
    @ResponseBody
    ResponseEntity editCar(@RequestBody Car editedCar, @PathVariable Long car_id) {
        if(carsRepository.existsById(car_id) && editedCar.getClientId() == null){
            carsRepository.findById(car_id).map(car -> {
                car.setBrand(editedCar.getBrand());
                car.setModel(editedCar.getModel());
                return carsRepository.save(car);
            }).get();
            return customResponse.getResponseOK(car_id, "car"," edit successful", "/car-rental/cars/edit/" + car_id);
        }else{
            return customResponse.getResponseBAD_REQUEST(car_id,"car"," car edit fail!!!", "/car-rental/cars/edit/" + car_id);
        }
    }

    @PutMapping("/cars/giveBack/{car_id}")
    @ResponseBody
    ResponseEntity giveBackCar(@PathVariable Long car_id){
        if(carsRepository.existsById(car_id)){
            carsRepository.findById(car_id).map(car -> {
                car.setClientId(Long.valueOf(0));
                return carsRepository.save(car);
            }).get();
            return customResponse.getResponseOK(car_id, "car"," give back successful", "/car-rental/cars/giveBack/" + car_id);
        }else{
            return customResponse.getResponseBAD_REQUEST(car_id, "car"," something went wrong", "/car-rental/cars/giveBack/" + car_id);
        }
    }

    @PutMapping("/cars/rent")//{?car_id=value&client_id=value}
    @ResponseBody
    ResponseEntity rentCar(@RequestParam Long car_id, @RequestParam Long client_id) {
        //Optional<Car> carPrev = carsRepository.findById(car_id);
        if(carsRepository.existsById(car_id) && carsRepository.findById(car_id).get().getClientId() == 0 && clientsRepository.existsById(client_id) ) {
            carsRepository.findById(car_id).map(car -> {
                car.setClientId(client_id);
                return carsRepository.save(car);
            }).get();
            return customResponse.getResponseOK(car_id, "car"," is rent","/car-rental/cars/rent/?car_id=" + car_id + "&client_id=" + client_id);
        }else{
            return customResponse.getResponseBAD_REQUEST(car_id, "car"," rent fail!!!", "/car-rental/cars/rent/?car_id=" + car_id + "&client_id=" + client_id);
        }
    }

    @DeleteMapping("/cars/{car_id}")
    @ResponseBody
    ResponseEntity deleteCar(@PathVariable Long car_id) {
        if(carsRepository.existsById(car_id)){
            carsRepository.deleteById(car_id);
            return customResponse.getResponseOK(car_id, "car"," delete successful","/car-rental/cars/"+car_id);
        }else{
            return customResponse.getResponseBAD_REQUEST(car_id, "car"," delete fail!!!","/car-rental/cars/"+car_id);
        }
    }

}

