package com.example.dbcar_rental.unitTests.entites;

import com.example.dbcar_rental.entities.Car;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarUnitTest {
    private Car testCar = new Car(Long.valueOf(1),"Audi","RS6",Long.valueOf(2));

    @Test
    void getCarId() {
        assertEquals(1,testCar.getCarId());
    }

    @Test
    void setCarId() {
        testCar.setCarId(Long.valueOf(2));
        assertEquals(2,testCar.getCarId());
    }

    @Test
    void getBrand() {
        assertTrue(testCar.getBrand().equals("Audi"));
    }

    @Test
    void setBrand() {
        testCar.setBrand("BMW");
        assertTrue(testCar.getBrand().equals("BMW"));
    }

    @Test
    void getModel() {
        assertTrue(testCar.getModel().equals("RS6"));
    }

    @Test
    void setModel() {
        testCar.setModel("M5");
        assertTrue(testCar.getModel().equals("M5"));
    }

    @Test
    void getClientId() {
        assertEquals(2,testCar.getClientId());
    }

    @Test
    void setClientId() {
        testCar.setClientId(Long.valueOf(5));
        assertEquals(5,testCar.getClientId());
    }

    @Test
    void testToString() {
        Car testCar2 = new Car(Long.valueOf(1),"Audi","RS6",Long.valueOf(2));
        String proper = "Car:[id=1 ,brand=Audi ,model=RS6 ,userId=2]";
        assertTrue(testCar2.toString().equals(proper));
    }

    @Test
    void testEquals(){
        Car testCar1 = new Car(Long.valueOf(1),"Audi","RS6",Long.valueOf(2));
        Car testCar2 = new Car(Long.valueOf(1),"Audi","RS6",Long.valueOf(2));
        Car testCar3 = new Car(Long.valueOf(1),"Audi","RS5",Long.valueOf(2));

        assertTrue(testCar1.equals(testCar2));
        assertFalse(testCar1.equals(testCar3));
    }

    @Test
    void testHashCode(){
        Car testCar1 = new Car(Long.valueOf(1),"Audi","RS6",Long.valueOf(2));
        Car testCar2 = new Car(Long.valueOf(1),"Audi","RS6",Long.valueOf(2));
        Car testCar3 = new Car(Long.valueOf(1),"Audi","RS5",Long.valueOf(2));

        assertEquals(testCar1.hashCode(),testCar2.hashCode());
        assertNotEquals(testCar1.hashCode(),testCar3.hashCode());
    }
}