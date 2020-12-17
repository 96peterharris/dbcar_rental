package com.example.dbcar_rental.custom_response;


;
import com.example.dbcar_rental.entities.Car;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tsohr.JSONObject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.sql.Timestamp;

public class CustomResponse {
    private JSONObject responseOK;
    private JSONObject responseBAD_REQUEST;
    private JSONObject responseCREATED;
    private JSONObject responseOK_singleObject;

    public CustomResponse(){
        this.responseOK = new JSONObject();
        this.responseOK.put("timestamp",new Timestamp(System.currentTimeMillis()).toInstant());
        this.responseOK.put("status", HttpStatus.OK.value());
        this.responseOK.put("description", "OK");

        this.responseBAD_REQUEST = new JSONObject();
        this.responseBAD_REQUEST.put("timestamp",new Timestamp(System.currentTimeMillis()).toInstant());
        this.responseBAD_REQUEST.put("status", HttpStatus.BAD_REQUEST.value());
        this.responseBAD_REQUEST.put("description", "BAD_REQUEST");

        this.responseCREATED = new JSONObject();
        this.responseCREATED.put("timestamp",new Timestamp(System.currentTimeMillis()).toInstant());
        this.responseCREATED.put("status", HttpStatus.CREATED.value());
        this.responseCREATED.put("description", "CREATED");

        this.responseOK_singleObject = new JSONObject();
    }

    public ResponseEntity getResponseOK(Long idArg, String type, String description, String path){
        this.responseOK.put("message", type + " id-" + idArg + description);
        this.responseOK.put("path", path);
        return new ResponseEntity(this.responseOK.toString(), HttpStatus.OK);
    }

    public ResponseEntity getResponseOK_singleObject(Long idArg, String type, String description, String path){
        this.responseOK.put("message", type + " id-" + idArg + description);
        this.responseOK.put("path", path);
        return new ResponseEntity(this.responseOK.toString(), HttpStatus.OK);
    }

    public ResponseEntity getResponseCREATED(Long idArg, String type, String path){
        this.responseCREATED.put("message",  type + " id-" + idArg + " was created");
        this.responseCREATED.put("path", path);
        return new ResponseEntity(this.responseCREATED.toString(), HttpStatus.CREATED);
    }

    public ResponseEntity getResponseBAD_REQUEST(Long idArg, String type, String description, String path){
        this.responseBAD_REQUEST.put("message", type + " id-" + idArg + description);
        this.responseBAD_REQUEST.put("path", path);
        return new ResponseEntity(this.responseBAD_REQUEST.toString(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity getResponseOK_singleObject(Object obj) throws JsonProcessingException {
        this.responseOK_singleObject = new JSONObject(new ObjectMapper().writeValueAsString(obj));
        return new ResponseEntity(this.responseOK_singleObject.toString(), HttpStatus.OK);
    }

}

