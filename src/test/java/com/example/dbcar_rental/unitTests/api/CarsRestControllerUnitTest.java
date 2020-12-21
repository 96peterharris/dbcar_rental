package com.example.dbcar_rental.unitTests.api;
import com.example.dbcar_rental.entities.Car;
import com.example.dbcar_rental.repositories.CarsRepository;
import com.example.dbcar_rental.repositories.ClientsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CarsRestControllerUnitTest {

    @MockBean
    private CarsRepository carsRepository;

    @MockBean
    private ClientsRepository clientsRepository;

    @Autowired
    private MockMvc mvc;

    ObjectMapper om = new ObjectMapper();

    @Test
    void getAllCars() throws Exception {
        Car car1 = new Car(1L,"BMW","M4",0L);
        Car car2 = new Car(2L,"Renault","Megane RS",1L);

        doReturn(Lists.newArrayList(car1,car2)).when(carsRepository).findAll();

        mvc.perform(get("/car-rental/cars")
                .content(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].carId", is(1)))
                .andExpect(jsonPath("$[0].brand", is("BMW")))
                .andExpect(jsonPath("$[0].model", is("M4")))
                .andExpect(jsonPath("$[0].clientId", is(0)))
                .andExpect(jsonPath("$[1].carId", is(2)))
                .andExpect(jsonPath("$[1].brand", is("Renault")))
                .andExpect(jsonPath("$[1].model", is("Megane RS")))
                .andExpect(jsonPath("$[1].clientId", is(1)));
    }

    @Test
    void getCarById_car_exist() throws Exception {
        Car car1 = new Car(1L,"BMW","M4",0L);

        doReturn(Optional.of(car1)).when(carsRepository).findById(1l);
        when(carsRepository.existsById(1L)).thenReturn(true);

        mvc.perform(get("/car-rental/cars/{car_id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carId", is(1)))
                .andExpect(jsonPath("$.brand", is("BMW")))
                .andExpect(jsonPath("$.model", is("M4")));
    }

    @Test
    void getCarById_car_not_exist() throws Exception {
        when(carsRepository.existsById(1L)).thenReturn(false);

        mvc.perform(get("/car-rental/cars/{car_id}", 2L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.description", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", is("car id-2 get fail!!!")));
    }

    @Test
    void addCar_client_exist_or_equals_zero() throws Exception {
        Car car1 = new Car(1L,"BMW","M4",0L);
        String request = om.writeValueAsString(car1);

        doReturn(car1).when(carsRepository).save(car1);

        mvc.perform(post("/car-rental/cars")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description", is("CREATED")))
                .andExpect(jsonPath("$[*]", hasSize(5)))
                .andExpect(jsonPath("$.message", is("car id-1 was created")));
    }

    @Test
    void addCar_client_not_exist_or_different_zero() throws Exception {
        Car car1 = new Car(1L,"BMW","M4",5L);
        String request = om.writeValueAsString(car1);

        doReturn(car1).when(carsRepository).save(car1);

        mvc.perform(post("/car-rental/cars")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description", is("BAD_REQUEST")))
                .andExpect(jsonPath("$[*]", hasSize(5)))
                .andExpect(jsonPath("$.message", is("client id-5 client not exist")));
    }

    @Test
    void editCar_car_exist_and_clientId_is_null() throws Exception {
        Car carPut = new Car("BMW","M5");
        Car carFind = new Car(1L,"BMW","M4",0L);
        Car carSave = new Car(1L,"BMW","M4",0L);
        String request = om.writeValueAsString(carPut);

        doReturn(Optional.of(carFind)).when(carsRepository).findById(1L);
        doReturn(carSave).when(carsRepository).save(any());
        when(carsRepository.existsById(1L)).thenReturn(true);

        mvc.perform(put("/car-rental/cars/edit/{car_id}", 1L)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("OK")))
                .andExpect(jsonPath("$.message", is("car id-1 edit successful")));
    }

    @Test
    void editCar_car_not_exist_and_clientId_is_not_null() throws Exception {
        Car carPut = new Car(1L,"BMW","M5",1L);
        Car carFind = new Car(1L,"BMW","M4",0L);
        Car carSave = new Car(1L,"BMW","M4",0L);
        String request = om.writeValueAsString(carPut);

        doReturn(Optional.of(carFind)).when(carsRepository).findById(1L);
        doReturn(carSave).when(carsRepository).save(any());

        mvc.perform(put("/car-rental/cars/edit/{car_id}", 1L)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", is("car id-1 car edit fail!!!")));
    }

    @Test
    void giveBackCar_car_exists() throws Exception{
        Car carPut = new Car("BMW","M4",0L);
        Car carFind = new Car(1L,"BMW","M4",1L);
        Car carSave = new Car(1L,"BMW","M4",1L);
        String request = om.writeValueAsString(carPut);

        when(carsRepository.existsById(1L)).thenReturn(true);
        doReturn(Optional.of(carFind)).when(carsRepository).findById(1L);
        doReturn(carSave).when(carsRepository).save(any());

        mvc.perform(put("/car-rental/cars/giveBack/{car_id}", 1L)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("OK")))
                .andExpect(jsonPath("$.message", is("car id-1 give back successful")));
    }

    @Test
    void giveBackCar_car_not_exists() throws Exception{
        Car carPut = new Car("BMW","M4",0L);
        Car carFind = new Car(1L,"BMW","M4",1L);
        Car carSave = new Car(1L,"BMW","M4",1L);
        String request = om.writeValueAsString(carPut);

        doReturn(Optional.of(carFind)).when(carsRepository).findById(1L);
        doReturn(carSave).when(carsRepository).save(any());

        mvc.perform(put("/car-rental/cars/giveBack/{car_id}", 1L)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", is("car id-1 something went wrong")));
    }

    @Test
    void rentCar_proper_case() throws Exception {
        Car carFind = new Car(1L,"BMW","M4",0L);
        Car carSave = new Car(1L,"BMW","M4",1L);

        when(carsRepository.existsById(1L)).thenReturn(true);
        when(clientsRepository.existsById(1L)).thenReturn(true);
        doReturn(Optional.of(carFind)).when(carsRepository).findById(1L);
        doReturn(carSave).when(carsRepository).save(any());

        mvc.perform(put("/car-rental/cars/rent?car_id=1&client_id=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("OK")))
                .andExpect(jsonPath("$.message", is("car id-1 is rent")));
    }

    @Test
    void rentCar_fail_case() throws Exception {
        Car carFind = new Car(1L,"BMW","M4",1L);
        Car carSave = new Car(1L,"BMW","M4",2L);

        when(carsRepository.existsById(1L)).thenReturn(true);
        when(clientsRepository.existsById(2L)).thenReturn(true);
        doReturn(Optional.of(carFind)).when(carsRepository).findById(1L);
        doReturn(carSave).when(carsRepository).save(any());

        mvc.perform(put("/car-rental/cars/rent?car_id=1&client_id=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", is("car id-1 rent fail!!!")));
    }

    @Test
    void deleteCar_proper_case() throws Exception {
        Car car1 = new Car(1L,"BMW","M4",0L);

        doReturn(car1).when(carsRepository).save(car1);
        when(carsRepository.existsById(1L)).thenReturn(true);

        mvc.perform(delete("/car-rental/cars/{car_is}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("OK")))
                .andExpect(jsonPath("$.message", is("car id-1 delete successful")));

        verify(carsRepository).deleteById(1L);
    }

    @Test
    void deleteCar_fail_case() throws Exception {
        Car car1 = new Car(1L,"BMW","M4",0L);

        doReturn(car1).when(carsRepository).save(car1);
        when(carsRepository.existsById(1L)).thenReturn(false);

        mvc.perform(delete("/car-rental/cars/{car_id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", is("car id-1 delete fail!!!")));

        verify(carsRepository, times(0)).deleteById(1L);
    }
}