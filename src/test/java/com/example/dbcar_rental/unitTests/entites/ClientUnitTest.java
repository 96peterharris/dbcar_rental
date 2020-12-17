package com.example.dbcar_rental.unitTests.entites;

import com.example.dbcar_rental.entities.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientUnitTest {
    private Client testClient = new Client(Long.valueOf(1),"Jan","Kowalski");

    @Test
    void getClientID() {
        assertEquals(1,testClient.getClientId());
    }

    @Test
    void setClientID() {
        testClient.setClientId(Long.valueOf(2));
        assertEquals(2,testClient.getClientId());
    }

    @Test
    void getFirstName() {
        assertTrue(testClient.getFirstName().equals("Jan"));
    }

    @Test
    void setFirstName() {
        testClient.setFirstName("Andrzej");
        assertTrue(testClient.getFirstName().equals("Andrzej"));
    }

    @Test
    void getLastName() {
        assertTrue(testClient.getLastName().equals("Kowalski"));
    }

    @Test
    void setLastName() {
        testClient.setLastName("Nowak");
        assertTrue(testClient.getLastName().equals("Nowak"));
    }

    @Test
    void testToString() {
        Client testClient1 = new Client(Long.valueOf(1),"Jan","Kowalski");
        String proper = "Client:[id=1 ,firstName=Jan ,lastName=Kowalski]";
        assertTrue(testClient1.toString().equals(proper));
    }

    @Test
    void testEquals() {
        Client testClient1 = new Client(Long.valueOf(1),"Jan","Kowalski");
        Client testClient2 = new Client(Long.valueOf(1),"Jan","Kowalski");
        Client testClient3 = new Client(Long.valueOf(1),"Jan","Nowak");

        assertTrue(testClient1.equals(testClient2));
        assertFalse(testClient1.equals(testClient3));
    }

    @Test
    void testHashCode() {
        Client testClient1 = new Client(Long.valueOf(1),"Jan","Kowalski");
        Client testClient2 = new Client(Long.valueOf(1),"Jan","Kowalski");
        Client testClient3 = new Client(Long.valueOf(1),"Jan","Nowak");

        assertEquals(testClient1.hashCode(),testClient2.hashCode());
        assertNotEquals(testClient1.hashCode(),testClient3.hashCode());
    }
}