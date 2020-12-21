package com.example.dbcar_rental.integrationTests;

import com.example.dbcar_rental.DbCarRentalApplication;
import com.example.dbcar_rental.entities.Car;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "application_test.properties")
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(statements = {"DELETE FROM CLIENT;", "DELETE FROM CAR;",
        "INSERT INTO CLIENT (\"first_name\",\"last_name\") VALUES (\"Jan\", \"Nowak\");",
        "INSERT INTO CLIENT (\"first_name\",\"last_name\") VALUES (\"Adam\", \"Kowalski\");",
        "INSERT INTO CLIENT (\"first_name\",\"last_name\") VALUES (\"Krzysztof\", \"Polski\");",
        "INSERT INTO CAR (\"brand\",\"model\",\"client_id\") VALUES (\"BMW\", \"M4\",0);",
        "INSERT INTO CAR (\"brand\",\"model\",\"client_id\") VALUES (\"Renault\", \"Megane-RS\",1);",
        "INSERT INTO CAR (\"brand\",\"model\",\"client_id\") VALUES (\"Audi\", \"RS6\",2);",})
public class CarsRestControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    ObjectMapper om = new ObjectMapper();

    @Test
    public void getAllCars() throws Exception{
        mvc.perform(get("/car-rental/cars")
                .content(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].carId", is(1)))
                .andExpect(jsonPath("$[0].brand", is("BMW")))
                .andExpect(jsonPath("$[0].model", is("M4")))
                .andExpect(jsonPath("$[0].clientId", is(0)))
                .andExpect(jsonPath("$[1].carId", is(2)))
                .andExpect(jsonPath("$[1].brand", is("Renault")))
                .andExpect(jsonPath("$[1].model", is("Megane-RS")))
                .andExpect(jsonPath("$[1].clientId", is(1)))
                .andExpect(jsonPath("$[2].carId", is(3)))
                .andExpect(jsonPath("$[2].brand", is("Audi")))
                .andExpect(jsonPath("$[2].model", is("RS6")))
                .andExpect(jsonPath("$[2].clientId", is(2)));
    }

    @Test
    public void getCarById_car_exist() throws Exception{
        mvc.perform(get("/car-rental/cars/{car_id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carId", is(1)))
                .andExpect(jsonPath("$.brand", is("BMW")))
                .andExpect(jsonPath("$.model", is("M4")));
    }

    @Test
    public void getCarById_car_not_exist() throws Exception {
        mvc.perform(get("/car-rental/cars/{car_id}", 8L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", is("car id-8 get fail!!!")));
    }

    @Test
    public void addCar_client_exist_or_equals_zero() throws Exception {
        Car car1 = new Car("Mercedes","A45-AMG",0L);
        String request = om.writeValueAsString(car1);

        mvc.perform(post("/car-rental/cars")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description", is("CREATED")))
                .andExpect(jsonPath("$[*]", hasSize(5)))
                .andExpect(jsonPath("$.message", is("car id-4 was created")));
    }

    @Test
    public void addCar_client_not_exist_or_different_zero() throws Exception {
        Car car1 = new Car("Mercedes","A45-AMG",5L);
        String request = om.writeValueAsString(car1);

        mvc.perform(post("/car-rental/cars")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description", is("BAD_REQUEST")))
                .andExpect(jsonPath("$[*]", hasSize(5)))
                .andExpect(jsonPath("$.message", is("client id-5 client not exist")));
    }

    @Test
    public void editCar_car_exist_and_clientId_is_null() throws Exception {
        Car carPut = new Car("BMW","M5");
        String request = om.writeValueAsString(carPut);

        mvc.perform(put("/car-rental/cars/edit/{car_id}", 1L)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("OK")))
                .andExpect(jsonPath("$.message", is("car id-1 edit successful")));
    }

    @Test
    public void editCar_car_not_exist_and_clientId_is_not_null() throws Exception {
        Car carPut = new Car(1L,"BMW","M5",1L);
        String request = om.writeValueAsString(carPut);

        mvc.perform(put("/car-rental/cars/edit/{car_id}", 1L)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", is("car id-1 car edit fail!!!")));
    }

    @Test
    public void giveBackCar_car_exists() throws Exception{
        mvc.perform(put("/car-rental/cars/giveBack/{car_id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("OK")))
                .andExpect(jsonPath("$.message", is("car id-1 give back successful")));
    }

    @Test
    public void giveBackCar_car_not_exists() throws Exception{
        mvc.perform(put("/car-rental/cars/giveBack/{car_id}", 8L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", is("car id-8 something went wrong")));
    }

    @Test
    public void rentCar_proper_case() throws Exception {
        mvc.perform(put("/car-rental/cars/rent?car_id=1&client_id=3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("OK")))
                .andExpect(jsonPath("$.message", is("car id-1 is rent")));
    }

    @Test
    public void rentCar_fail_case() throws Exception {
        mvc.perform(put("/car-rental/cars/rent?car_id=3&client_id=3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", is("car id-3 rent fail!!!")));
    }

    @Test
    public void deleteCar_proper_case() throws Exception {
        mvc.perform(delete("/car-rental/cars/{car_id}",3L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("OK")))
                .andExpect(jsonPath("$.message", is("car id-3 delete successful")));
    }

    @Test
    public void deleteCar_fail_case() throws Exception {
        mvc.perform(delete("/car-rental/cars/{car_id}",4L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", is("car id-4 delete fail!!!")));
    }
}