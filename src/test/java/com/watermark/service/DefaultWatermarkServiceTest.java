package com.watermark.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.watermark.model.ContentType;
import com.watermark.model.Document;
import com.watermark.model.Status;
import com.watermark.model.Ticket;
import com.watermark.model.TopicType;
import com.watermark.model.WatermarkBook;
import com.watermark.repository.WatermarkRepository;
import com.watermark.util.DocumentUtil;

public class DefaultWatermarkServiceTest {

    @Autowired
    private WatermarkService service;

    @Mock
    private WatermarkRepository repository;

    private List<Document> data;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new DefaultWatermarkService(repository);
        data = DocumentUtil.addData();
    }

    @Test
    public void shouldReturnAtLeastOneDocument() throws InterruptedException, ExecutionException {
        assertNotNull(service);
        when(repository.getAllDocuments()).thenReturn(data);
        List<Document> documents = service.getAllDocuments().get();
        assertNotNull(documents);
        assertTrue(documents.size() > 0);
    }

    @Test
    public void shouldReturnAllDocumentsMocked() throws InterruptedException, ExecutionException {
        when(repository.getAllDocuments()).thenReturn(data);
        List<Document> documents = service.getAllDocuments().get();
        assertNotNull(documents);
        assertTrue(documents.size() > 0);
        assertEquals(documents, data);
    }

    @Test
    public void shouldReturnDocumentByTicket() throws InterruptedException, ExecutionException {
        Ticket ticketLookup = data.get(1).getTicket();
        when(repository.findById(anyString())).thenReturn(data.get(0));
        Document document = service.getDocumentByTicket(ticketLookup).get();
        assertNotNull(document);
        assertEquals(data.get(0), document);
    }

    @Test
    public void shouldNotReturnDocumentWhenNoTicketValueIsProvided() throws InterruptedException, ExecutionException {
        Ticket ticketLookup = new Ticket("");
        when(repository.findById(anyString())).thenReturn(null);
        Document document = service.getDocumentByTicket(ticketLookup).get();
        assertNull(document);
    }

    @Test
    public void shouldCreateDocument() throws InterruptedException, ExecutionException {
        Ticket ticket = DocumentUtil.generateTicket();
        Document toCreate = new Document("Learning Java in 1 day", "John Doe", null, ticket, null);
        when(repository.saveDocument(anyObject())).thenReturn(ticket.getTicket());
        Ticket ticketReturned = service.createDocument(toCreate).get();
        assertNotNull(ticketReturned);
        assertEquals(ticket.getTicket(), ticketReturned.getTicket());
    }

    @Test
    public void shouldCreateDocumentWithStatusInProgress() throws InterruptedException, ExecutionException {
        Ticket ticket = DocumentUtil.generateTicket();
        Document toCreate = new Document("Learning Java in 1 day", "John Doe", null, ticket, null);
        when(repository.saveDocument(anyObject())).thenReturn(ticket.getTicket());
        Ticket ticketReturned = service.createDocument(toCreate).get();
        assertNotNull(ticketReturned);
        assertEquals(ticket.getTicket(), ticketReturned.getTicket());
        assertEquals(toCreate.getStatus(), Status.INPROGRESS);
    }

    @Test
    public void shouldCreateDocumentWithStatusFinished() throws InterruptedException, ExecutionException {
        Ticket ticket = DocumentUtil.generateTicket();
        WatermarkBook property = new WatermarkBook();
        property.setAuthor("John Doe");
        property.setContent(ContentType.BOOK.toString());
        property.setTitle("Learning Java en 1 day");
        property.setTopic(TopicType.MEDIA.toString());
        Document toCreate = new Document("Learning Java in 1 day", "John Doe", property, ticket, null);
        when(repository.saveDocument(anyObject())).thenReturn(ticket.getTicket());
        Ticket ticketReturned = service.createDocument(toCreate).get();
        assertNotNull(ticketReturned);
        assertEquals(ticket.getTicket(), ticketReturned.getTicket());
        assertEquals(toCreate.getStatus(), Status.FINISHED);
    }

    @Test
    public void shouldCreateDocumentWithStatusInProgress_TopicNotProvided() throws InterruptedException, ExecutionException {
        Ticket ticket = DocumentUtil.generateTicket();
        WatermarkBook property = new WatermarkBook();
        property.setAuthor("John Doe");
        property.setContent(ContentType.BOOK.toString());
        property.setTitle("Learning Java en 1 day");
        Document toCreate = new Document("Learning Java in 1 day", "John Doe", property, ticket, null);
        when(repository.saveDocument(anyObject())).thenReturn(ticket.getTicket());
        Ticket ticketReturned = service.createDocument(toCreate).get();
        assertNotNull(ticketReturned);
        assertEquals(ticket.getTicket(), ticketReturned.getTicket());
        assertEquals(toCreate.getStatus(), Status.INPROGRESS);
    }

    @Test
    public void shouldUpdateADocumentWhenExists() throws InterruptedException, ExecutionException {
        Ticket ticket = data.get(1).getTicket();
        Document docToUpdate = data.get(1);
        docToUpdate.setTitle("New Title to test");
        when(repository.findById(anyString())).thenReturn(data.get(1));
        when(repository.updateDocument(anyObject())).thenReturn(docToUpdate);
        Document docUpdated = service.updateDocument(ticket, docToUpdate).get();
        assertNotNull(docUpdated);
        assertEquals(docToUpdate, docUpdated);
    }

    @Test
    public void shouldNotUpdateADocumentIfNotExists_AndReturnNullForObjectAfterPromise() throws InterruptedException, ExecutionException {
        Ticket ticket = new Ticket("000c5089c4084ebcaf8bb0c8e0b1b2e5");
        Document docToUpdate = data.get(1);
        docToUpdate.setTitle("New Title to test");
        when(repository.findById(ticket.getTicket())).thenReturn(null);
        Document docUpdated = service.updateDocument(docToUpdate.getTicket(), docToUpdate).get();
        assertNull(docUpdated);
    }

    @Test
    public void shouldUpdateADocumentAndChangeStatusInProgressToFinished() throws InterruptedException, ExecutionException {
        Document toCreate = new Document("Learning Java in 1 day", "John Doe", null, null, null);
        Ticket ticket = new Ticket("000c5089c4084ebcaf8bb0c8e0b1b2e5");
        when(repository.findById(ticket.getTicket())).thenReturn(toCreate);
        toCreate.setStatus(Status.INPROGRESS);
        toCreate.setWatermarkProperty(data.get(0).getWatermarkProperty());
        when(repository.updateDocument(anyObject())).thenReturn(toCreate);
        Document documentUpdated = service.updateDocument(ticket, toCreate).get();
        assertEquals(Status.FINISHED, documentUpdated.getStatus());
    }

    @Test
    public void shouldUpdateADocumentWithStatusFinishedAndUpdateOnlyData() throws InterruptedException, ExecutionException {
        Document toCreate = new Document("Learning Java in 1 day", "John Doe", null, null, null);
        Ticket ticket = new Ticket("000c5089c4084ebcaf8bb0c8e0b1b2e5");
        when(repository.findById(ticket.getTicket())).thenReturn(toCreate);
        toCreate.setStatus(Status.FINISHED);
        toCreate.setWatermarkProperty(data.get(0).getWatermarkProperty());
        when(repository.updateDocument(anyObject())).thenReturn(toCreate);
        Document documentUpdated = service.updateDocument(ticket, toCreate).get();
        assertEquals(Status.FINISHED, documentUpdated.getStatus());
        assertEquals(toCreate, documentUpdated);
    }

}
