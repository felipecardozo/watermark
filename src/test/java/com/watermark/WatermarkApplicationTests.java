package com.watermark;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.watermark.model.ContentType;
import com.watermark.model.Document;
import com.watermark.model.Status;
import com.watermark.model.Ticket;
import com.watermark.model.WatermarkBook;
import com.watermark.model.WatermarkJournal;
import com.watermark.repository.WatermarkRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class WatermarkApplicationTests {

    private TestRestTemplate restTemplate;

    private HttpHeaders headers;

    @LocalServerPort
    private int port;

    @Autowired
    private WatermarkRepository repository;

    @Before
    public void setUp() {
        restTemplate = new TestRestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void shouldCallGetDocumentByTicketWithExistingDocument() throws JSONException, InterruptedException, JsonProcessingException {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        Document doc = repository.getAllDocuments().get(0);
        ResponseEntity<String> response =
                restTemplate.exchange(createURLWithPort("/" + doc.getTicket().getTicket()), HttpMethod.GET, entity, String.class);

        String expected = new ObjectMapper().writeValueAsString(doc);
        JSONAssert.assertEquals(expected, response.getBody(), false);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldCallGetDocumentByTicketWithNonExistingDocument() throws JSONException, InterruptedException, JsonProcessingException {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/fakeid0000abc"), HttpMethod.GET, entity, String.class);

        JSONAssert.assertEquals(null, response.getBody(), false);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldCreateADocument() throws JsonProcessingException, JSONException {
        Document toCreate = new Document("Learning Java in 1 day", "John Doe", null, null, null);
        HttpEntity<String> entity = new HttpEntity<String>(new ObjectMapper().writeValueAsString(toCreate), headers);

        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("ticket"));
    }

    @Test
    public void shouldCreateADocumentWithStatusInProgress() throws JSONException, IOException, InterruptedException {
        Document toCreate = new Document("Learning Java in 1 day", "John Doe", null, null, null);
        HttpEntity<String> entity = new HttpEntity<String>(new ObjectMapper().writeValueAsString(toCreate), headers);

        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("ticket"));

        Ticket ticket = new ObjectMapper().readValue(response.getBody(), Ticket.class);
        response = restTemplate.exchange(createURLWithPort("/" + ticket.getTicket()), HttpMethod.GET, new HttpEntity<String>(null, headers),
                String.class);

        Document documentExpected = repository.getAllDocuments().get(repository.getAllDocuments().size() - 1);
        String expected = new ObjectMapper().writeValueAsString(documentExpected);
        Document documentFromRepository = new ObjectMapper().readValue(response.getBody(), Document.class);
        JSONAssert.assertEquals(expected, response.getBody(), false);
        assertEquals(Status.INPROGRESS, documentFromRepository.getStatus());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldCreateADocumentWithStatusFinished() throws JSONException, IOException, InterruptedException {
        WatermarkBook property = new WatermarkBook();
        property.setAuthor("John Doe");
        property.setContent(ContentType.JOURNAL.toString());
        property.setTitle("Learning Java en 1 day");
        Document toCreate = new Document("Learning Java in 1 day", "John Doe", property, null, null);
        HttpEntity<String> entity = new HttpEntity<String>(new ObjectMapper().writeValueAsString(toCreate), headers);

        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/"), HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("ticket"));

        Ticket ticket = new ObjectMapper().readValue(response.getBody(), Ticket.class);
        response = restTemplate.exchange(createURLWithPort("/" + ticket.getTicket()), HttpMethod.GET, new HttpEntity<String>(null, headers),
                String.class);

        Document documentExpected = repository.getAllDocuments().get(repository.getAllDocuments().size() - 1);
        String expected = new ObjectMapper().writeValueAsString(documentExpected);
        Document documentFromRepository = new ObjectMapper().readValue(response.getBody(), Document.class);
        JSONAssert.assertEquals(expected, response.getBody(), false);
        assertEquals(Status.FINISHED, documentFromRepository.getStatus());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldUpdateAnExistingDocument() throws InterruptedException, JsonProcessingException {
        Document toUpdate = repository.getAllDocuments().get(0);
        toUpdate.setAuthor("John Kent");
        HttpEntity<String> entity = new HttpEntity<String>(new ObjectMapper().writeValueAsString(toUpdate), headers);

        ResponseEntity<Document> response =
                restTemplate.exchange(createURLWithPort("/" + toUpdate.getTicket().getTicket()), HttpMethod.PUT, entity, Document.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(toUpdate.getAuthor(), response.getBody().getAuthor());
    }

    @Test
    public void shouldUpdateAnExistingDocumentAndUpdateStatusFromInprogressToFinished() throws InterruptedException, JsonProcessingException {
        Document toCreate = new Document("Learning Java in 1 day", "John Doe", null, null, null);
        HttpEntity<String> entity = new HttpEntity<String>(new ObjectMapper().writeValueAsString(toCreate), headers);

        ResponseEntity<Ticket> responseTicket = restTemplate.exchange(createURLWithPort("/"), HttpMethod.POST, entity, Ticket.class);
        WatermarkJournal watermarkProperty = new WatermarkJournal();
        watermarkProperty.setAuthor("john Doe");
        watermarkProperty.setContent(ContentType.JOURNAL.toString());
        watermarkProperty.setTitle("Learning Java in 1 day");
        toCreate.setWatermarkProperty(watermarkProperty);
        entity = new HttpEntity<String>(new ObjectMapper().writeValueAsString(toCreate), headers);
        ResponseEntity<Document> responseDoc =
                restTemplate.exchange(createURLWithPort("/" + responseTicket.getBody().getTicket()), HttpMethod.PUT, entity, Document.class);

        assertNotNull(responseDoc.getBody());
        assertEquals(Status.FINISHED, responseDoc.getBody().getStatus());

    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}
