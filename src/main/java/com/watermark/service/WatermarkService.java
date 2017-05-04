package com.watermark.service;

import java.util.List;
import java.util.concurrent.Future;

import com.watermark.model.Document;
import com.watermark.model.Ticket;

public interface WatermarkService {

    public Future<List<Document>> getAllDocuments();

    public Future<Document> getDocumentByTicket(Ticket ticket);

    public Future<Ticket> createDocument(Document document);

    public Future<Document> updateDocument(Ticket ticket, Document document);

}
