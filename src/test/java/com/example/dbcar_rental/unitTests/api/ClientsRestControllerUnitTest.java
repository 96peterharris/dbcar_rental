package com.example.dbcar_rental.unitTests.api;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.example.dbcar_rental.entities.Client;
import com.example.dbcar_rental.repositories.ClientsRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientsRestControllerUnitTest {

    @MockBean
    private ClientsRepository clientsRepository;

    @Autowired
    private MockMvc mvc;

    ObjectMapper om = new ObjectMapper();

    @Test
    public void getAllClients() throws Exception {
        Client client1 = new  Client(1L,"Jan","Kowalski");
        Client client2 = new  Client(2L,"Adam","Nowak");
        doReturn(Lists.newArrayList(client1,client2)).when(clientsRepository).findAll();

        mvc.perform(get("/car-rental/clients")
                .content(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].clientId", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("Jan")))
                .andExpect(jsonPath("$[0].lastName", is("Kowalski")))
                .andExpect(jsonPath("$[1].clientId", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("Adam")))
                .andExpect(jsonPath("$[1].lastName", is("Nowak")));
    }

    @Test
    public void getClientById_client_exist() throws Exception {
        Client client1 = new  Client(1l,"Jan","Kowalski");
        doReturn(Optional.of(client1)).when(clientsRepository).findById(1l);
        when(clientsRepository.existsById(1L)).thenReturn(true);

        mvc.perform(get("/car-rental/clients/{client_id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId", is(1)))
                .andExpect(jsonPath("$.firstName", is("Jan")))
                .andExpect(jsonPath("$.lastName", is("Kowalski")));
    }

    @Test
    public void getClientById_client_not_exist() throws Exception {
        when(clientsRepository.existsById(1L)).thenReturn(false);

        mvc.perform(get("/car-rental/clients/{client_id}", 1L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("client id-1 get fail!!!")));
    }

    @Test
    public void addClient_client_not_exist() throws Exception {
        Client client1 = new  Client(1L,"Jan","Kowalski");
        String request = om.writeValueAsString(client1);
        doReturn(client1).when(clientsRepository).save(client1);

        mvc.perform(post("/car-rental/clients")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[*]", hasSize(5)))
                .andExpect(jsonPath("$.message", is("client id-1 was created")));
    }

    @Test
    public void addClient_client_exist() throws Exception {
        Client client1 = new  Client(1L,"Jan","Kowalski");
        String request = om.writeValueAsString(client1);
        doReturn(client1).when(clientsRepository).save(client1);
        when(clientsRepository.existsById(1L)).thenReturn(true);

        mvc.perform(post("/car-rental/clients")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*]", hasSize(5)))
                .andExpect(jsonPath("$.message", is("client id-1 client add fail!!!")));
    }

    @Test
    public void editClient_client_exist_and_clientId_is_null() throws Exception {
        Client clientPut = new  Client("Jan","Kowalski");
        Client clientFind = new  Client(1L,"Jan","Kowalski");
        Client clientSave = new  Client(1L,"Jan","Kowalski");
        String request = om.writeValueAsString(clientPut);

        doReturn(Optional.of(clientFind)).when(clientsRepository).findById(1L);
        doReturn(clientSave).when(clientsRepository).save(any());
        when(clientsRepository.existsById(1L)).thenReturn(true);

        mvc.perform(put("/car-rental/clients/edit/{client_id}", 1L)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("client id-1 edit successful")));
    }

    @Test
    public void editClient_client_not_exist_and_clientId_is_not_null() throws Exception {
        Client clientPut = new  Client("Jan","Kowalski");
        Client clientFind = new  Client(1L,"Jan","Kowalski");
        Client clientSave = new  Client(1L,"Jan","Kowalski");
        String request = om.writeValueAsString(clientPut);

        doReturn(Optional.of(clientFind)).when(clientsRepository).findById(1L);
        doReturn(clientSave).when(clientsRepository).save(any());

        mvc.perform(put("/car-rental/clients/edit/{client_id}", 1L)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("client id-1 client edit fail!!!")));
    }

    @Test
    public void deleteClient_proper_case() throws Exception {
        Client client1 = new  Client(1L,"Jan","Kowalski");

        doReturn(client1).when(clientsRepository).save(client1);
        when(clientsRepository.existsById(1L)).thenReturn(true);

        mvc.perform(delete("/car-rental/clients/{client_id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("client id-1 delete successful")));
    }

    @Test
    public void deleteClient_fail_case() throws Exception {
        Client client1 = new  Client(1L,"Jan","Kowalski");

        doReturn(client1).when(clientsRepository).save(client1);
        when(clientsRepository.existsById(1L)).thenReturn(false);

        mvc.perform(delete("/car-rental/clients/{client_id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("client id-1 delete fail!!!")));

        verify(clientsRepository, times(0)).deleteById(1L);
    }
}