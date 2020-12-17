package com.example.dbcar_rental.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private Long carId;
    private String brand;
    private String model;
    private  Long clientId;

    public Car(){}

    public Car(String brand, String model){
        this.brand = brand;
        this.model = model;
    }

    public Car(String brand, String model, Long clientId){
        this.brand = brand;
        this.model = model;
        this.clientId = clientId;
    }

    public Car(Long carId, String brand, String model, Long clientId){
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.clientId = clientId;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString(){
        return "Car:[id=" + carId + " ,brand=" + brand + " ,model=" + model + " ,userId=" + clientId + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return carId.equals(car.carId) &&
                brand.equals(car.brand) &&
                model.equals(car.model) &&
                clientId.equals(car.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carId, brand, model, clientId);
    }
}
