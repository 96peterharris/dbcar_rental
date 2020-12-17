package com.example.dbcar_rental.api;

import com.example.dbcar_rental.custom_response.CustomResponse;
import com.example.dbcar_rental.entities.Client;
import com.example.dbcar_rental.repositories.ClientsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/car-rental", produces = { MediaType.APPLICATION_JSON_VALUE })
public class ClientsRestController {
    private ClientsRepository clientsRepository;
    private final CustomResponse customResponse = new CustomResponse();

    @Autowired
    public void setClientsRepository(ClientsRepository clientsRepository){
        this.clientsRepository = clientsRepository;
    }

    @GetMapping(value = "/clients")
    public List<Client> getAllClients() {
        return clientsRepository.findAll();
    }

    @GetMapping(value = "/clients/{client_id}")
    ResponseEntity getClientById(@PathVariable Long client_id) throws JsonProcessingException {
        if(clientsRepository.existsById(client_id)){
            return customResponse.getResponseOK_singleObject(clientsRepository.findById(client_id).get());
        }else{
            return customResponse.getResponseBAD_REQUEST(client_id,"client", " get fail!!!","clients/"+client_id);
        }

    }

    @PostMapping("/clients")
    @ResponseBody
    ResponseEntity addClient(@RequestBody Client newClient){
        if(newClient.getClientId() == null || clientsRepository.existsById(newClient.getClientId()) == false){
            clientsRepository.save(newClient);
            return customResponse.getResponseCREATED(newClient.getClientId(),"client", "/car-rental/clients/" + newClient.getClientId());
        }else{
            return customResponse.getResponseBAD_REQUEST(newClient.getClientId(),"client", " client add fail!!!","/car-rental/clients/" + newClient.getClientId());
        }
    }

    @PutMapping("/clients/edit/{client_id}")
    @ResponseBody
    ResponseEntity editClient(@RequestBody Client editedClient, @PathVariable Long client_id) {
        if(clientsRepository.existsById(client_id) && editedClient.getClientId() == null){
            clientsRepository.findById(client_id).map(client -> {
                client.setFirstName(editedClient.getFirstName());
                client.setLastName(editedClient.getLastName());
                return clientsRepository.save(client);
            }).get();
            return customResponse.getResponseOK(client_id, "client"," edit successful", "/car-rental/clients/edit/" + client_id);
        }else{
            return customResponse.getResponseBAD_REQUEST(client_id,"client"," client edit fail!!!", "/car-rental/clients/edit/" + client_id);
        }
    }

    @DeleteMapping("/clients/{client_id}")
    @ResponseBody
    ResponseEntity deleteClient(@PathVariable Long client_id) {
        if(clientsRepository.existsById(client_id)){
            clientsRepository.deleteById(client_id);
            return customResponse.getResponseOK(client_id, "client"," delete successful","/car-rental/clients/" + client_id);
        }else{
            return customResponse.getResponseBAD_REQUEST(client_id, "client"," delete fail!!!","/car-rental/clients/" + client_id);
        }
    }

}
