package com.example.dbcar_rental.integrationTests;
import com.example.dbcar_rental.entities.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(properties = "application_test.properties")
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(statements = {"DELETE FROM CLIENT;",
        "INSERT INTO CLIENT (\"first_name\",\"last_name\") VALUES (\"Jan\", \"Nowak\");",
        "INSERT INTO CLIENT (\"first_name\",\"last_name\") VALUES (\"Adam\", \"Kowalski\");",
        "INSERT INTO CLIENT (\"first_name\",\"last_name\") VALUES (\"Krzysztof\", \"Polski\");"})
public class ClientsRestControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    ObjectMapper om = new ObjectMapper();

    @Test
    public void getAllClients() throws Exception {
        mvc.perform(get("/car-rental/clients")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].clientId").exists())
                .andExpect(jsonPath("$[*].clientId").isNotEmpty())
                .andExpect(jsonPath("$[0].firstName", is("Jan")));
    }

    @Test
    public void getClientById_client_exist() throws Exception {
        mvc.perform( MockMvcRequestBuilders
                .get("/car-rental/clients/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jan"));
    }

    @Test
    public void getClientById_client_not_exist() throws Exception {
        mvc.perform( MockMvcRequestBuilders
                .get("/car-rental/clients/{id}", 4)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.message").value("client id-4 get fail!!!"));
    }

    @Test
    public void addClient_client_not_exist() throws Exception {
        Client client1 = new  Client("Krzysztof","Kowalczyk");
        String request = om.writeValueAsString(client1);

        mvc.perform(post("/car-rental/clients")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description", is("CREATED")))
                .andExpect(jsonPath("$.message",is("client id-4 was created")));
    }

    @Test
    public void addClient_client_exist() throws Exception {
        Client client1 = new  Client(1L,"Jan","Nowak");
        String request = om.writeValueAsString(client1);

        mvc.perform(post("/car-rental/clients")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.message",is("client id-1 client add fail!!!")));;
    }

    @Test
    public void editClient_client_exist_and_clientId_is_null() throws Exception {
        Client clientPut = new  Client("Jakub","Kowalski");
        String request = om.writeValueAsString(clientPut);

        mvc.perform(put("/car-rental/clients/edit/{client_id}", 1L)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("OK")))
                .andExpect(jsonPath("$.message", is("client id-1 edit successful")));
    }

    @Test
    public void editClient_client_not_exist_and_clientId_is_not_null() throws Exception {
        Client clientPut = new  Client("Jakub","Kowalski");
        String request = om.writeValueAsString(clientPut);

        mvc.perform(put("/car-rental/clients/edit/{client_id}", 8L)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", is("client id-8 client edit fail!!!")));
    }

    @Test
    public void deleteClient_proper_case() throws Exception {
        mvc.perform(delete("/car-rental/clients/{client_id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("OK")))
                .andExpect(jsonPath("$.message", is("client id-1 delete successful")));
    }

    @Test
    public void deleteClient_fail_case() throws Exception {
        mvc.perform(delete("/car-rental/clients/{client_id}", 8L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", is("client id-8 delete fail!!!")));
    }
}