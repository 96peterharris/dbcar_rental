package com.example.dbcar_rental.entities;


import javax.persistence.*;
import java.util.Objects;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long clientId;
    private String firstName;
    private String lastName;

    public Client(){}

    public  Client(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public  Client(Long clientId, String firstName, String lastName){
        this.clientId = clientId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString(){
        return "Client:[id=" + clientId + " ,firstName=" + firstName + " ,lastName=" + lastName + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return clientId.equals(client.clientId) &&
                firstName.equals(client.firstName) &&
                lastName.equals(client.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, firstName, lastName);
    }
}
